package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.InternTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternTransactionRepository extends JpaRepository<InternTransaction, Integer> {
}
