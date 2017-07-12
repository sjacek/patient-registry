package com.grinnotech.patients.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.PersistentLoginRepository;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patients.dto.UserSettings;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.PersistentLogin;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.util.TotpAuthUtil;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import com.grinnotech.patients.util.ValidationUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.Validator;
import java.util.List;
import java.util.Locale;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequireAnyAuthority
public class UserConfigService {

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private MongoDb mongoDb;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentLoginRepository persistentLoginRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private MessageSource messageSource;

    @ExtDirectMethod(STORE_READ)
    public ExtDirectStoreResult<UserSettings> readSettings(@AuthenticationPrincipal MongoUserDetails userDetails) {
        UserSettings userSettings = new UserSettings(userDetails.getUser(userRepository));
        return new ExtDirectStoreResult<>(userSettings);
    }

    @ExtDirectMethod
    public String enable2f(@AuthenticationPrincipal MongoUserDetails userDetails) {
        String randomSecret = TotpAuthUtil.randomSecret();

        User user = userRepository.findOne(userDetails.getUserDbId());
        user.setSecret(randomSecret);
        userRepository.save(user);

        return randomSecret;
    }

    @ExtDirectMethod
    public void disable2f(@AuthenticationPrincipal MongoUserDetails userDetails) {
        User user = userRepository.findOne(userDetails.getUserDbId());
        user.setSecret(null);
        userRepository.save(user);
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<UserSettings> updateSettings(UserSettings modifiedUserSettings,
                                                                 @AuthenticationPrincipal MongoUserDetails userDetails, Locale locale) {

        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, modifiedUserSettings);
        User user = userDetails.getUser(userRepository);
        boolean userModified = false;

        if (hasText(modifiedUserSettings.getNewPassword()) && validations.isEmpty()) {
            if (passwordEncoder.matches(modifiedUserSettings.getCurrentPassword(), user.getPasswordHash())) {
                if (modifiedUserSettings.getNewPassword().equals(modifiedUserSettings.getNewPasswordRetype())) {
                    user.setPasswordHash(passwordEncoder.encode(modifiedUserSettings.getNewPassword()));
                    userModified = true;
                } else {
                    validations.add(ValidationMessages.builder()
                            .field("newPassword")
                            .message(messageSource.getMessage("userconfig_pwdonotmatch", null, locale))
                            .build());
                    validations.add(ValidationMessages.builder()
                            .field("newPasswordRetype")
                            .message(messageSource.getMessage("userconfig_pwdonotmatch", null, locale))
                            .build());
                }
            } else {
                validations.add(ValidationMessages.builder()
                        .field("currentPassword")
                        .message(messageSource.getMessage("userconfig_wrongpassword", null, locale))
                        .build());
            }
        }

        if (!isEmailUnique(user.getId(), modifiedUserSettings.getEmail())) {
            validations.add(ValidationMessages.builder()
                    .field(CUser.email)
                    .message(messageSource.getMessage("user_emailtaken", null, locale))
                    .build());
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
    public List<PersistentLogin> readPersistentLogins(@AuthenticationPrincipal MongoUserDetails userDetails) {
        return persistentLoginRepository.findByUserId(userDetails.getUserDbId()).stream()
//                mongoDb.getCollection(PersistentLogin.class)
//                        .find(Filters.eq(CPersistentLogin.userId, userDetails.getUserDbId())).spliterator(), false)
                .peek(login -> {
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
    public void destroyPersistentLogin(String series, @AuthenticationPrincipal MongoUserDetails userDetails) {
        persistentLoginRepository.deletePersistentLoginBySeriesAndUserId(series, userDetails.getUserDbId());
//        mongoDb.getCollection(PersistentLogin.class).deleteOne(Filters.and(
//                Filters.eq(CPersistentLogin.series, series),
//                Filters.eq(CPersistentLogin.userId, userDetails.getUserDbId())));
    }

}
