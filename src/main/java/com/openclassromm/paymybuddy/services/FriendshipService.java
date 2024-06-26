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

    /**
     * Creates a new friendship between the specified user and the friend with the given email.
     *
     * @param fromUser the ID of the user who initiates the friendship
     * @param toUser   the email of the friend to be added
     * @throws IllegalArgumentException if the friend with the given email does not exist
     * @precondition the user must be fully authenticated
     */
    @PreAuthorize("isFullyAuthenticated()")
    public void createFriendship(Integer fromUser, String toUser) {
        var user = userRepository.findById(fromUser).orElseThrow();
        var friend = userRepository.findByEmail(toUser);
        if (friend.isPresent()) {
            Friendship friendship = new Friendship();
            friendship.setIdUser(user);
            friendship.setIdFriend(friend.get().getId());
            friendship.setIsActive(true);
            friendshipRepository.save(friendship);
        } else {
            throw new IllegalArgumentException("Invalid friend");
        }
    }

    /**
     * Retrieves the friends of the user identified by the user ID.
     *
     * @param userId the ID of the user
     * @return a list of pairs where each pair consists of an integer representing the friend's ID and
     * a string representing the friend's username, or null if the user has no friends
     */
    public List<Pair<Integer, String>> getFriends(Integer userId) {
        var friends = friendshipRepository.findFriendshipsByUserId(userId);
        if (!friends.isEmpty()) {
            return friends.stream().map(friend -> userId.equals(friend.getIdUser().getId()) && friend.getIsActive() ?
                            Pair.of(friend.getIdFriend(), userRepository.findUsernameById(friend.getIdFriend())) :
                            Pair.of(friend.getIdUser().getId(), friend.getIdUser().getUsername()))
                    .toList();
        }
        return null;
    }
}
