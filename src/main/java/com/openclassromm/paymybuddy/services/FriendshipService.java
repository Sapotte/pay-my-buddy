package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.db.models.Friendship;
import com.openclassromm.paymybuddy.db.repositories.FriendshipRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {
    @Autowired
    FriendshipRepository friendshipRepository;
    @Autowired
    UserRepository userRepository;

    @PreAuthorize("isFullyAuthenticated()")
    public void createFriendship(Integer fromUser, String toUser) {
        var user = userRepository.findById(fromUser).orElseThrow();
        var friend = userRepository.findByEmail(toUser);
        if (friend.isPresent()) {
            Friendship friendship = new Friendship();
            friendship.setIdUser(user);
            friendship.setId2User(friend.get());
            friendship.setStatus("A");
            friendshipRepository.save(friendship);
        } else {
            throw new IllegalArgumentException("Invalid user or friend");
        }

    }
}
