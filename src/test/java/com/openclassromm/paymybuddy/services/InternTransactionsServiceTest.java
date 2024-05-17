package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.db.models.InternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.InternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InternTransactionsServiceTest {

    @InjectMocks
    InternTransactionsService internTransactionsService;

    @Mock
    InternTransactionRepository internTransactionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    @Test
    void saveTransactionTest() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(Integer.valueOf(USER_ID));
        mockUser.setAccountBalance(200.0);
        PostInternTransaction postInternTransaction = new PostInternTransaction();
        postInternTransaction.setFriend(1);
        postInternTransaction.setAmount(Double.valueOf(100.00));
        when(userRepository.findById(Integer.valueOf(USER_ID))).thenReturn(Optional.of(mockUser));

        internTransactionsService.saveTransaction(Integer.valueOf(USER_ID), postInternTransaction);

        verify(userRepository, Mockito.times(1)).decreaseAccountBalance(Integer.valueOf(USER_ID), 105.00);
        verify(userRepository, Mockito.times(1)).increaseAccountBalance(postInternTransaction.getFriend(), 100.00);
        verify(internTransactionRepository, Mockito.times(1)).save(any(InternTransaction.class));
    }

    @Test
    void saveTransactionTestKo() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(Integer.valueOf(USER_ID));
        mockUser.setAccountBalance(200.0);
        PostInternTransaction postInternTransaction = new PostInternTransaction();
        postInternTransaction.setFriend(1);
        postInternTransaction.setAmount(Double.valueOf(100.00));
        when(userRepository.findById(Integer.valueOf(USER_ID))).thenReturn(Optional.of(mockUser));

        doThrow(new RuntimeException("Database error")).when(userRepository).decreaseAccountBalance(Integer.valueOf(USER_ID), postInternTransaction.getAmount() + 5);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            internTransactionsService.saveTransaction(Integer.valueOf(USER_ID), postInternTransaction);
        });

        assertEquals("Database error", thrown.getMessage());
        verify(internTransactionRepository, times(1)).save(any());
    }
}