package com.grinnotech.patientsorig.service;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

import com.grinnotech.patientsorig.NotFoundException;
import com.grinnotech.patientsorig.config.security.MongoUserDetails;
import com.grinnotech.patientsorig.dao.PersistentLoginRepository;
import com.grinnotech.patientsorig.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patientsorig.dto.UserSettings;
import com.grinnotech.patients.model.PersistentLogin;
import com.grinnotech.patients.mongodb.dao.UserRepository;
import com.grinnotech.patients.mongodb.model.User;
import com.grinnotech.patientsorig.util.TotpAuthUtil;
import com.grinnotech.patientsorig.util.ValidationMessages;
import com.grinnotech.patientsorig.util.ValidationMessagesResult;
import com.grinnotech.patientsorig.util.ValidationUtil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Validator;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import eu.bitwalker.useragentutils.UserAgent;

@Service
@RequireAnyAuthority
public class UserConfigService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final PersistentLoginRepository persistentLoginRepository;

	private final Validator validator;

	private final MessageSource messageSource;

	@Contract(pure = true)
	public UserConfigService(PasswordEncoder passwordEncoder, UserRepository userRepository,
			PersistentLoginRepository persistentLoginRepository, Validator validator, MessageSource messageSource) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.persistentLoginRepository = persistentLoginRepository;
		this.validator = validator;
		this.messageSource = messageSource;
	}

	@ExtDirectMethod(STORE_READ)
	public ExtDirectStoreResult<UserSettings> readSettings(@NotNull @AuthenticationPrincipal MongoUserDetails userDetails)
			throws NotFoundException {
		Optional<User> user = userRepository.findById(userDetails.getUserDbId());
		User user1 = user.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));

		UserSettings userSettings = new UserSettings(user1);
		return new ExtDirectStoreResult<>(userSettings);
	}

	@ExtDirectMethod
	public String enable2f(@NotNull @AuthenticationPrincipal MongoUserDetails userDetails) throws NotFoundException {
		String randomSecret = TotpAuthUtil.randomSecret();

		Optional<User> user = userRepository.findById(userDetails.getUserDbId());
		User user1 = user.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));

		user1.setSecret(randomSecret);
		userRepository.save(user1);

		return randomSecret;
	}

	@ExtDirectMethod
	public void disable2f(@NotNull @AuthenticationPrincipal MongoUserDetails userDetails) throws NotFoundException {
		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		User user = oUser.orElseThrow(() -> new NotFoundException("User id={} not found", userDetails.getUserDbId()));
		user.setSecret(null);
		userRepository.save(user);
	}

	@ExtDirectMethod(STORE_MODIFY)
	public ValidationMessagesResult<UserSettings> updateSettings(UserSettings modifiedUserSettings,
			@NotNull @AuthenticationPrincipal MongoUserDetails userDetails, Locale locale) {

		List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, modifiedUserSettings);
		Optional<User> oUser = userRepository.findById(userDetails.getUserDbId());
		User user = oUser.orElseThrow(() -> new javax.ws.rs.NotFoundException(
				String.format("User id={} not found", userDetails.getUserDbId())));
		boolean userModified = false;

		if (hasText(modifiedUserSettings.getNewPassword()) && validations.isEmpty()) {
			if (passwordEncoder.matches(modifiedUserSettings.getCurrentPassword(), user.getPasswordHash())) {
				if (modifiedUserSettings.getNewPassword().equals(modifiedUserSettings.getNewPasswordRetype())) {
					user.setPasswordHash(passwordEncoder.encode(modifiedUserSettings.getNewPassword()));
					userModified = true;
				} else {
					validations.add(ValidationMessages.builder().field("newPassword")
							.message(messageSource.getMessage("userconfig_pwdonotmatch", null, locale)).build());
					validations.add(ValidationMessages.builder().field("newPasswordRetype")
							.message(messageSource.getMessage("userconfig_pwdonotmatch", null, locale)).build());
				}
			} else {
				validations.add(ValidationMessages.builder().field("currentPassword")
						.message(messageSource.getMessage("userconfig_wrongpassword", null, locale)).build());
			}
		}

		if (!isEmailUnique(user.getId(), modifiedUserSettings.getEmail())) {
//			validations.add(ValidationMessages.builder().field(CUser.email)
//					.message(messageSource.getMessage("user_emailtaken", null, locale)).build());
		}

		if (validations.isEmpty()) {
			user.setLastName(modifiedUserSettings.getLastName());
			user.setFirstName(modifiedUserSettings.getFirstName());
			user.setEmail(modifiedUserSettings.getEmail());
			user.setLocale(modifiedUserSettings.getLocale());
			userModified = true;
		}

		if (userModified) {
			userRepository.save(user);
		}

		return new ValidationMessagesResult<>(modifiedUserSettings, validations);
	}

	private boolean isEmailUnique(String userId, String email) {
		return userRepository.existsByEmailRegexAndIdNot(email, userId);
	}

	@ExtDirectMethod(STORE_READ)
	public List<PersistentLogin> readPersistentLogins(@NotNull @AuthenticationPrincipal MongoUserDetails userDetails) {
		return persistentLoginRepository.findByUserId(userDetails.getUserDbId()).stream().peek(login -> {
			String ua = login.getUserAgent();
			if (hasText(ua)) {
				UserAgent userAgent = UserAgent.parseUserAgentString(ua);
				login.setUserAgentName(userAgent.getBrowser().getGroup().getName());
				login.setUserAgentVersion(userAgent.getBrowserVersion().getMajorVersion());
				login.setOperatingSystem(userAgent.getOperatingSystem().getName());
			}
		}).collect(toList());
	}

	@ExtDirectMethod(STORE_MODIFY)
	public void destroyPersistentLogin(String series, @NotNull @AuthenticationPrincipal MongoUserDetails userDetails) {
		persistentLoginRepository.deletePersistentLoginBySeriesAndUserId(series, userDetails.getUserDbId());
	}

}
