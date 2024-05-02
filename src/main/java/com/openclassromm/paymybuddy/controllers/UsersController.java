package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        var newUser = usersService.createUserAccount(user);
        if(newUser) {
            return "redirect:/login?success";
        } else {
            return "redirect:/login?alreadyExisted";
        }
    }

    @DeleteMapping
    public String deletePerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            usersService.deleteUser(username);
            return "redirect:/login?userDeleted";
        } catch (Exception e) {
            LOGGER.error(e);
            if (e instanceof NotAllowed) {
                return "redirect:/account?accountNotEmpty";
            }
            return "redirect:/account?errorDeleted";
        }
    }
}
