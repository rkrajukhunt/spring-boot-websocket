package com.rajukhunt.websocket.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajukhunt.websocket.model.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {

	List<Message> findByRoomId(Long roomId);
}
