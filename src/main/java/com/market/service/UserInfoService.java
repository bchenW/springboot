package com.market.service;

import java.util.Map;
import java.util.Set;

import com.market.pojo.UserInfo;

public interface UserInfoService {

	public UserInfo findUser(String uid);

	public Set<String> getRolesByUserName(Map<String, Object> map);
	
	public Set<String> getPermissionsByUserName(Map<String, Object> map);

	public UserInfo selectByUsername(String userName);
}
