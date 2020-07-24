package com.rajukhunt.websocket.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajukhunt.websocket.model.Room;
import com.rajukhunt.websocket.model.Room.RoomType;

public interface RoomRepo extends JpaRepository<Room, Long> {

	List<Room> findByType(RoomType type);

}
