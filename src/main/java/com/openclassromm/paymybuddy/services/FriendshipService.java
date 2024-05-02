package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.db.models.Friendship;
import com.openclassromm.paymybuddy.db.repositories.FriendshipRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Pair<String, String>> getFriends(Integer userId) {
        var friends = friendshipRepository.findFriendshipsByUserId(userId);
        if (!friends.isEmpty()) {
            return friends.stream().map(friend -> userId.equals(friend.getIdUser().getId()) ?
                            Pair.of(friend.getIdUser().getUserName(), friend.getIdUser().getEmail()) :
                            Pair.of(friend.getId2User().getUserName(), friend.getId2User().getEmail()))
                    .toList();
        }
        return null;
    }
}
