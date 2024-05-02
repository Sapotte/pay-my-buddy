package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {
    @InjectMocks
    UsersController usersController;
    @Mock
    UsersService usersService;

    @Test
    void createUserOk() {
        PostUser user = new PostUser();
        when(usersService.createUserAccount(any())).thenReturn(true);

        var response = usersController.addPerson(user);

        verify(usersService, times(1)).createUserAccount(user);
        assertEquals("redirect:/login?success", response);
    }

    @Test
    void createUserAlreadyExistsKo() {
        PostUser user = new PostUser();
        when(usersService.createUserAccount(any())).thenReturn(false);

        var response = usersController.addPerson(user);

        verify(usersService, times(1)).createUserAccount(user);
        assertEquals("redirect:/login?alreadyExisted", response);
    }
}
