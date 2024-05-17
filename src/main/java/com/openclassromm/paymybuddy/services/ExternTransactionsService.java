package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.ExternTransactionDto;
import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.ExternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.mappers.ExternTransactionServiceMapperImpl;
import com.openclassromm.paymybuddy.utils.Helpers;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ExternTransactionsService {
    private final Logger LOGGER = LogManager.getLogger(ExternTransactionsService.class);

    @Autowired
    ExternTransactionRepository externTransactionRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UsersService usersService;
    @Autowired
    Helpers helpers;

    private final ExternTransactionServiceMapperImpl mapper = new ExternTransactionServiceMapperImpl();

    @Transactional
    public void saveTransaction(Integer userId, PostExternTransaction postExternTransaction) throws NotAllowed {
        User user = userRepository.findById(userId).orElse(null);
        Double taxe = Helpers.round(postExternTransaction.getAmount() * 0.05);
        try {
            if ('+' == postExternTransaction.getType()) {
                userRepository.increaseAccountBalance(userId, postExternTransaction.getAmount());
                LOGGER.info("Account balance increased");
            } else if ('-' == postExternTransaction.getType()) {
                usersService.checkIfAccountCanBeWithdraw(user.getAccountBalance(), postExternTransaction.getAmount() + taxe);
                userRepository.decreaseAccountBalance(userId, postExternTransaction.getAmount() + taxe);
                LOGGER.info("Account balance decreased");
            } else {
                throw new RuntimeException();
            }
            externTransactionRepository.save(mapper.map(user, postExternTransaction, new Date(), taxe));
            LOGGER.info("Transaction added");
        } catch (RuntimeException | NotAllowed e) {
            throw e;
        }
    }

    public Page<ExternTransactionDto> getExternTransactionsById(Integer page, User user) {
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, 5);

        Page<ExternTransaction> externTransactionList = externTransactionRepository.findAllByIdUserOrderByIdDesc(user, pageable);
        return externTransactionList.map(it -> {
            ExternTransactionDto dto = new ExternTransactionDto();
            if (it.getType() == '-') {
                dto.setAmount((it.getAmount() + it.getTaxe()));
            } else {
                dto.setAmount(it.getAmount());
            }
            dto.setDate(LocalDate.parse(it.getDate().format(DateTimeFormatter.ISO_DATE)));
            dto.setType(it.getType().toString());
            dto.setAccount(it.getAccount());
            return dto;
        });
    }
}
