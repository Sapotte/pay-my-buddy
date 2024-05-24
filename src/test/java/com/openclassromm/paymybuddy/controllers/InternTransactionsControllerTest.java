package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.InternTransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InternTransactionsControllerTest {
    @InjectMocks
    InternTransactionsController controller;

    @Mock
    InternTransactionsService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private final PostInternTransaction postInternTransaction = new PostInternTransaction();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postInternTransaction.setFriend(2);
        postInternTransaction.setAmount(Double.valueOf(22.2));
        postInternTransaction.setLabel("label");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID.toString());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void postInternTransactionOk() throws NotAllowed {

        Mockito.when(authentication.getName()).thenReturn(USER_ID.toString());

        var result = controller.postInternTransaction(postInternTransaction);

        verify(service, times(1)).saveTransaction(1, postInternTransaction);
        assertEquals("redirect:/account", result);
    }

    @Test
    void postInternTransactionNotAllowedException() throws NotAllowed {
        Mockito.when(authentication.getName()).thenReturn(USER_ID.toString());
        Mockito.doThrow(new NotAllowed("Not enough money in your account")).when(service).saveTransaction(any(Integer.class), any(PostInternTransaction.class));

        var result = controller.postInternTransaction(postInternTransaction);

        verify(service, times(1)).saveTransaction(1, postInternTransaction);
        assertEquals("redirect:/account?notEnough", result);
    }

    @Test
    void postInternTransactionUnknownErrorException() throws NotAllowed {
        Mockito.when(authentication.getName()).thenReturn(USER_ID.toString());
        Mockito.doThrow(new RuntimeException()).when(service).saveTransaction(any(Integer.class), any(PostInternTransaction.class));

        var result = controller.postInternTransaction(postInternTransaction);

        verify(service, times(1)).saveTransaction(1, postInternTransaction);
        assertEquals("redirect:/account?errorUnknown", result);
    }
}
