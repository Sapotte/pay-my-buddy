package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.InternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class InternTransactionsServiceTest {

    @Test
    void saveTransactionTest() throws NotAllowed {
        // given
        Integer userId = 1;
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        InternTransactionRepository mockTransactionRepository = Mockito.mock(InternTransactionRepository.class);
        UsersService mockUsersService = Mockito.mock(UsersService.class);
        InternTransactionsService internTransactionsService = new InternTransactionsService();
        internTransactionsService.userRepository = mockUserRepository;
        internTransactionsService.internTransactionRepository = mockTransactionRepository;
        internTransactionsService.usersService = mockUsersService;

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setAccountBalance(100.0F);
        PostInternTransaction mockTransaction = Mockito.mock(PostInternTransaction.class);
        Mockito.when(mockTransaction.getFriend()).thenReturn(2);
        Mockito.when(mockTransaction.getAmount()).thenReturn(50f);

        Mockito.when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        internTransactionsService.saveTransaction(userId, mockTransaction);

        // then
        Mockito.verify(mockUserRepository, Mockito.times(1)).decreaseAccountBalance(userId, mockTransaction.getAmount());
        Mockito.verify(mockUserRepository, Mockito.times(1)).increaseAccountBalance(mockTransaction.getFriend(), mockTransaction.getAmount());
    }
}