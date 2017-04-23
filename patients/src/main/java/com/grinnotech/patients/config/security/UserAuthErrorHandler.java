package com.grinnotech.patients.config.security;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;

import com.grinnotech.patients.config.AppProperties;
import com.grinnotech.patients.config.MongoDb;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.User;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserAuthErrorHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final MongoDb mongoDb;

    private final Integer loginLockAttempts;

    private final Integer loginLockMinutes;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

        if (this.loginLockAttempts != null && (principal instanceof String || principal instanceof MongoUserDetails)) {

            User user = null;
            if (principal instanceof String) {

                user = mongoDb.getCollection(User.class).findOneAndUpdate(
                        Filters.and(Filters.eq(CUser.email, principal),
                                Filters.eq(CUser.deleted, false)),
                        Updates.inc(CUser.failedLogins, 1), new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER).upsert(false));
            } else {

                user = mongoDb.getCollection(User.class).findOneAndUpdate(
                        Filters.eq(CUser.id,
                                ((MongoUserDetails) principal).getUserDbId()),
                        Updates.inc(CUser.failedLogins, 1), new FindOneAndUpdateOptions()
                        .returnDocument(ReturnDocument.AFTER).upsert(false));
            }

            if (user != null) {
                if (user.getFailedLogins() >= this.loginLockAttempts) {
                    if (loginLockMinutes != null) {
                        mongoDb.getCollection(User.class).updateOne(
                                Filters.eq(CUser.id, user.getId()),
                                Updates.set(CUser.lockedOutUntil,
                                        Date.from(ZonedDateTime.now(ZoneOffset.UTC)
                                                .plusMinutes(this.loginLockMinutes)
                                                .toInstant())));
                    } else {
                        mongoDb.getCollection(User.class)
                                .updateOne(Filters.eq(CUser.id, user.getId()),
                                        Updates.set(CUser.lockedOutUntil,
                                                Date.from(ZonedDateTime
                                                        .now(ZoneOffset.UTC)
                                                        .plusYears(1000).toInstant())));
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
