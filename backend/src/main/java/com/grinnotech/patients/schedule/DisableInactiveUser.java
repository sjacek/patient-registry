package com.grinnotech.patients.schedule;

import com.grinnotech.patients.config.MongoDb;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.User;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class DisableInactiveUser {

    @Autowired
    private MongoDb mongoDb;

    @Scheduled(cron = "0 0 5 * * *")
    public void doCleanup() {
        // Inactivate users that have a lastAccess timestamp that is older than one year
        ZonedDateTime oneYearAgo = ZonedDateTime.now(ZoneOffset.UTC).minusYears(1);
        mongoDb.getCollection(User.class).updateMany(
                Filters.lte(CUser.lastAccess, Date.from(oneYearAgo.toInstant())),
                Updates.set(CUser.enabled, false));
    }

}
