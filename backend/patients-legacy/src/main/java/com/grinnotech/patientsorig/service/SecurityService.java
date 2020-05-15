package com.grinnotech.patientsorig.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.FORM_POST;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.POLL;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.Date.from;

import com.grinnotech.patientsorig.NotFoundException;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.authorities.RequireAdminAuthority;
import com.grinnotech.patientsorig.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patientsorig.dto.UserDetailDto;
import com.grinnotech.patients.mongodb.dao.UserRepository;
import com.grinnotech.patients.mongodb.model.User;
import com.grinnotech.patientsorig.util.TotpAuthUtil;
import com.grinnotech.patientsorig.web.CsrfController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.MethodHandles;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectFormPostResult;

@Service
public class SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static final String AUTH_USER = "authUser";

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final MailService mailService;

	private final ApplicationEventPublisher applicationEventPublisher;

	public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService,
			ApplicationEventPublisher applicationEventPublisher) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@ExtDirectMethod
	public UserDetailDto getAuthUser(@AuthenticationPrincipal MongoUserDetails userDetails) throws NotFoundException {

		logger.debug("getAuthUser {}", userDetails);
		if (userDetails == null) {
			return null;
		}

		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));

		userRepository.loadOrganizationsData(user);
		UserDetailDto userDetailDto = new UserDetailDto(userDetails, user, null);

		if (!userDetails.isPreAuth()) {
			user.setLastAccess(new Date());
			userRepository.save(user);
		}

		return userDetailDto;
	}

	@ExtDirectMethod(FORM_POST)
	@PreAuthorize("hasAuthority('PRE_AUTH')")
	public ExtDirectFormPostResult signin2fa(HttpServletRequest request,
			@AuthenticationPrincipal MongoUserDetails userDetails, @RequestParam("code") int code)
			throws NotFoundException {

		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));

		if (TotpAuthUtil.verifyCode(user.getSecret(), code, 3)) {
			user.setLastAccess(new Date());
			userRepository.save(user);
			userDetails.grantAuthorities();

			Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);

			ExtDirectFormPostResult result = new ExtDirectFormPostResult();
			userRepository.loadOrganizationsData(user);
			result.addResultProperty(AUTH_USER,
					new UserDetailDto(userDetails, user, CsrfController.getCsrfToken(request)));
			return result;
		}

		BadCredentialsException excp = new BadCredentialsException("Bad verification code");
		AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
				SecurityContextHolder.getContext().getAuthentication(), excp);
		applicationEventPublisher.publishEvent(event);

		oUser = userRepository.findById(userDetails.getUserDbId());
		user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));
		if (user.getLockedOutUntil() != null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				logger.debug("Invalidating session: " + session.getId());
				session.invalidate();
			}
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(null);
			SecurityContextHolder.clearContext();
		}

		return new ExtDirectFormPostResult(false);
	}

	@ExtDirectMethod
	@RequireAnyAuthority
	public void enableScreenLock(@AuthenticationPrincipal MongoUserDetails userDetails) {
		userDetails.setScreenLocked(true);
	}

	@ExtDirectMethod(FORM_POST)
	@RequireAnyAuthority
	public ExtDirectFormPostResult disableScreenLock(@AuthenticationPrincipal MongoUserDetails userDetails,
			@RequestParam("password") String password) throws NotFoundException {

		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));

		boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
		userDetails.setScreenLocked(!matches);

		return new ExtDirectFormPostResult(matches);
	}

	@ExtDirectMethod(FORM_POST)
	public ExtDirectFormPostResult resetRequest(@RequestParam("email") String email) throws NotFoundException {

		String token = UUID.randomUUID().toString();

		Optional<User> oUser = userRepository.findByEmailActive(email);
		User user = oUser.orElseThrow(() -> new NotFoundException("User with email={} not found", email));

		user.setPasswordResetTokenValidUntil(from(now(UTC).plusHours(4).toInstant()));
		user.setPasswordResetToken(token);
		userRepository.save(user);

		mailService.sendPasswortResetEmail(user);

		return new ExtDirectFormPostResult();
	}

	@ExtDirectMethod(FORM_POST)
	public ExtDirectFormPostResult reset(@RequestParam("newPassword") String newPassword,
			@RequestParam("newPasswordRetype") String newPasswordRetype, @RequestParam("token") String token) {

		if (StringUtils.hasText(token) && StringUtils.hasText(newPassword) && StringUtils.hasText(newPasswordRetype)
				&& newPassword.equals(newPasswordRetype)) {
			String decodedToken = new String(Base64.getUrlDecoder().decode(token));

			User user = userRepository.findOneByPasswordResetTokenAndEnabled(decodedToken);
			if (user != null && user.getPasswordResetTokenValidUntil() != null) {

				ExtDirectFormPostResult result;

				if (user.getPasswordResetTokenValidUntil().after(new Date())) {
					user.setPasswordHash(passwordEncoder.encode(newPassword));
					user.setSecret(null);

					MongoUserDetails principal = new MongoUserDetails(user);
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal,
							null, principal.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);

					result = new ExtDirectFormPostResult();
					userRepository.loadOrganizationsData(user);
					result.addResultProperty(AUTH_USER, new UserDetailDto(principal, user, null));
				} else {
					result = new ExtDirectFormPostResult(false);
				}
				user.setPasswordResetToken(null);
				user.setPasswordResetTokenValidUntil(null);
				userRepository.save(user);

				return result;
			}
		}

		return new ExtDirectFormPostResult(false);
	}

	@ExtDirectMethod
	@RequireAdminAuthority
	public UserDetailDto switchUser(String userId) throws NotFoundException {
		Optional<User> oSwitchToUser = userRepository.findById(userId);
		User switchToUser = oSwitchToUser.orElseThrow(() -> new NotFoundException("User id={} not found", userId));
		userRepository.loadOrganizationsData(switchToUser);

		MongoUserDetails principal = new MongoUserDetails(switchToUser);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				principal.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(token);

		return new UserDetailDto(principal, switchToUser, null);
	}

	@ExtDirectMethod(value = POLL, event = "heartbeat")
	@RequireAnyAuthority
	public void heartbeat() {
		// nothing here
	}

}
