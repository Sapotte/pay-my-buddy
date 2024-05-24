package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.InternTransactionsDto;
import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.db.models.InternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.InternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.mappers.InternTransactionsServiceMapperImpl;
import com.openclassromm.paymybuddy.utils.Helpers;
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
    @Autowired
    Helpers helpers;
    InternTransactionsServiceMapperImpl mapper = new InternTransactionsServiceMapperImpl();
    private final Logger LOGGER = LogManager.getLogger(InternTransactionsService.class);

    /**
     * Saves a transaction for a user.
     *
     * @param userId                the ID of the user
     * @param postInternTransaction the transaction details
     * @throws NotAllowed if the user's account balance is insufficient for the transaction amount
     */
    @Transactional
    public void saveTransaction(Integer userId, PostInternTransaction postInternTransaction) throws NotAllowed {
        Double taxe = Helpers.round(postInternTransaction.getAmount() * 0.5 / 100);

        User user = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        usersService.checkIfAccountCanBeWithdraw(user.getAccountBalance(), postInternTransaction.getAmount() + taxe);
        try {
            userRepository.decreaseAccountBalance(userId, postInternTransaction.getAmount() + taxe);
            LOGGER.info("Account balance user decreased");
            userRepository.increaseAccountBalance(postInternTransaction.getFriend(), postInternTransaction.getAmount());
            LOGGER.info("Account balance user increased");
            internTransactionRepository.save(mapper.map(user, postInternTransaction, new Date(), taxe, true));
            LOGGER.info("Transaction added");
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            internTransactionRepository.save(mapper.map(user, postInternTransaction, new Date(), taxe, false));
            LOGGER.info("Transaction added");
            throw e;
        }

    }

    /**
     * Retrieves a page of InternTransactionsDto objects for a given page number and user.
     *
     * @param page the page number to retrieve (starting from 0)
     * @param user the user for whom to retrieve the transactions
     * @return a Page of InternTransactionsDto objects
     */
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
                dto.setFriend(userRepository.findUsernameById(it.getIdFriend()).describeConstable().orElseThrow());
                dto.setType("-");
                dto.setAmount((it.getAmount() + it.getTaxe()));
            } else {
                dto.setFriend(it.getIdUser().getUsername());
                dto.setType("+");
                dto.setAmount(it.getAmount());
            }
            dto.setStatus(it.getIsCompleted() ? "PASSED" : "CANCELED");
            dto.setDate(LocalDate.parse(it.getDate().format(DateTimeFormatter.ISO_DATE)));
            dto.setLabel(it.getLabel());
            return dto;
        });
    }
}
