package com.market.service.impl;

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
	
	
	
	
}
