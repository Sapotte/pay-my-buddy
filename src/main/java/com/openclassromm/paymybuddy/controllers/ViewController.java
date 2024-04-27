package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
    private final Logger LOGGER = LogManager.getLogger(ViewController.class);

    @RequestMapping("/login")
    public String login() {
        LOGGER.info("Login");
        return "login";
    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        LOGGER.info("Sign up");
        model.addAttribute("postUser", new PostUser());
        return "signup";
    }

    @RequestMapping("/account")
    public String account() {
        LOGGER.info("Account");
        return "account";
    }
}
