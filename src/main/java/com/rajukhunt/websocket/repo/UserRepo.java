package com.rajukhunt.websocket.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajukhunt.websocket.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
