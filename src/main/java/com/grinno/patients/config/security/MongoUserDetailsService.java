package com.grinno.patients.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mongodb.client.model.Filters;

import com.grinno.patients.config.MongoDb;
import com.grinno.patients.model.CUser;
import com.grinno.patients.model.User;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    private final MongoDb mongoDb;

    @Autowired
    public MongoUserDetailsService(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    @Override
    public UserDetails loadUserByUsername(String loginName)
            throws UsernameNotFoundException {
        User user = this.mongoDb.getCollection(User.class)
                .find(Filters.and(Filters.eq(CUser.email, loginName),
                        Filters.eq(CUser.deleted, false)))
                .first();
        if (user != null) {
            return new MongoUserDetails(user);
        }

        throw new UsernameNotFoundException(loginName);
    }

}
