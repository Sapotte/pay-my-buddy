package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostUser;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.ExternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.FriendshipRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.AlreadyExistant;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @InjectMocks
    UsersService usersService;
    @Mock
    UserRepository userRepository;
    @Mock
    FriendshipRepository friendshipRepository;
    @Mock
    ExternTransactionRepository externTransactionRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void createUserOk() throws AlreadyExistant {
        PostUser postUser = new PostUser();
        postUser.setPassword("password");
        postUser.setUsername("userName");
        postUser.setEmail("email");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        usersService.createUserAccount(postUser);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserAlreadyExistsKo() {
        PostUser postUser = new PostUser();
        postUser.setPassword("password");
        postUser.setUsername("userName");
        postUser.setEmail("email");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(AlreadyExistant.class, () -> usersService.createUserAccount(postUser));
    }

    @Test
    void createUserRunTimeExceptionKo() {
        PostUser postUser = new PostUser();
        postUser.setPassword("password");
        postUser.setUsername("userName");
        postUser.setEmail("email");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        doThrow(RuntimeException.class).when(userRepository).save(any(User.class));

        assertThrows(RuntimeException.class, () -> usersService.createUserAccount(postUser));
    }

    @Test
    void deleteUserOk() throws NotAllowed {
        User user = new User();
        user.setId(USER_ID);
        user.setAccountBalance(0.0);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        usersService.deleteUser(USER_ID.toString());

        verify(friendshipRepository, times(1)).updateFriendshipStatus(USER_ID);
        verify(externTransactionRepository, times(1)).deleteByIdUser(USER_ID);
        verify(userRepository, times(1)).disabledUser(USER_ID, "deletedUser", "0");
    }

    @Test
    void deleteUserKo() throws NotAllowed {
        User user = new User();
        user.setId(USER_ID);
        user.setAccountBalance(25.00);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        assertThrows(NotAllowed.class, () -> usersService.deleteUser(USER_ID.toString()));
        verify(friendshipRepository, times(0)).updateFriendshipStatus(anyInt());
        verify(externTransactionRepository, times(0)).deleteByIdUser(anyInt());
        verify(userRepository, times(0)).disabledUser(anyInt(), anyString(), anyString());
    }

    @Test
    void updateUserOk() {
        usersService.updateUser(USER_ID, USER_NAME, USER_PASSWORD);
        verify(userRepository, times(1)).updateUser(any(), any(), any());
    }
}
