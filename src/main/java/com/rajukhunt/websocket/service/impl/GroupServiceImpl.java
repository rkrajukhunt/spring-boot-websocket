package com.rajukhunt.websocket.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.bean.MessageBean;
import com.rajukhunt.websocket.model.Message;
import com.rajukhunt.websocket.model.Occupants;
import com.rajukhunt.websocket.model.Room;
import com.rajukhunt.websocket.model.Room.RoomType;
import com.rajukhunt.websocket.model.User;
import com.rajukhunt.websocket.repo.MessageRepo;
import com.rajukhunt.websocket.repo.OccupantsRepo;
import com.rajukhunt.websocket.repo.RoomRepo;
import com.rajukhunt.websocket.repo.UserRepo;
import com.rajukhunt.websocket.service.GroupService;
import com.rajukhunt.websocket.utils.ResponseEnum;
import com.rajukhunt.websocket.utils.ResponseUtils;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private RoomRepo roomRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private MessageRepo messageRepo;

	@Autowired
	private OccupantsRepo occupantsRepo;
	
	@Autowired
	private SimpMessageSendingOperations messageSender;
	
	@Value("${chat.not-allow-time}")
	private Long timeAllow;
	
	@Override
	public GenericRes<?> createGroup(GroupBean bean) throws Exception {
		Room room = new Room();
		room.setTitle(bean.getTitle());
		room.setType(RoomType.GROUP);

		Optional<User> persistUser = userRepo.findById(bean.getCreatedBy());
		if (persistUser.isPresent()) {
			room.setCreatedBy(persistUser.get());
			Occupants occupants = new Occupants();
			occupants.setRoom(room);
			occupants.setUser(persistUser.get());
			room.setOccupants(Arrays.asList(occupants));
			updateGroup(bean.getCreatedBy());
			return ResponseUtils.success(roomRepo.save(room).toBean(), ResponseEnum.GROUP_CREATE_RESPONSE);
		} else
			return ResponseUtils.error("User not found !!!", ResponseEnum.GROUP_CREATE_RESPONSE);
	}

	@Override
	public GenericRes<?> joinMember(GroupBean bean) throws Exception {
		try {
			Optional<Room> room = roomRepo.findById(bean.getId());
			if (!room.isPresent())
				return ResponseUtils.error("Group not found", ResponseEnum.GROUP_RESPONSE);

			Optional<User> user = userRepo.findById(bean.getUserId());
			if (!user.isPresent())
				return ResponseUtils.error("User not found !!!", ResponseEnum.GROUP_RESPONSE);

			boolean check = room.get().getOccupants().stream()
					.filter(o -> o.getUser().getId().equals(user.get().getId())).findAny().isPresent();

			if (!check) {
				Occupants occupants = new Occupants();
				occupants.setRoom(room.get());
				occupants.setUser(user.get());
				roomRepo.save(room.get());
				updateGroup(bean.getUserId());
				return ResponseUtils.success("successfully joined!!!", ResponseEnum.GROUP_RESPONSE);
			} else
				return ResponseUtils.success("something went wrong", ResponseEnum.GROUP_RESPONSE);
		} catch (Exception e) {
			return ResponseUtils.success("Internal Server Error", ResponseEnum.GROUP_RESPONSE);
		}
	}

	@Override
	public GenericRes<?> getList(GroupBean bean) throws Exception {
		List<Room> rooms = roomRepo.findByType(RoomType.GROUP);
		List<GroupBean> found = rooms.stream().map(room -> {
			GroupBean _bean = room.toBean();
			boolean check = room.getOccupants().stream().filter(o -> o.getUser().getId().equals(bean.getUserId()))
					.findAny().isPresent();
			_bean.setUserExsist(check);
			return _bean;
		}).collect(Collectors.toList());
		return ResponseUtils.success(found, ResponseEnum.GROUP_LIST_RESPONSE);
	}

	@Override
	public GenericRes<?> leaveMember(GroupBean bean) throws Exception {
		try {
			Optional<Room> room = roomRepo.findById(bean.getId());
			if (!room.isPresent())
				return ResponseUtils.error("Group not found", ResponseEnum.GROUP_RESPONSE);

			Optional<User> user = userRepo.findById(bean.getUserId());
			if (!user.isPresent())
				return ResponseUtils.error("User not found !!!", ResponseEnum.GROUP_RESPONSE);
			
			Optional<Occupants> foundId = occupantsRepo.findByUserIdAndRoomId(bean.getUserId(), room.get().getId());
			if(!foundId.isPresent()) {
				return ResponseUtils.error("something went wrong", ResponseEnum.GROUP_RESPONSE);
			}else {
				occupantsRepo.delete(foundId.get());
				updateGroup(bean.getUserId());
				return ResponseUtils.success("successfully group leave!!!", ResponseEnum.GROUP_RESPONSE);
			}
		} catch (Exception e) {
			return ResponseUtils.error("Internal Server Error : "+e.getMessage(), ResponseEnum.GROUP_RESPONSE);
		}
	}

	@Override
	public GenericRes<?> sendMessage(MessageBean bean) throws Exception {
		Date date = new Date();
//		if(date.getHours() > timeAllow) {
//			return ResponseUtils.error("message not delivered after 10 pm", ResponseEnum.CHAT_STATUS_RESPONSE);
//		}
		
		Optional<Room> roomOp = roomRepo.findById(bean.getRoomId());
		if(!roomOp.isPresent())
			return ResponseUtils.error("Group not found", ResponseEnum.CHAT_RESPONSE);
		
		Optional<User> userOp = userRepo.findById(bean.getSenderId());
		if(!userOp.isPresent())
			return ResponseUtils.error("User not found", ResponseEnum.CHAT_RESPONSE);
		
		
		Room room = roomOp.get();
		Message message = new Message();
		message.setRoom(room);
		message.setMessage(bean.getMessage());
		message.setCreatedAt(new Date());
		message.setSender(userOp.get());
		
		MessageBean _bean = messageRepo.save(message).toBean();
		room.getOccupants().parallelStream().forEach(o->{
			String channel = new StringBuilder("/topic/").append(room.getId()).append("/chat").toString();
			messageSender.convertAndSend(channel, _bean);
		});
		return ResponseUtils.success(_bean,  ResponseEnum.CHAT_RESPONSE);
	}

	@Override
	public GenericRes<?> messageList(MessageBean bean) throws Exception {
		List<Message> messages = messageRepo.findByRoomId(bean.getRoomId());
		return ResponseUtils.success(messages.stream().map(Message::toBean),  ResponseEnum.CHAT_RESPONSE);
	}
	
	public void updateGroup(Long userId) {
//		//Optional<Occupants> foundId = occupantsRepo.findByUserIdAndRoomId(userId, room.get().getId());
//		
//		List<Room> rooms = roomRepo.findByType(RoomType.GROUP);
//		List<GroupBean> found = rooms.stream().map(room -> {
//			GroupBean _bean = room.toBean();
//			boolean check = room.getOccupants().stream().filter(o -> o.getUser().getId().equals(userId))
//					.findAny().isPresent();
//			_bean.setUserExsist(check);
//			return _bean;
//		}).collect(Collectors.toList());
//		messageSender.convertAndSend("/topic/group_list",found);
	}
	
	public void updateGroup(Room room) {
		List<Occupants> rooms = occupantsRepo.findByRoom(room);
		
//		rooms.parallelStream().forEach(o->{
//			//o.get
//			//messageSender.convertAndSend("/topic/group_list",found);
//		};
	}
}
