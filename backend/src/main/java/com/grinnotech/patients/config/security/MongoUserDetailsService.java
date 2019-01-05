package com.grinnotech.patients.config.security;

import com.grinnotech.patients.dao.UserRepository;
import com.grinnotech.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginName)
            throws UsernameNotFoundException {
        Optional<User> oUser = userRepository.findByEmailActive(loginName);
        User user = oUser.orElseThrow(() -> new UsernameNotFoundException(loginName));

        return new MongoUserDetails(user);
    }

}
