package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.services.FriendshipService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/friendships")
public class FriendshipController {
    private final Logger LOGGER = LogManager.getLogger(FriendshipController.class);
    @Autowired
    FriendshipService friendshipService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String createFriendship(@ModelAttribute("userEmail") String userEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (UserDetails) authentication.getPrincipal();
        try {
            friendshipService.createFriendship(Integer.valueOf(user.getUsername()), userEmail);
            return "redirect:/account";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return "redirect:/account?error";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Pair<Integer, String>> getFriendsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (UserDetails) authentication.getPrincipal();
        try {
            return friendshipService.getFriends(Integer.valueOf(user.getUsername()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
