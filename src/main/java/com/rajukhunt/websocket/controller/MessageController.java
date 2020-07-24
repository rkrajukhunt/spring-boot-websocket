package com.rajukhunt.websocket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.bean.UserBean;
import com.rajukhunt.websocket.service.GroupService;
import com.rajukhunt.websocket.service.UserService;

@Controller
public class MessageController {

  @Autowired
  private UserService userService;

  @Autowired
  private GroupService groupService;

  @MessageMapping("/user.add")
  @SendToUser("/topic/register")
  public UserBean register(@Payload UserBean bean) throws Exception {
    UserBean _bean = userService.register(bean);
    return _bean;
  }

  @MessageMapping("/user.login")
  @SendToUser("/topic/login")
  public UserBean login(@Payload UserBean bean) throws Exception {
    UserBean _bean = userService.login(bean);
    return _bean;
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
