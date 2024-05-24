package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.ExternTransactionDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTransactionInTest() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(USER_ID);
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('+');
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        externTransactionsService.saveTransaction(USER_ID, postExternTransaction);

        verify(userRepository, Mockito.times(1)).increaseAccountBalance(USER_ID, 100.00d);
        verify(externTransactionRepository, Mockito.times(1)).save(any(ExternTransaction.class));
    }

    @Test
    void saveTransactionOutTest() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(USER_ID);
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('-');
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        externTransactionsService.saveTransaction(USER_ID, postExternTransaction);

        verify(userRepository, Mockito.times(1)).decreaseAccountBalance(USER_ID, 100.0);
        verify(externTransactionRepository, Mockito.times(1)).save(any(ExternTransaction.class));
    }

    @Test
    void saveTransactionTestKo() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(USER_ID);
        mockUser.setAccountBalance(200.0);
        PostExternTransaction postExternTransaction = new PostExternTransaction();
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(100.0);
        postExternTransaction.setType('-');
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        doThrow(new RuntimeException("Database error")).when(userRepository).decreaseAccountBalance(anyInt(), any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            externTransactionsService.saveTransaction(USER_ID, postExternTransaction);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void getExternTransactionsByIdOk() {
        User mockUser = new User();
        mockUser.setId(USER_ID);
        ExternTransaction externTransaction = new ExternTransaction();
        externTransaction.setType('+');
        externTransaction.setAccount("account");
        externTransaction.setAmount(100.0);
        externTransaction.setDate(LocalDate.now());
        when(externTransactionRepository.findAllByIdUserOrderByIdDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(externTransaction)));

        Page<ExternTransactionDto> result = externTransactionsService.getExternTransactionsById(0, mockUser);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(externTransactionRepository, times(1)).findAllByIdUserOrderByIdDesc(any(User.class), any(Pageable.class));
    }

    @Test
    void getExternTransactionsByIdEmpty() {
        User mockUser = new User();
        mockUser.setId(USER_ID);
        when(externTransactionRepository.findAllByIdUserOrderByIdDesc(any(User.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<ExternTransactionDto> result = externTransactionsService.getExternTransactionsById(0, mockUser);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(externTransactionRepository, times(1)).findAllByIdUserOrderByIdDesc(any(User.class), any(Pageable.class));
    }
}
