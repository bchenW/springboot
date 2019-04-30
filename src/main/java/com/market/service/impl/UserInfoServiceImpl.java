package com.market.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.market.mapper.UserInfoMapper;
import com.market.pojo.UserInfo;
import com.market.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public UserInfo findUser(String uid) {
		return userInfoMapper.selectByPrimaryKey(uid);
	}

	@Override
	public Set<String> getRolesByUserName(Map<String, Object> map) {
		//List<String> list = userInfoMapper.getRolesByUserName(map);
		Set<String> set = new HashSet<>(userInfoMapper.getRolesByUserName(map));
		return set;
	}

	@Override
	public Set<String> getPermissionsByUserName(Map<String, Object> map) {
		return new HashSet<>(userInfoMapper.getPermissionsByUserName(map));
	}

	@Override
	public UserInfo selectByUsername(String userName) {
		System.out.println("selectByUsername:"+userName);
		return userInfoMapper.selectByUsername(userName);
	}
	
	
	
	
}
