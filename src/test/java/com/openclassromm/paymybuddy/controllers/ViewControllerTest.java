package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.ExternTransactionDto;
import com.openclassromm.paymybuddy.controllers.dto.InternTransactionsDto;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.services.ExternTransactionsService;
import com.openclassromm.paymybuddy.services.FriendshipService;
import com.openclassromm.paymybuddy.services.InternTransactionsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.openclassromm.paymybuddy.Constants.User.USER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    @InjectMocks
    private ViewController viewController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private ExternTransactionsService externTransactionsService;

    @MockBean
    private InternTransactionsService internTransactionsService;

    private void setupSecurityContext() {
        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("user", "password", grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void login_ShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void signup_ShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    void externTransaction_ShouldReturnCorrectView() throws Exception {
        setupSecurityContext();
        mockMvc.perform(get("/externTransactions"))
                .andExpect(status().isOk())
                .andExpect(view().name("externTransactions"));
    }

    @Test
    void account_ShouldReturnCorrectView() throws Exception {
        Page<InternTransactionsDto> pageInternTransaction = new PageImpl<>(List.of(new InternTransactionsDto()));
        Page<ExternTransactionDto> pageExternTransaction = new PageImpl<>(List.of(new ExternTransactionDto()));

        setupSecurityContext();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USER_ID.toString());
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(new User()));
        when(externTransactionsService.getExternTransactionsById(anyInt(), any(User.class))).thenReturn(new PageImpl<ExternTransactionDto>(new ArrayList<>())); //Return empty Page object
        when(internTransactionsService.getInternTransactions(anyInt(), any(User.class))).thenReturn(new PageImpl<InternTransactionsDto>(new ArrayList<>())); //Return empty Page object
        when(friendshipService.getFriends(anyInt())).thenReturn(new ArrayList<>());

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(externTransactionsService.getExternTransactionsById(anyInt(), any())).thenReturn(pageExternTransaction);
        when(internTransactionsService.getInternTransactions(anyInt(), any())).thenReturn(pageInternTransaction);
        mockMvc.perform(get("/account")
                        .param("externTransactionsPage", "1").param("internTransactionsPage", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"));
    }

    @Test
    void addFriend_ShouldReturnCorrectView() throws Exception {
        setupSecurityContext();
        mockMvc.perform(get("/addFriend"))
                .andExpect(status().isOk())
                .andExpect(view().name("addFriend"));
    }
}
