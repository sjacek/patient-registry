package com.grinnotech.patients.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.POLL;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.annotation.ExtDirectMethodType;
import ch.ralscha.extdirectspring.bean.ExtDirectFormPostResult;
import com.grinnotech.patients.config.MongoDb;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAdminAuthority;
import com.grinnotech.patients.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patients.dto.UserDetailDto;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.util.TotpAuthUtil;
import com.grinnotech.patients.web.CsrfController;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String AUTH_USER = "authUser";

    @Autowired
    private MongoDb mongoDb;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @ExtDirectMethod
    public UserDetailDto getAuthUser(@AuthenticationPrincipal MongoUserDetails userDetails) {

        LOGGER.debug("getAuthUser");
        if (userDetails == null) {
            return null;
        }
                    
        User user = userDetails.getUser(userRepository);
        UserDetailDto userDetailDto = new UserDetailDto(userDetails, user, null);

        if (!userDetails.isPreAuth()) {
            user.setLastAccess(new Date());
            userRepository.save(user);
        }

        return userDetailDto;
    }

    @ExtDirectMethod(ExtDirectMethodType.FORM_POST)
    @PreAuthorize("hasAuthority('PRE_AUTH')")
    public ExtDirectFormPostResult signin2fa(HttpServletRequest request, @AuthenticationPrincipal MongoUserDetails userDetails, @RequestParam("code") int code) {

        User user = userDetails.getUser(userRepository);
        if (user != null) {
            if (TotpAuthUtil.verifyCode(user.getSecret(), code, 3)) {
                user.setLastAccess(new Date());
                userRepository.save(user);
                userDetails.grantAuthorities();

                Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);

                ExtDirectFormPostResult result = new ExtDirectFormPostResult();
                result.addResultProperty(AUTH_USER, new UserDetailDto(userDetails, user, CsrfController.getCsrfToken(request)));
                return result;
            }

            BadCredentialsException excp = new BadCredentialsException("Bad verification code");
            AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
                    SecurityContextHolder.getContext().getAuthentication(), excp);
            applicationEventPublisher.publishEvent(event);

            user = userDetails.getUser(userRepository);
            if (user.getLockedOutUntil() != null) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    LOGGER.debug("Invalidating session: " + session.getId());
                    session.invalidate();
                }
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(null);
                SecurityContextHolder.clearContext();
            }
        }

        return new ExtDirectFormPostResult(false);
    }

    @ExtDirectMethod
    @RequireAnyAuthority
    public void enableScreenLock(@AuthenticationPrincipal MongoUserDetails userDetails) {
        userDetails.setScreenLocked(true);
    }

    @ExtDirectMethod(ExtDirectMethodType.FORM_POST)
    @RequireAnyAuthority
    public ExtDirectFormPostResult disableScreenLock(@AuthenticationPrincipal MongoUserDetails userDetails, @RequestParam("password") String password) {

        User user = mongoDb.getCollection(User.class)
                .find(Filters.eq(CUser.id, userDetails.getUserDbId()))
                .projection(Projections.include(CUser.passwordHash)).first();

        boolean matches = passwordEncoder.matches(password, user.getPasswordHash());
        userDetails.setScreenLocked(!matches);
        ExtDirectFormPostResult result = new ExtDirectFormPostResult(matches);

        return result;
    }

    @ExtDirectMethod(ExtDirectMethodType.FORM_POST)
    public ExtDirectFormPostResult resetRequest(@RequestParam("email") String email) {

        String token = UUID.randomUUID().toString();

        User user = mongoDb.getCollection(User.class).findOneAndUpdate(
                Filters.and(Filters.eq(CUser.email, email), Filters.eq(CUser.deleted, false)),
                Updates.combine(
                        Updates.set(CUser.passwordResetTokenValidUntil, Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(4).toInstant())),
                        Updates.set(CUser.passwordResetToken, token)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(false));

        if (user != null) {
            this.mailService.sendPasswortResetEmail(user);
        }

        return new ExtDirectFormPostResult();
    }

    @ExtDirectMethod(ExtDirectMethodType.FORM_POST)
    public ExtDirectFormPostResult reset(@RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordRetype") String newPasswordRetype,
            @RequestParam("token") String token) {

        if (StringUtils.hasText(token) && StringUtils.hasText(newPassword)
                && StringUtils.hasText(newPasswordRetype)
                && newPassword.equals(newPasswordRetype)) {
            String decodedToken = new String(Base64.getUrlDecoder().decode(token));
            
            User user = userRepository.findByPasswordResetTokenNotDeletedAndEnabled(decodedToken);

            if (user != null && user.getPasswordResetTokenValidUntil() != null) {

                ExtDirectFormPostResult result;
//                List<Bson> updates = new ArrayList<>();

                if (user.getPasswordResetTokenValidUntil().after(new Date())) {
                    user.setPasswordHash(this.passwordEncoder.encode(newPassword));
                    user.setSecret(null);
//                    updates.add(Updates.unset(CUser.secret));
//                    updates.add(Updates.set(CUser.passwordHash, user.getPasswordHash()));

                    MongoUserDetails principal = new MongoUserDetails(user);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    result = new ExtDirectFormPostResult();
                    result.addResultProperty(AUTH_USER, new UserDetailDto(principal, user, null));
                } else {
                    result = new ExtDirectFormPostResult(false);
                }
                user.setPasswordResetToken(null);
                user.setPasswordResetTokenValidUntil(null);
                userRepository.save(user);
                
//                updates.add(Updates.unset(CUser.passwordResetToken));
//                updates.add(Updates.unset(CUser.passwordResetTokenValidUntil));
//
//                mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, user.getId()), Updates.combine(updates));

                return result;
            }
        }

        return new ExtDirectFormPostResult(false);
    }

    @ExtDirectMethod
    @RequireAdminAuthority
    public UserDetailDto switchUser(String userId) {
        User switchToUser = userRepository.findOneNotDeleted(userId);
        if (switchToUser != null) {

            MongoUserDetails principal = new MongoUserDetails(switchToUser);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);

            return new UserDetailDto(principal, switchToUser, null);
        }

        return null;
    }

    @ExtDirectMethod(value = POLL, event = "heartbeat")
    @RequireAnyAuthority
    public void heartbeat() {
        // nothing here
    }

}
