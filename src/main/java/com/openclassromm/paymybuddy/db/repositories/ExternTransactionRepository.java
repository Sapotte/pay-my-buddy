package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.ExternTransaction;
import com.openclassromm.paymybuddy.db.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternTransactionRepository extends JpaRepository<ExternTransaction, Integer> {
    @Query("SELECT et FROM ExternTransaction et WHERE et.idUser = :user ORDER BY et.id DESC")
    Page<ExternTransaction> findAllByIdUserOrderByIdDesc(User user, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ExternTransaction et WHERE et.idUser.id = :idUser")
    void deleteByIdUser(@Param("idUser") Integer idUser);
}
