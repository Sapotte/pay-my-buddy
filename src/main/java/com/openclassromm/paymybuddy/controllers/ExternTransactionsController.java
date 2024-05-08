package com.openclassromm.paymybuddy.controllers;

import com.openclassromm.paymybuddy.controllers.dto.PostExternTransaction;
import com.openclassromm.paymybuddy.errors.NotAllowed;
import com.openclassromm.paymybuddy.services.ExternTransactionsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/externTransactions")
public class ExternTransactionsController {
    private final Logger LOGGER = LogManager.getLogger(ExternTransactionsController.class);

    @Autowired
    ExternTransactionsService externTransactionsService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String postExternTransaction(@ModelAttribute("postExternTransaction") PostExternTransaction postExternTransaction) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            externTransactionsService.saveTransaction(Integer.valueOf(auth.getName()), postExternTransaction);
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
