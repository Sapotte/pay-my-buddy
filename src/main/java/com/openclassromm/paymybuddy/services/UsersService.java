package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.ExternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.FriendshipRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.AlreadyExistant;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.mappers.UserServiceMapperImpl;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersService {
    private final Logger LOGGER = LogManager.getLogger(UsersService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    UserServiceMapperImpl userServiceMapper = new UserServiceMapperImpl();
    @Autowired
    private ExternTransactionRepository externTransactionRepository;

    /**
     * Creates a user account with the given information.
     *
     * @param postUser The user information to create the account.
     * @throws AlreadyExistant If an account with the same email already exists.
     */
    public void createUserAccount(PostUser postUser) throws AlreadyExistant {
        LOGGER.info("Check if account already exists");
        if(checkIfEmailExists(postUser.getEmail())) {
            throw new AlreadyExistant("Account already exists");
        } else {
            LOGGER.info("Creating user account");
            // Encode password
            postUser.setPassword(passwordEncoder.encode(postUser.getPassword()));
            User user = userServiceMapper.mapPostUserToUser(postUser, 0.00);
            try {
                userRepository.save(user);
            } catch (Exception e) {
                LOGGER.error("Error occurred while saving user: " + e.getMessage());
                throw new RuntimeException("Error occurred while saving user", e);
            }
        }
    }

    /**
     * Deletes the user with the given username.
     *
     * @param username The username of the user to delete.
     * @throws NotAllowed       if the user's account balance is not zero.
     * @throws RuntimeException if an error occurs while deleting the user.
     */
    @Transactional
    public void deleteUser(String username) throws NotAllowed {
        User user = userRepository.findById(Integer.valueOf(username)).orElseThrow(NullPointerException::new);
        if (user.getAccountBalance() != 0) {
            throw new NotAllowed("Account not null");
        }
        try {

            // Set all the deleted user's friendship at not active
            friendshipRepository.updateFriendshipStatus(user.getId());
            externTransactionRepository.deleteByIdUser(user.getId());
            userRepository.disabledUser(user.getId(), "deletedUser", UUID.randomUUID().toString());
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting user: " + e.getMessage());
            throw new RuntimeException("Error occurred while deleting user", e);
        }

    }

    /**
     * Method to check if an account has already been created with this email
     *
     * @param email, id user
     * @return true if account is already registered
     */
    private boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Updates the user's information with the given id, username, and password.
     *
     * @param id        The id of the user.
     * @param userName  The new username.
     * @param password  The new password.
     */
    public void updateUser(Integer id, String userName, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updateUser(id, userName, password);
    }

    /**
     * Checks if the account balance is sufficient for a withdrawal.
     *
     * @param accountBalance The current balance of the account.
     * @param amount         The amount to withdraw.
     * @throws NotAllowed If the account balance is less than the withdrawal amount.
     */
    public void checkIfAccountCanBeWithdraw(Double accountBalance, Double amount) throws NotAllowed {
        if (accountBalance < amount) {
            throw new NotAllowed("Not enough money in your account");
        }
    }
}
