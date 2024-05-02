package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @InjectMocks
    UsersService usersService;
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void createUserOk() {
        PostUser postUser = new PostUser();
        postUser.setPassword("password");
        postUser.setUserName("userName");
        postUser.setEmail("email");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        var response = usersService.createUserAccount(postUser);

        verify(userRepository, times(1)).save(any(User.class));
        assertTrue(response);
    }

    @Test
    void createUserAlreadyExistsKo() {
        PostUser postUser = new PostUser();
        postUser.setPassword("password");
        postUser.setUserName("userName");
        postUser.setEmail("email");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        var response = usersService.createUserAccount(postUser);

        verify(userRepository, times(0)).save(any(User.class));
        assertFalse(response);
    }

    @Test
    void deleteUserOk() throws NotAllowed {
        User user = new User();
        user.setId(Integer.valueOf(USER_ID));
        user.setAccountBalance((float) 0);
        when(userRepository.getReferenceById(any())).thenReturn(user);

        usersService.deleteUser(USER_ID);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void deleteUserKo() throws NotAllowed {
        User user = new User();
        user.setId(Integer.valueOf(USER_ID));
        user.setAccountBalance((float) 25);
        when(userRepository.getReferenceById(any())).thenReturn(user);

        assertThrows(NotAllowed.class, () -> usersService.deleteUser(USER_ID));
        verify(userRepository, times(0)).delete(any(User.class));
    }
}
