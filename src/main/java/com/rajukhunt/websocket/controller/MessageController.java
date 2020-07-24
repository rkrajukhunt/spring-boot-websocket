package com.rajukhunt.websocket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.bean.ResponseEnum;
import com.rajukhunt.websocket.bean.UserBean;
import com.rajukhunt.websocket.service.GroupService;
import com.rajukhunt.websocket.service.UserService;
import com.rajukhunt.websocket.utils.ResponseUtils;

@Controller
public class MessageController implements ResponseUtils{

  @Autowired
  private UserService userService;

  @Autowired
  private GroupService groupService;

  @MessageMapping("/user.signup")
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> register(@Payload UserBean bean) throws Exception {
    return userService.register(bean);
  }

  @MessageMapping("/user.signin")
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> login(@Payload UserBean bean) throws Exception {
    return userService.login(bean);
  }

  @MessageMapping("/group.create")
  @SendToUser("/topic/group.create")
  public GroupBean createGroup(@Payload GroupBean bean) throws Exception {
    GroupBean _bean = groupService.createGroup(bean);
    return _bean;
  }

  @MessageMapping("/group.join")
  @SendToUser("/topic/group.join")
  public String joinGroup(@Payload GroupBean bean) throws Exception {
    groupService.joinMember(bean);
    return "Successfully Joined!!!";
  }

  @MessageMapping("/group.list")
  @SendToUser("/topic/group.list")
  public List<GroupBean> groupList(@Payload GroupBean bean) throws Exception {
    return groupService.getList(bean);
  }

  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception, @Header(name = "simpSessionId") String sessionId) {
    exception.printStackTrace();
    return exception.getMessage();
  }
}
