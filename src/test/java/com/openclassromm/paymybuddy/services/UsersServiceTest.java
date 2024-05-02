package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

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
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        var response = usersService.createUserAccount(postUser);

        verify(userRepository, times(0)).save(any(User.class));
        assertFalse(response);
    }
}
