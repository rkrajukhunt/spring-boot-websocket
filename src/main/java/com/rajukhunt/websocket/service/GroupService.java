package com.rajukhunt.websocket.service;

import java.util.List;

import com.rajukhunt.websocket.bean.GroupBean;

public interface GroupService {

	public GroupBean createGroup(GroupBean bean) throws Exception;

	public void joinMember(GroupBean bean) throws Exception;

	public List<GroupBean> getList(GroupBean bean) throws Exception;
}
