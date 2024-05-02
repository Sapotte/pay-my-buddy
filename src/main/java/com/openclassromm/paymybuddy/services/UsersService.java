package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.services.mappers.UserServiceMapperImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    private final Logger LOGGER = LogManager.getLogger(UsersService.class);

    UserServiceMapperImpl userServiceMapper = new UserServiceMapperImpl();
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Boolean createUserAccount(PostUser postUser) {
        LOGGER.info("Check if account already exists");
        if(checkIfEmailExists(postUser.getEmail())) {
            LOGGER.info("Account already exists");
            return false;
        } else {
            LOGGER.info("Creating user account");
            // Encode password
            postUser.setPassword(passwordEncoder.encode(postUser.getPassword()));
            User user =  userServiceMapper.mapPostUserToUser(postUser, (float) 0);
            userRepository.save(user);
            return true;
        }
    }

    /** Method to check if an account has already been created with this email
     * @param email, id user
     * @return true if account is already registered
     */
    private boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
