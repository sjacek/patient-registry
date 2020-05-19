package com.grinnotech.patientsorig.config.security;

import com.grinnotech.patients.mongodb.model.User;
import com.grinnotech.patients.mongodb.dao.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

	public MongoUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
    public UserDetails loadUserByUsername(String loginName)
            throws UsernameNotFoundException {
        Optional<User> oUser = userRepository.findByEmailActive(loginName);
        User user = oUser.orElseThrow(() -> new UsernameNotFoundException(loginName));

        return new MongoUserDetails(user);
    }

}
