package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostInternTransaction;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.InternTransactionsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/internTransactions")
public class InternTransactionsController {
    @Autowired
    InternTransactionsService internTransactionsService;
    private final Logger LOGGER = LogManager.getLogger(InternTransactionsController.class);

    @PostMapping
    public String postInternTransaction(PostInternTransaction postInternTransaction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            internTransactionsService.saveTransaction(Integer.valueOf(auth.getName()), postInternTransaction);
        } catch (Exception e) {
            LOGGER.error(e);
            if (e instanceof NotAllowed) {
                return "redirect:/account?notEnough";
            } else {
                return "redirect:/account?errorUnknown";
            }
        }
        return "redirect:/account";
    }
}
