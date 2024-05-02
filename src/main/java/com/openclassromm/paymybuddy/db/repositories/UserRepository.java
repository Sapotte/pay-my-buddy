package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);

    @Query("SELECT u.id FROM User u WHERE u.email = ?1")
    Integer findUserByEmail(String email);

    @NotNull
    Optional<User> findById(Integer id);

    Boolean existsByEmail(String email);
}
