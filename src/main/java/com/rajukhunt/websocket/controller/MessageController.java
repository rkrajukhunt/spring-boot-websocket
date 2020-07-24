package com.rajukhunt.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.GroupBean;
import com.rajukhunt.websocket.bean.MessageBean;
import com.rajukhunt.websocket.bean.UserBean;
import com.rajukhunt.websocket.model.Message;
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
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> createGroup(@Payload GroupBean bean) throws Exception {
	  return groupService.createGroup(bean);
  }

  @MessageMapping("/group.join")
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> joinGroup(@Payload GroupBean bean) throws Exception {
    return groupService.joinMember(bean);
  }
  
  @MessageMapping("/group.leave")
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> leaveGroup(@Payload GroupBean bean) throws Exception {
    return groupService.leaveMember(bean);
  }
  
  @MessageMapping("/group.list")
  @SendToUser("/topic/websocket_response")
  public GenericRes<?> groupList(@Payload GroupBean bean) throws Exception {
    return groupService.getList(bean);
  }

  @MessageMapping("/group.message.list")
  @SendToUser("/topic/chat_list")
  public GenericRes<?> messageList(@Payload MessageBean bean) throws Exception {
	  return groupService.messageList(bean);
  }
  
  @MessageMapping("/group.message")
  @SendToUser("/topic/chat_message_status")
  public GenericRes<?> message(@Payload MessageBean bean) throws Exception {
	  return groupService.sendMessage(bean);
  }
  
  
  @MessageExceptionHandler
  @SendToUser("/queue/errors")
  public String handleException(Throwable exception, @Header(name = "simpSessionId") String sessionId) {
    exception.printStackTrace();
    return exception.getMessage();
  }
}
