package com.market.config;

import java.util.*;

import com.market.pojo.UserInfo;

public class DBCache {
	/**
     * K 用户名
     * V 用户信息
     */
    public static final Map<String, UserInfo> UserInfoS_CACHE = new HashMap<>();
    /**
     * K 角色ID
     * V 权限编码
     */
    public static final Map<String, Collection<String>> PERMISSIONS_CACHE = new HashMap<>();

    static {
        // TODO 假设这是数据库记录
        UserInfoS_CACHE.put("u1", new UserInfo("1", "u1","u1", "p1", "u1", "0"));
        UserInfoS_CACHE.put("u2", new UserInfo("2", "u2","u2", "p2", "u2", "0"));
        UserInfoS_CACHE.put("u3", new UserInfo("3", "u3","u3", "p3", "u3","0"));

        PERMISSIONS_CACHE.put("admin", Arrays.asList("UserInfo:list", "UserInfo:add", "UserInfo:edit"));
        PERMISSIONS_CACHE.put("test", Collections.singletonList("UserInfo:list"));

    }
}