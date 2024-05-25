package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.services.FriendshipService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Creates a new friendship between the currently authenticated user and the specified user.
     *
     * @param userEmail the email of the user to befriend
     * @return a string representing the redirection URL
     * @return a string representing the redirection URL. If the friendship is created successfully, the user will be redirected to "/account". If an error occurs, the user will be
     * redirected to "/account?error".
     * @throws Exception if an error occurs while creating the friendship
     * @PreAuthorize("isAuthenticated()")
     * @PostMapping
     * @ModelAttribute("userEmail") String userEmail the email of the user to befriend
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String createFriendship(@ModelAttribute("userEmail") String userEmail) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            friendshipService.createFriendship(Integer.valueOf(auth.getName()), userEmail);
            return "redirect:/account";
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return "redirect:/addFriend?error";
        }
    }

    /**
     * Retrieves the friends of the user identified by the user ID.
     *
     * This method requires the user to be authenticated.
     *
     * @return a list of pairs where each pair consists of an integer representing the friend's ID and
     *         a string representing the friend's username
     * @throws NullPointerException if the user ID is null
     * @throws NumberFormatException if the user ID is not a valid number format
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Pair<Integer, String>> getFriendsByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            return friendshipService.getFriends(Integer.valueOf(auth.getName()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
