package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
public class ViewController {
    private final Logger LOGGER = LogManager.getLogger(ViewController.class);
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        LOGGER.info("Sign up");
        model.addAttribute("postUser", new PostUser());
        return "signup";
    }

    @GetMapping(path = "/account")
    public String account(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userId));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
        }
        return "account";
    }


    @RequestMapping("/addFriend")
    public String addFriend() {
        LOGGER.info("addFriend");
        return "addFriend";
    }
}
