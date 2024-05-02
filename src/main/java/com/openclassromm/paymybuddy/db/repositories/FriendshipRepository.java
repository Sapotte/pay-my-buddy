package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query(value = "SELECT fs.id2User, fs.idUser FROM Friendship fs WHERE fs.idUser = :userId OR fs.id2User = ?1")
    List<Friendship> findFriendshipsByUserId(@Param("userId") Integer userId);
}
