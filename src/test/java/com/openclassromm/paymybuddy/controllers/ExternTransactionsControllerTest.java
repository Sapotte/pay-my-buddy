package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.ExternTransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExternTransactionsControllerTest {

    @InjectMocks
    ExternTransactionsController controller;
    @Mock
    ExternTransactionsService service;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    private final PostExternTransaction postExternTransaction = new PostExternTransaction();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postExternTransaction.setAccount("account");
        postExternTransaction.setAmount(22.2F);
        postExternTransaction.setType('+');
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void postExternTRansactionOk() throws NotAllowed {
        Mockito.when(authentication.getName()).thenReturn(USER_ID);

        var result = controller.postExternTransaction(postExternTransaction);

        verify(service, times(1)).saveTransaction(1, postExternTransaction);
        assertEquals("redirect:/account", result);
    }

    @Test
    void postExternTRansactionKo() throws NotAllowed {
        Mockito.when(authentication.getName()).thenReturn(USER_ID);
        Mockito.doThrow(new NotAllowed("Not enough money in your account")).when(service).saveTransaction(any(), any());

        var result = controller.postExternTransaction(postExternTransaction);

        assertEquals("redirect:/account?notEnough", result);
        verify(service, times(1)).saveTransaction(1, postExternTransaction);
    }

    @Test
    void postExternTransactionWhenExceptionThrown() throws NotAllowed {
        Mockito.when(authentication.getName()).thenReturn(USER_ID);
        Mockito.doThrow(new RuntimeException()).when(service).saveTransaction(any(), any());

        var result = controller.postExternTransaction(postExternTransaction);

        assertEquals("redirect:/account?errorUnknown", result);
        verify(service, times(1)).saveTransaction(1, postExternTransaction);
    }
}
