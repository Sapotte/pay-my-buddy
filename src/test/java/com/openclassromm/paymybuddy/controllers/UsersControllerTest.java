package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.errors.AlreadyExistant;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static com.openclassromm.paymybuddy.Constants.User.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {
    @InjectMocks
    UsersController usersController;
    @Mock
    UsersService usersService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @Test
    void createUserOk() throws AlreadyExistant {
        PostUser user = new PostUser();

        var response = usersController.addPerson(user);

        verify(usersService, times(1)).createUserAccount(user);
        assertEquals("redirect:/login?success", response);
    }

    @Test
    void createUserAlreadyExistsKo() throws AlreadyExistant {
        PostUser user = new PostUser();
        doThrow(AlreadyExistant.class).when(usersService).createUserAccount(user);

        var response = usersController.addPerson(user);

        verify(usersService, times(1)).createUserAccount(user);
        assertEquals("redirect:/login?alreadyExisted", response);
    }

    @Test
    void createUsersKo() throws AlreadyExistant {
        PostUser user = new PostUser();
        doThrow(RuntimeException.class).when(usersService).createUserAccount(user);

        var response = usersController.addPerson(user);

        verify(usersService, times(1)).createUserAccount(user);
        assertEquals("redirect:/login?errorDatabase", response);
    }

    @Test
    @WithMockUser(username = USER_ID)
    void shouldDeleteUserAndRedirectLogin() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID);
        SecurityContextHolder.setContext(securityContext);

        String response = usersController.deleteUser();

        assertEquals("redirect:/login?userDeleted", response);
    }

    @Test
    @WithMockUser(username = USER_ID)
    void shouldReturnAccountNotEmptyWhenNotAllowedExceptionOccurs() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID);
        SecurityContextHolder.setContext(securityContext);

        doThrow(NotAllowed.class).when(usersService).deleteUser(USER_ID);

        String response = usersController.deleteUser();

        assertEquals("redirect:/account?accountNotEmpty", response);
    }

    @Test
    @WithMockUser(username = USER_ID)
    void shouldReturnErrorDeletedWhenExceptionOccurs() throws Exception {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID);
        SecurityContextHolder.setContext(securityContext);

        doThrow(new RuntimeException()).when(usersService).deleteUser(USER_ID);

        String response = usersController.deleteUser();

        assertEquals("redirect:/account?errorDeleted", response);
    }

    @Test
    @WithMockUser(username = USER_ID)
    void updateUserOk() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID);
        SecurityContextHolder.setContext(securityContext);

        PostUser user = new PostUser();
        user.setUsername(USER_NAME);
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);

        usersController.updateUser(user);

        verify(usersService, times(1)).updateUser(Integer.valueOf(USER_ID), USER_NAME, USER_PASSWORD);
    }
}
