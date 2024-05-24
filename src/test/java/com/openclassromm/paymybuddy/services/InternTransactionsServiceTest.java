package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.InternTransactionsDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;
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
        mockUser.setId(USER_ID);
        mockUser.setAccountBalance(200.0);
        PostInternTransaction postInternTransaction = new PostInternTransaction();
        postInternTransaction.setFriend(1);
        postInternTransaction.setAmount(100.00);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        internTransactionsService.saveTransaction(USER_ID, postInternTransaction);
        verify(internTransactionRepository, times(0)).findAllByIdUserOrIdFriendOrderByIdDesc(any(), any());

        verify(userRepository, Mockito.times(1)).decreaseAccountBalance(USER_ID, 100.50);
        verify(userRepository, Mockito.times(1)).increaseAccountBalance(postInternTransaction.getFriend(), 100.00);
        verify(internTransactionRepository, Mockito.times(1)).save(any(InternTransaction.class));
    }

    @Test
    void saveTransactionTestKo() throws NotAllowed {

        User mockUser = new User();
        mockUser.setId(USER_ID);
        mockUser.setAccountBalance(200.0);
        PostInternTransaction postInternTransaction = new PostInternTransaction();
        postInternTransaction.setFriend(1);
        postInternTransaction.setAmount(Double.valueOf(100.00));
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mockUser));

        doThrow(new RuntimeException("Database error")).when(userRepository).decreaseAccountBalance(USER_ID, postInternTransaction.getAmount() + 0.5);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            internTransactionsService.saveTransaction(USER_ID, postInternTransaction);
        });

        assertEquals("Database error", thrown.getMessage());
        verify(internTransactionRepository, times(1)).save(any());
        verify(internTransactionRepository, times(0)).findAllByIdUserOrIdFriendOrderByIdDesc(any(), any());
    }

    @Test
    void getInternOutTransactionsTest() {
        User mockUser = new User();
        mockUser.setId(USER_ID);
        InternTransaction mockTransaction = new InternTransaction();
        mockTransaction.setIdUser(mockUser);
        mockTransaction.setAmount(100.00);
        mockTransaction.setIsCompleted(true);
        mockTransaction.setDate(LocalDate.now());
        mockTransaction.setLabel("label");
        mockTransaction.setTaxe(0.5);

        when(userRepository.findUsernameById(any())).thenReturn("friend");

        Page<InternTransaction> page = new PageImpl<>(List.of(mockTransaction));
        when(internTransactionRepository.findAllByIdUserOrIdFriendOrderByIdDesc(any(), any())).thenReturn(page);

        Page<InternTransactionsDto> result = internTransactionsService.getInternTransactions(0, mockUser);
        InternTransactionsDto resultDto = result.getContent().get(0);

        verify(internTransactionRepository, times(1)).findAllByIdUserOrIdFriendOrderByIdDesc(any(), any());
        assertEquals(mockTransaction.getAmount() + mockTransaction.getTaxe(), resultDto.getAmount());
        assertEquals(mockTransaction.getIsCompleted() ? "PASSED" : "CANCELED", resultDto.getStatus());
    }
}
