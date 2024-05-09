package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.InternTransactionsDto;
import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.db.models.InternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.InternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.mappers.InternTransactionsServiceMapperImpl;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class InternTransactionsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    InternTransactionRepository internTransactionRepository;
    @Autowired
    UsersService usersService;
    InternTransactionsServiceMapperImpl mapper = new InternTransactionsServiceMapperImpl();
    private final Logger LOGGER = LogManager.getLogger(InternTransactionsService.class);

    @Transactional
    public void saveTransaction(Integer userId, PostInternTransaction postInternTransaction) throws NotAllowed {
        User user = userRepository.findById(userId).orElse(null);
        usersService.checkIfAccountCanBeWithdraw(user.getAccountBalance(), postInternTransaction.getAmount());
        Boolean passed;
        try {
            userRepository.decreaseAccountBalance(userId, postInternTransaction.getAmount());
            LOGGER.info("Account balance user decreased");
            userRepository.increaseAccountBalance(postInternTransaction.getFriend(), postInternTransaction.getAmount());
            LOGGER.info("Account balance user increased");
            internTransactionRepository.save(mapper.map(user, postInternTransaction, new Date(), 5 / 100 * postInternTransaction.getAmount(), "OK"));
            LOGGER.info("Transaction added");
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            internTransactionRepository.save(mapper.map(user, postInternTransaction, new Date(), 5 / 100 * postInternTransaction.getAmount(), "ERR"));
            LOGGER.info("Transaction added");
            throw e;
        }

    }

    @PreAuthorize("isAuthenticated()")
    public Page<InternTransactionsDto> getInternTransactions(Integer page, User user) {
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, 5);
        Page<InternTransaction> internTransactionList = internTransactionRepository.findAllByIdUserOrIdFriendOrderByIdDesc(user.getId(), pageable);
        return internTransactionList.map(it -> {
            InternTransactionsDto dto = new InternTransactionsDto();
            if (it.getIdUser().getId().equals(user.getId())) {
                dto.setFriend(userRepository.findUsernameById(it.getIdFriend()));
                dto.setType("-");
            } else {
                dto.setFriend(it.getIdUser().getUsername());
                dto.setType("+");
            }
            dto.setStatus(it.getStatus().equals("OK") ? "PASSED" : "CANCEL");
            dto.setDate(LocalDate.parse(it.getDate().format(DateTimeFormatter.ISO_DATE)));
            dto.setAmount(it.getAmount().floatValue());
            dto.setLabel(it.getLabel());
            return dto;
        });
    }
}
