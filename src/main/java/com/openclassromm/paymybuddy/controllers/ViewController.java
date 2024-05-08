package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.services.ExternTransactionsService;
import com.openclassromm.paymybuddy.services.FriendshipService;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class ViewController {
    private final Logger LOGGER = LogManager.getLogger(ViewController.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendshipService friendshipService;
    @Autowired
    ExternTransactionsService externTransactionsService;

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("title", "Log in");
        return "login";
    }

    @GetMapping(value = "/signup")
    public String signup(Model model) {
        LOGGER.info("Sign up");
        model.addAttribute("postUser", new PostUser());
        model.addAttribute("title", "Sign up");
        return "signup";
    }

    @GetMapping(path = "/account")
    public String account(@Param("externTransactionsPage") @Nullable
                          Integer externTransactionsPage, @Param("internTransactionsPage") @Nullable Integer internTransactionsPage, Model model) {
        Integer userId = null;
        Optional<User> optionalUser = Optional.empty();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = Integer.valueOf(authentication.getName());
            optionalUser = userRepository.findById(userId);
        } catch (Exception e) {
            LOGGER.error(e);
        }

        if (!optionalUser.isEmpty()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
        }
        Page<ExternTransaction> externTransactions = externTransactionsService.getExternTransactionsById(externTransactionsPage, optionalUser.get());
        LOGGER.info(externTransactions.getTotalElements() + externTransactions.getTotalPages());
        externTransactions.stream().forEach(trans -> {
            LOGGER.info(trans.toString());
        });
        model.addAttribute("externTransactions", externTransactions);
        model.addAttribute("title", "Your account");
        return "account";
    }


    @RequestMapping("/addFriend")
    public String addFriend(Model model) {
        model.addAttribute("title", "Add a new friend");
        return "addFriend";
    }

    @RequestMapping(path = "/externTransactions")
    public String externTransaction(Model model) {
        model.addAttribute("postExternTransaction", new PostExternTransaction());
        model.addAttribute("title", "New extern transaction");
        return "externTransactions";
    }
}
