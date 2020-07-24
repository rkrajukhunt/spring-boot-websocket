package com.rajukhunt.websocket.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.model.Occupants;
import com.rajukhunt.websocket.model.Room;
import com.rajukhunt.websocket.model.Room.RoomType;
import com.rajukhunt.websocket.model.User;
import com.rajukhunt.websocket.repo.RoomRepo;
import com.rajukhunt.websocket.repo.UserRepo;
import com.rajukhunt.websocket.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private RoomRepo roomRepo;

	@Autowired
	private UserRepo userRepo;

	@Override
	public GroupBean createGroup(GroupBean bean) throws Exception {
		Room room = new Room();
		room.setTitle(bean.getTitle());
		room.setType(RoomType.GROUP);

		User persistUser = userRepo.findById(bean.getCreatedBy())
				.orElseThrow(() -> new Exception("User not found !!!"));
		room.setCreatedBy(persistUser);

		Occupants occupants = new Occupants();
		occupants.setRoom(room);
		occupants.setUser(persistUser);
		room.setOccupants(Arrays.asList(occupants));
		return roomRepo.save(room).toBean();
	}

	@Override
	public void joinMember(GroupBean bean) throws Exception {
		Room room = roomRepo.findById(bean.getId()).orElseThrow(() -> new Exception("Group not found."));
		User user = userRepo.findById(bean.getUserId()).orElseThrow(() -> new Exception("User not found !!!"));

		boolean check = room.getOccupants().stream().filter(o -> o.getUser().getId().equals(user.getId())).findAny()
				.isPresent();
		if (!check) {
			Occupants occupants = new Occupants();
			occupants.setRoom(room);
			occupants.setUser(user);
			roomRepo.save(room);
		}
	}

	@Override
	public List<GroupBean> getList(GroupBean bean) throws Exception {
		List<Room> rooms = roomRepo.findByType(RoomType.GROUP);
		return rooms.stream().map(room -> {
			GroupBean _bean = room.toBean();
			boolean check = room.getOccupants().stream().filter(o -> o.getUser().getId().equals(bean.getUserId()))
					.findAny().isPresent();
			_bean.setUserExsist(check);
			return _bean;
		}).collect(Collectors.toList());
	}

}
