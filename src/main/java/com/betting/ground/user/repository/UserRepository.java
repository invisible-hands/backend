package com.betting.ground.user.repository;

import com.betting.ground.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    int countByNickname(String Nickname);
    boolean existsByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
