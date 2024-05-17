package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.ExternTransactionRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExternTransactionsServiceTest {

    @InjectMocks
    ExternTransactionsService externTransactionsService;

    @Mock
    ExternTransactionRepository externTransactionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    @Test
    void saveTransactionInTest() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(Integer.valueOf(USER_ID));
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('+');
        when(userRepository.findById(Integer.valueOf(USER_ID))).thenReturn(Optional.of(mockUser));

        externTransactionsService.saveTransaction(Integer.valueOf(USER_ID), postExternTransaction);

        verify(userRepository, Mockito.times(1)).increaseAccountBalance(Integer.valueOf(USER_ID), 100.00d);
        verify(externTransactionRepository, Mockito.times(1)).save(any(ExternTransaction.class));
    }

    @Test
    void saveTransactionOutTest() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(Integer.valueOf(USER_ID));
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('-');
        when(userRepository.findById(Integer.valueOf(USER_ID))).thenReturn(Optional.of(mockUser));

        externTransactionsService.saveTransaction(Integer.valueOf(USER_ID), postExternTransaction);

        verify(userRepository, Mockito.times(1)).decreaseAccountBalance(Integer.valueOf(USER_ID), 105.0d);
        verify(externTransactionRepository, Mockito.times(1)).save(any(ExternTransaction.class));
    }

    @Test
    void saveTransactionTestKo() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(Integer.valueOf(USER_ID));
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('-');
        when(userRepository.findById(Integer.valueOf(USER_ID))).thenReturn(Optional.of(mockUser));

        doThrow(new RuntimeException("Database error")).when(userRepository).decreaseAccountBalance(anyInt(), any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            externTransactionsService.saveTransaction(Integer.valueOf(USER_ID), postExternTransaction);
        });

        assertEquals("Database error", thrown.getMessage());
    }
}