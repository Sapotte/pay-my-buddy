package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.services.FriendshipService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedList;

import static com.openclassromm.paymybuddy.Constants.User.USER_EMAIL;
import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FriendshipControllerTest {
    @InjectMocks
    FriendshipController controller;

    @Mock
    FriendshipService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID.toString());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createFriendshipOk() {

        var result = controller.createFriendship(USER_EMAIL);

        verify(service, times(1)).createFriendship(1, USER_EMAIL);
        assertEquals("redirect:/account", result);
    }

    @Test
    void createFriendshipKo() {
        Mockito.doThrow(new IllegalArgumentException("Invalid friend")).when(service).createFriendship(any(), any());

        var result = controller.createFriendship(USER_EMAIL);

        assertEquals("redirect:/account?error", result);
        verify(service, times(1)).createFriendship(1, USER_EMAIL);
    }

    @Test
    void getFriendsByUserIdOk() {
        var friendList = new LinkedList<Pair<Integer, String>>();
        friendList.add(Pair.of(1, "user1@ops.com"));
        friendList.add(Pair.of(2, "user2@ops.com"));

        when(service.getFriends(USER_ID)).thenReturn(friendList);

        var result = controller.getFriendsByUserId();

        assertEquals(friendList, result);
        verify(service, times(1)).getFriends(USER_ID);
    }

    @Test
    void getFriendsByUserIdKo() {
        when(service.getFriends(USER_ID)).thenThrow(new RuntimeException("Unexpected Error"));

        var result = controller.getFriendsByUserId();

        assertNull(result);
        verify(service, times(1)).getFriends(USER_ID);
    }
}
