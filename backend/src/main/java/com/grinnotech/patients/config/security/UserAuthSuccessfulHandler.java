package com.grinnotech.patients.config.security;

import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class UserAuthSuccessfulHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof MongoUserDetails) {
            String userId = ((MongoUserDetails) principal).getUserDbId();

            User user = userRepository.findOneActive(userId);
            if (user != null) {
                user.setFailedLogins(0);
                user.setLockedOutUntil(null);
                userRepository.save(user);
            }
        }
    }
}
