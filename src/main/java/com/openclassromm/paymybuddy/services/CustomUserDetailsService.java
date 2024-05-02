package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LogManager.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user  = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            LOGGER.info("Found user: " + user);
            return new org.springframework.security.core.userdetails.User(user.get().getId().toString(), user.get().getPassword(), new ArrayList<>());
        }
    }
}
