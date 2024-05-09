package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.InternTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InternTransactionRepository extends JpaRepository<InternTransaction, Integer> {
    @Query("SELECT it FROM InternTransaction it WHERE it.idUser.id = :userId OR it.idFriend = :userId ORDER BY it.id DESC")
    Page<InternTransaction> findAllByIdUserOrIdFriendOrderByIdDesc(Integer userId, Pageable pageable);
}
