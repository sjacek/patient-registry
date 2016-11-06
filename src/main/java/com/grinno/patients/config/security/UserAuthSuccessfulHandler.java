package com.grinno.patients.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import com.grinno.patients.config.MongoDb;
import com.grinno.patients.model.CUser;
import com.grinno.patients.model.User;

@Component
public class UserAuthSuccessfulHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final MongoDb mongoDb;

    @Autowired
    public UserAuthSuccessfulHandler(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof MongoUserDetails) {
            String userId = ((MongoUserDetails) principal).getUserDbId();

            this.mongoDb.getCollection(User.class).updateOne(Filters.eq(CUser.id, userId),
                    Updates.combine(Updates.unset(CUser.lockedOutUntil),
                            Updates.set(CUser.failedLogins, 0)));
        }
    }
}
