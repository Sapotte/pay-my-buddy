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
            friendship.setIdFriend(friend.get().getId());
            friendship.setStatus("A");
            friendshipRepository.save(friendship);
        } else {
            throw new IllegalArgumentException("Invalid friend");
        }

    }

    public List<Pair<Integer, String>> getFriends(Integer userId) {
        var friends = friendshipRepository.findFriendshipsByUserId(userId);
        if (!friends.isEmpty()) {
            return friends.stream().map(friend -> userId.equals(friend.getIdUser().getId()) && friend.getStatus().equals("A") ?
                            Pair.of(friend.getIdFriend(), userRepository.findUsernameById(friend.getIdFriend())) :
                            Pair.of(friend.getIdUser().getId(), friend.getIdUser().getUsername()))
                    .toList();
        }
        return null;
    }
}
