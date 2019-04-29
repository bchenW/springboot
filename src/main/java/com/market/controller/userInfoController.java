package com.market.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.pojo.UserInfo;
import com.market.service.UserInfoService;

@RestController
public class userInfoController {

	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping("hello")
	public String hello() {
		return "hello world";
	}
	
	@RequestMapping("user")
	public UserInfo showUser() {
		UserInfo user = new UserInfo();
		user.setName("张三丰");
		user.setPassword("123456");
		return user;
	}
	
	@RequestMapping("list")
	public List<UserInfo> showMap() {
		List<UserInfo> list = new ArrayList<UserInfo>();
		UserInfo user = new UserInfo();
		user.setName("张四丰");
		user.setPassword("123456");
		list.add(user);
		return list;
	}
	
	//@Cacheable(value="findUser")
	@RequestMapping("findUser")
	public UserInfo findUser(String id) {
		System.out.println("进入findUser");
		return userInfoService.findUser(id);
	}
}
