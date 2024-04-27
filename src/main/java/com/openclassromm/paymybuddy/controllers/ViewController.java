package com.openclassromm.paymybuddy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/account")
    public String account() {
        return "account";
    }

    @RequestMapping("/signup")
    public String signup() {
        return "signup";
    }
}
