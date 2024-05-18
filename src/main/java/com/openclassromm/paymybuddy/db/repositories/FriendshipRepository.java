package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.Friendship;
import com.openclassromm.paymybuddy.db.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query(value = "SELECT fs FROM Friendship fs WHERE (fs.idUser.id = :userId OR fs.idFriend = :userId) AND fs.isActive = true")
    List<Friendship> findFriendshipsByUserId(@Param("userId") Integer userId);

    @Modifying
    @Query(value = "UPDATE Friendship fs SET fs.isActive = false WHERE (fs.idUser.id = :userId OR fs.idFriend = :userId) AND fs.isActive = true")
    void updateFriendshipStatus(User user);
}
