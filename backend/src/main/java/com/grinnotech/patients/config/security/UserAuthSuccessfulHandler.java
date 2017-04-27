package com.grinnotech.patients.config.security;

import com.grinnotech.patients.config.MongoDb;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.User;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

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
