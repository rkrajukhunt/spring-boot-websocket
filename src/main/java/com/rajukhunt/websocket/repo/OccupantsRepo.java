package com.rajukhunt.websocket.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajukhunt.websocket.model.Occupants;

public interface OccupantsRepo extends JpaRepository<Occupants, Long>{
	
	Optional<Occupants> findByUserIdAndRoomId(Long userId, Long roomId);
}
