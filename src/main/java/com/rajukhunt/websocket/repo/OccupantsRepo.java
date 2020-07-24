package com.rajukhunt.websocket.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajukhunt.websocket.model.Occupants;
import com.rajukhunt.websocket.model.Room;

public interface OccupantsRepo extends JpaRepository<Occupants, Long>{
	
	Optional<Occupants> findByUserIdAndRoomId(Long userId, Long roomId);
	
	List<Occupants> findByRoom(Room room);
	
}
