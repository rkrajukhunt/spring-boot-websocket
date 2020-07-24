package com.rajukhunt.websocket.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.UserBean;
import com.rajukhunt.websocket.model.User;
import com.rajukhunt.websocket.repo.UserRepo;
import com.rajukhunt.websocket.service.UserService;
import com.rajukhunt.websocket.utils.ResponseEnum;
import com.rajukhunt.websocket.utils.ResponseUtils;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepo userRepo;

	@Override
	public GenericRes<?> register(UserBean bean) throws Exception {
		Optional<User> userOp = userRepo.findByEmail(bean.getEmail());
		if (userOp.isPresent()) {
			return ResponseUtils.error("User already exsists", ResponseEnum.SIGNUP_RESPONSE,"409 ");
		}
		User entity = new User();
		entity.setName(bean.getName());
		entity.setEmail(bean.getEmail());
		
		return ResponseUtils.success(userRepo.save(entity).toBean(),ResponseEnum.SIGNUP_RESPONSE);
	}

	@Override
	public GenericRes<?> login(UserBean bean) throws Exception {
		try {
			Optional<User> obj = userRepo.findByEmail(bean.getEmail());
			if(obj.isPresent())
				return ResponseUtils.success(obj.get().toBean(),ResponseEnum.SIGNIN_RESPONSE);
			else
				return ResponseUtils.error("User doesn't found !!!", ResponseEnum.SIGNIN_RESPONSE,"400");
		} catch (Exception e) {
			return ResponseUtils.error("Internal Server Error", ResponseEnum.SIGNIN_RESPONSE,"500");
		}
	}
}
