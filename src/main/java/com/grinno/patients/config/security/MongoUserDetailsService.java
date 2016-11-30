package com.grinno.patients.config.security;

import com.grinno.patients.dao.UserRepository;
import com.grinno.patients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginName)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmailNotDeleted(loginName);
        if (user == null) {
            throw new UsernameNotFoundException(loginName);
        }
        return new MongoUserDetails(user);
    }

}
