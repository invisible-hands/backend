package com.betting.ground.user.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betting.ground.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickname);

	Optional<User> findByEmailOrNickname(String email, String nickname);

	boolean existsByEmail(String email);

	Set<User> findAllByIdIn(Set<Long> buyerId);
}
