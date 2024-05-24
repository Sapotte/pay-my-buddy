package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.AlreadyExistant;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final Logger LOGGER = LogManager.getLogger(UsersController.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    UsersService usersService;

    /** Create a user's account if the account doesn't exist
     * @param user contains info to creat an account
     * @return responseEntity
     */
    @PostMapping
    public String addPerson(@ModelAttribute("postUser") PostUser user) {
        try {
            usersService.createUserAccount(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            if (e instanceof AlreadyExistant) {
                return "redirect:/login?alreadyExisted";
            } else {
                return "redirect:/login?errorDatabase";
            }
        }


    }

    /**
     * Deletes the authenticated user's account.
     *
     * @return The URL to redirect the user after deleting the account.
     * Possible redirect URLs:
     * - "/login?userDeleted" if the account is successfully deleted.
     * - "/account?accountNotEmpty" if the user's account balance is not zero.
     * - "/account?errorDeleted" if an error occurs during the deletion process.
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public String deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            usersService.deleteUser(username);
            authentication.setAuthenticated(false);
            return "redirect:/login?userDeleted";
        } catch (Exception e) {
            LOGGER.error(e);
            if (e instanceof NotAllowed) {
                return "redirect:/account?accountNotEmpty";
            }
            return "redirect:/account?errorDeleted";
        }
    }

    /**
     * Updates the authenticated user's information.
     *
     * @param user The updated user information.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public void updateUser(@ModelAttribute("putUser") PostUser user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        usersService.updateUser(Integer.valueOf(userId), user.username, user.password);
    }
}
