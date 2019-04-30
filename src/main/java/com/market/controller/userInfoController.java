package com.market.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market.pojo.UserInfo;
import com.market.service.UserInfoService;

@RestController("/users")
public class userInfoController {

	/*@Autowired
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
	}*/
	
    @GetMapping
    public String get() {
        return "get.....";
    }

    /**
     * RequiresRoles 是所需角色 包含 AND 和 OR 两种
     * RequiresPermissions 是所需权限 包含 AND 和 OR 两种
     *
     * @return msg
     */
    @RequiresRoles(value = {"admin", "test"}, logical = Logical.OR)
    //@RequiresPermissions(value = {"user:list", "user:query"}, logical = Logical.OR)
    @GetMapping("/query")
    public String query() {
        return "query.....";
    }

    @GetMapping("/find")
    public String find() {
        return "find.....";
    }
}
