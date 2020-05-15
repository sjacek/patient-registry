package com.grinnotech.patientsorig.config.security;

import com.grinnotech.patientsorig.mongodb.dao.UserRepository;
import com.grinnotech.patientsorig.mongodb.model.User;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuthSuccessfulHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    public UserAuthSuccessfulHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(@NotNull InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof MongoUserDetails) {
            String userId = ((MongoUserDetails) principal).getUserDbId();

            Optional<User> oUser = userRepository.findById(userId);
            oUser.ifPresent(user -> {
                user.setFailedLogins(0);
                user.setLockedOutUntil(null);
                userRepository.save(user);
            });
        }
    }
}
