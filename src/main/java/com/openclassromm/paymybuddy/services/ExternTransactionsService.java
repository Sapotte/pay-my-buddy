package com.openclassromm.paymybuddy.services;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import com.openclassromm.paymybuddy.db.repositories.ExternTransactionRepository;
import com.openclassromm.paymybuddy.db.repositories.UserRepository;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.mappers.ExternTransactionServiceMapperImpl;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExternTransactionsService {
    private final Logger LOGGER = LogManager.getLogger(ExternTransactionsService.class);

    @Autowired
    ExternTransactionRepository externTransactionRepository;

    @Autowired
    UserRepository userRepository;

    private final ExternTransactionServiceMapperImpl mapper = new ExternTransactionServiceMapperImpl();

    @Transactional
    public void saveTransaction(Integer userId, PostExternTransaction postExternTransaction) throws NotAllowed {
        User user = userRepository.findById(userId).orElse(null);
        try {
            if ("+".equals(postExternTransaction.getType())) {
                userRepository.increaseAccountBalance(userId, postExternTransaction.getAmount());
                LOGGER.info("Account balance increased");
            } else if ("-".equals(postExternTransaction.getType())) {
                checkIfAccountCanBeWithdraw(user.getAccountBalance(), postExternTransaction.getAmount());
                userRepository.decreaseAccountBalance(userId, postExternTransaction.getAmount());
                LOGGER.info("Account balance decreased");
            } else {
                throw new RuntimeException();
            }
            externTransactionRepository.save(mapper.map(user, postExternTransaction, new Date()));
            LOGGER.info("Transaction added");
        } catch (RuntimeException | NotAllowed e) {
            throw e;
        }
    }

    private void checkIfAccountCanBeWithdraw(Float accountBalance, Float amount) throws NotAllowed {
        if (accountBalance < amount) {
            throw new NotAllowed("Not enough money in your account");
        }
    }

    public Page<ExternTransaction> getExternTransactionsById(Integer page, User user) {
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, 5);
        return externTransactionRepository.findAllByIdUserOrderByIdDesc(user, pageable);
    }
}
