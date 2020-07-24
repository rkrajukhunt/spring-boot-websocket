package com.rajukhunt.websocket.service;

import com.rajukhunt.websocket.bean.UserBean;

public interface UserService {

  public UserBean register(UserBean bean) throws Exception;

  public UserBean login(UserBean bean) throws Exception;

}
