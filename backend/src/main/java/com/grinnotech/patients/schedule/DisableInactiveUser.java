package com.grinnotech.patients.schedule;

import com.grinnotech.patients.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;

@Component
public class DisableInactiveUser {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 5 * * *")
    public void doCleanup() {
        // Inactivate users that have a lastAccess timestamp that is older than one year
        ZonedDateTime oneYearAgo = now(UTC).minusYears(1);
//        mongoDb.getCollection(User.class).updateMany(
//                Filters.lte(CUser.lastAccess, Date.from(oneYearAgo.toInstant())),
//                Updates.set(CUser.enabled, false));
        userRepository.setNotEnabledByLastAccessIsGreaterThan(Date.from(oneYearAgo.toInstant()));
    }

}
