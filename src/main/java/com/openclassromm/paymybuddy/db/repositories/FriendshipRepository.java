package com.openclassromm.paymybuddy.db.repositories;

import com.openclassromm.paymybuddy.db.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
}
