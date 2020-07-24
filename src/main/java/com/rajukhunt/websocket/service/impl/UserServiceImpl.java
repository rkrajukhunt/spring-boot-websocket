package com.rajukhunt.websocket.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajukhunt.websocket.bean.UserBean;
import com.rajukhunt.websocket.model.User;
import com.rajukhunt.websocket.repo.UserRepo;
import com.rajukhunt.websocket.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepo userRepo;

  @Override
  public UserBean register(UserBean bean) throws Exception {
    Optional<User> userOp = userRepo.findByEmail(bean.getEmail());
    if (userOp.isPresent()) {
      throw new Exception("User already exsist.");
    }
    User entity = new User();
    entity.setName(bean.getName());
    entity.setEmail(bean.getEmail());
    return userRepo.save(entity).toBean();
  }

  @Override
  public UserBean login(UserBean bean) throws Exception {
    return userRepo.findByEmail(bean.getEmail()).orElseThrow(() -> new Exception("User doesn't found !!!")).toBean();

  }
}
