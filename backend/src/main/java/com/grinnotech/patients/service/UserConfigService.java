package com.grinnotech.patients.service;

import ch.ralscha.extdirectspring.annotation.ExtDirectMethod;
import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.grinnotech.patients.config.profiles.mongodb.MongoDb;
import com.grinnotech.patients.config.security.MongoUserDetails;
import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.dao.authorities.RequireAnyAuthority;
import com.grinnotech.patients.dto.UserSettings;
import com.grinnotech.patients.model.CPersistentLogin;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.PersistentLogin;
import com.grinnotech.patients.model.User;
import com.grinnotech.patients.util.TotpAuthUtil;
import com.grinnotech.patients.util.ValidationMessages;
import com.grinnotech.patients.util.ValidationMessagesResult;
import com.grinnotech.patients.util.ValidationUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import eu.bitwalker.useragentutils.UserAgent;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_MODIFY;
import static ch.ralscha.extdirectspring.annotation.ExtDirectMethodType.STORE_READ;

@Service
@Cacheable("main")
@RequireAnyAuthority
public class UserConfigService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoDb mongoDb;

    @Autowired
    private UserRepository userRepository;

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

        mongoDb.getCollection(User.class).updateOne(
                Filters.eq(CUser.id, userDetails.getUserDbId()),
                Updates.set(CUser.secret, randomSecret));

        return randomSecret;
    }

    @ExtDirectMethod
    public void disable2f(@AuthenticationPrincipal MongoUserDetails userDetails) {
        mongoDb.getCollection(User.class).updateOne(
                Filters.eq(CUser.id, userDetails.getUserDbId()),
                Updates.unset(CUser.secret));
    }

    @ExtDirectMethod(STORE_MODIFY)
    public ValidationMessagesResult<UserSettings> updateSettings(UserSettings modifiedUserSettings,
            @AuthenticationPrincipal MongoUserDetails userDetails, Locale locale) {

        List<ValidationMessages> validations = ValidationUtil.validateEntity(validator, modifiedUserSettings);
        User user = userDetails.getUser(userRepository);
        List<Bson> updates = new ArrayList<>();

        if (StringUtils.hasText(modifiedUserSettings.getNewPassword()) && validations.isEmpty()) {
            if (passwordEncoder.matches(modifiedUserSettings.getCurrentPassword(), user.getPasswordHash())) {
                if (modifiedUserSettings.getNewPassword().equals(modifiedUserSettings.getNewPasswordRetype())) {
                    user.setPasswordHash(passwordEncoder.encode(modifiedUserSettings.getNewPassword()));
                    updates.add(Updates.set(CUser.passwordHash, user.getPasswordHash()));
                } else {
                    for (String field : new String[]{"newPassword", "newPasswordRetype"}) {
                        ValidationMessages error = new ValidationMessages();
                        error.setField(field);
                        error.setMessage(messageSource.getMessage("userconfig_pwdonotmatch", null, locale));
                        validations.add(error);
                    }
                }
            } else {
                ValidationMessages error = new ValidationMessages();
                error.setField("currentPassword");
                error.setMessage(messageSource.getMessage("userconfig_wrongpassword", null, locale));
                validations.add(error);
            }
        }

        if (!isEmailUnique(user.getId(), modifiedUserSettings.getEmail())) {
            ValidationMessages validationError = new ValidationMessages();
            validationError.setField(CUser.email);
            validationError.setMessage(messageSource.getMessage("user_emailtaken", null, locale));
            validations.add(validationError);
        }

        if (validations.isEmpty()) {
            user.setLastName(modifiedUserSettings.getLastName());
            user.setFirstName(modifiedUserSettings.getFirstName());
            user.setEmail(modifiedUserSettings.getEmail());
            user.setLocale(modifiedUserSettings.getLocale());

            updates.add(Updates.set(CUser.lastName, user.getLastName()));
            updates.add(Updates.set(CUser.firstName, user.getFirstName()));
            updates.add(Updates.set(CUser.email, user.getEmail()));
            updates.add(Updates.set(CUser.locale, user.getLocale()));
        }

        if (!updates.isEmpty()) {
            mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, user.getId()), Updates.combine(updates));
        }

        return new ValidationMessagesResult<>(modifiedUserSettings, validations);
    }

    private boolean isEmailUnique(String userId, String email) {
        return userRepository.countByEmailRegexAndIdNot(email, userId) == 0;
    }

    @ExtDirectMethod(STORE_READ)
    public List<PersistentLogin> readPersistentLogins(@AuthenticationPrincipal MongoUserDetails userDetails) {
        return StreamSupport.stream(
//                persistentLoginRepository.findByUserId(userDetails.getUserDbId()).spliterator(),
                mongoDb.getCollection(PersistentLogin.class).find(Filters.eq(CPersistentLogin.userId, userDetails.getUserDbId())).spliterator(),
                false).peek(p -> {
                    String ua = p.getUserAgent();
                    if (StringUtils.hasText(ua)) {
                        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
                        p.setUserAgentName(userAgent.getBrowser().getGroup().getName());
                        p.setUserAgentVersion(userAgent.getBrowserVersion().getMajorVersion());
                        p.setOperatingSystem(userAgent.getOperatingSystem().getName());
                    }
                }).collect(Collectors.toList());
    }

    @ExtDirectMethod(STORE_MODIFY)
    public void destroyPersistentLogin(String series, @AuthenticationPrincipal MongoUserDetails userDetails) {
//        persistentLoginRepository.deletePersistentLoginBySeriesAndUserId(series, userDetails.getUserDbId());
        mongoDb.getCollection(PersistentLogin.class).deleteOne(Filters.and(
                Filters.eq(CPersistentLogin.series, series),
                Filters.eq(CPersistentLogin.userId, userDetails.getUserDbId())));
    }

}
