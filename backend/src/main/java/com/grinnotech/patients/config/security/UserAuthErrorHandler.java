package com.grinnotech.patients.config.security;

import static com.grinnotech.patients.util.OptionalEx.ifPresent;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;

import com.grinnotech.patients.config.AppProperties;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Optional;

@Component
public class UserAuthErrorHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final UserRepository userRepository;
	private final Integer loginLockAttempts;
	private final Integer loginLockMinutes;

	@Autowired
	public UserAuthErrorHandler(UserRepository userRepository, AppProperties appProperties) {
		this.userRepository = userRepository;
		this.loginLockAttempts = appProperties.getLoginLockAttempts();
		this.loginLockMinutes = appProperties.getLoginLockMinutes();
	}

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		updateLockedProperties(event);
	}

	private void updateLockedProperties(AuthenticationFailureBadCredentialsEvent event) {
		Object principal = event.getAuthentication().getPrincipal();

		if (loginLockAttempts != null && (principal instanceof String || principal instanceof MongoUserDetails)) {

			Optional<User> oUser = principal instanceof String ?
					userRepository.findByEmailActive((String) principal) :
					userRepository.findById(((MongoUserDetails) principal).getUserDbId());

			ifPresent(oUser, user -> {
				user.setFailedLogins(user.getFailedLogins() + 1);

				if (user.getFailedLogins() >= loginLockAttempts) {
					if (loginLockMinutes != null) {
						user.setLockedOutUntil(Date.from(now(UTC).plusMinutes(loginLockMinutes).toInstant()));
					} else {
						user.setLockedOutUntil(Date.from(now(UTC).plusYears(1000).toInstant()));
					}
				}
				userRepository.save(user);
			}).orElse(() -> {
				logger.warn("Unknown user login attempt: {}", principal);
			});
		} else {
			logger.warn("Invalid login attempt: {}", principal);
		}
	}

}
