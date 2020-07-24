package com.rajukhunt.websocket.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.model.Occupants;
import com.rajukhunt.websocket.model.Room;
import com.rajukhunt.websocket.model.Room.RoomType;
import com.rajukhunt.websocket.model.User;
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
			return ResponseUtils.success(roomRepo.save(room).toBean(), ResponseEnum.GROUP_RESPONSE);
		} else
			return ResponseUtils.error("User not found !!!", ResponseEnum.GROUP_RESPONSE);
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

		return ResponseUtils.success(found, ResponseEnum.GROUP_RESPONSE);
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

			boolean check = room.get().getOccupants().stream()
					.filter(o -> o.getUser().getId().equals(user.get().getId())).findAny().isPresent();
			if (check) {
				System.out.println(check);
				return ResponseUtils.success("successfully group leave!!!", ResponseEnum.GROUP_RESPONSE);
			} else
				return ResponseUtils.success("something went wrong", ResponseEnum.GROUP_RESPONSE);
		} catch (Exception e) {
			return ResponseUtils.success("Internal Server Error", ResponseEnum.GROUP_RESPONSE);
		}
	}

}
