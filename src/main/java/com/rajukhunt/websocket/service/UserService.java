package com.rajukhunt.websocket.service;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.UserBean;

public interface UserService {

	public GenericRes<?> register(UserBean bean) throws Exception;

	public GenericRes<?> login(UserBean bean) throws Exception;

}
