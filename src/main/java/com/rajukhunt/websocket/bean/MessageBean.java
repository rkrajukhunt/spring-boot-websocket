package com.rajukhunt.websocket.bean;

import java.io.Serializable;

import com.rajukhunt.websocket.model.Message.MessageType;

public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long roomId;
	private Long senderId;
	private String message;
	private UserBean sender;
	private MessageType type;
	private Long time;
	
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserBean getSender() {
		return sender;
	}
	public void setSender(UserBean sender) {
		this.sender = sender;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}	
}
