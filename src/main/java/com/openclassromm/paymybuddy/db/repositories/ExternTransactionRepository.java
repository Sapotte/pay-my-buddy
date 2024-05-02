package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExternTransactionRepository extends JpaRepository<ExternTransaction, Integer> {
}
