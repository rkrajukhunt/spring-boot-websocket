package com.rajukhunt.websocket.service;

import com.rajukhunt.websocket.bean.GenericRes;
import com.rajukhunt.websocket.bean.GroupBean;

public interface GroupService {

	public GenericRes<?> createGroup(GroupBean bean) throws Exception;

	public GenericRes<?> joinMember(GroupBean bean) throws Exception;

	public GenericRes<?> leaveMember(GroupBean bean) throws Exception;

	public GenericRes<?> getList(GroupBean bean) throws Exception;
}
