package com.grinnotech.patients.config.security;

import com.grinnotech.patients.config.AppProperties;
import com.grinnotech.patients.config.profiles.mongodb.MongoDb;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.User;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;

@Component
public class UserAuthErrorHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MongoDb mongoDb;
    private final Integer loginLockAttempts;
    private final Integer loginLockMinutes;

    @Autowired
    public UserAuthErrorHandler(MongoDb mongoDb, AppProperties appProperties) {
        this.mongoDb = mongoDb;
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

            User user = principal instanceof String ?
                    mongoDb.getCollection(User.class).findOneAndUpdate(
                            Filters.and(Filters.eq(CUser.email, principal),
                                    Filters.eq(CUser.deleted, false)),
                            Updates.inc(CUser.failedLogins, 1),
                            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(false))
                    :
                    mongoDb.getCollection(User.class).findOneAndUpdate(
                            Filters.eq(CUser.id,
                                    ((MongoUserDetails) principal).getUserDbId()),
                            Updates.inc(CUser.failedLogins, 1),
                            new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER).upsert(false));

            if (user != null) {
                if (user.getFailedLogins() >= loginLockAttempts) {
                    if (loginLockMinutes != null) {
                        mongoDb.getCollection(User.class).updateOne(
                                Filters.eq(CUser.id, user.getId()),
                                Updates.set(CUser.lockedOutUntil,
                                        Date.from(now(UTC).plusMinutes(loginLockMinutes).toInstant())));
                    } else {
                        mongoDb.getCollection(User.class).updateOne(
                                Filters.eq(CUser.id, user.getId()),
                                Updates.set(CUser.lockedOutUntil,
                                        Date.from(now(UTC).plusYears(1000).toInstant())));
                    }
                }
            } else {
                LOGGER.warn("Unknown user login attempt: {}", principal);
            }
        } else {
            LOGGER.warn("Invalid login attempt: {}", principal);
        }
    }

}
