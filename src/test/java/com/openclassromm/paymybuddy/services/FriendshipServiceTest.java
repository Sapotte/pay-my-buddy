package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.db.models.Friendship;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.FriendshipRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FriendshipServiceTest {
    @InjectMocks
    private FriendshipService friendshipService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendshipRepository friendshipRepository;


    @Test
    public void testCreateFriendship_FriendshipSaved() {
        User user = new User();
        User friend = new User();
        friend.setId(FRIEND_ID);
        Friendship friendship = new Friendship();
        friendship.setIdUser(user);
        friendship.setIdFriend(friend.getId());
        friendship.setIsActive(true);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(FRIEND_EMAIL)).thenReturn(Optional.of(friend));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        friendshipService.createFriendship(USER_ID, FRIEND_EMAIL);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).findByEmail(FRIEND_EMAIL);
        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    public void createFriendshipKo() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(new User()));
        when(userRepository.findByEmail(FRIEND_EMAIL)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            friendshipService.createFriendship(USER_ID, FRIEND_EMAIL);
        });
    }

    @Test
    public void getFriendsOk() {
        User user1 = new User();
        User user2 = new User();
        user1.setId(USER_ID);
        user2.setId(FRIEND_ID);
        Friendship friendship = new Friendship();
        friendship.setIdUser(user1);
        friendship.setIdFriend(user2.getId());
        friendship.setIsActive(true);
        List<Friendship> friendsList = List.of(friendship);
        List<Pair<Integer, String>> expectedList = new ArrayList<>();
        Pair<Integer, String> userPair = Pair.of(user2.getId(), user2.getUsername());
        expectedList.add(userPair);

        when(friendshipRepository.findFriendshipsByUserId(USER_ID)).thenReturn(friendsList);
        when(userRepository.findUsernameById(user2.getId())).thenReturn(user2.getUsername());

        List<Pair<Integer, String>> actualList = friendshipService.getFriends(USER_ID);

        verify(friendshipRepository, times(1)).findFriendshipsByUserId(USER_ID);
        verify(userRepository, times(1)).findUsernameById(user2.getId());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void getFriendNoFriendOk() {
        User user = new User();
        user.setId(USER_ID);
        List<Friendship> friendsList = new ArrayList<>();

        when(friendshipRepository.findFriendshipsByUserId(USER_ID)).thenReturn(friendsList);

        List<Pair<Integer, String>> actualList = friendshipService.getFriends(USER_ID);

        verify(friendshipRepository, times(1)).findFriendshipsByUserId(USER_ID);
        assertNull(actualList);
    }
}
