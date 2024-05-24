package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static com.openclassromm.paymybuddy.Constants.User.USER_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void whenLoadUserByUsername_thenReturnUserDetails() {
        User userMock = new User();
        userMock.setId(1);
        userMock.setPassword("password");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(userMock));

        UserDetails result = customUserDetailsService.loadUserByUsername("test@test.com");

        assertNotNull(result);
        assertEquals(USER_ID.toString(), result.getUsername());
        assertEquals(USER_PASSWORD, result.getPassword());
    }

    @Test
    public void whenLoadUserByUsername_noSuchUsername_thenThrowsUsernameNotFoundException() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("test@test.com");
        });
    }

}