package com.market.pojo;

import java.io.Serializable;

/**
 * 
 * 
 * @author wangyu
 * 
 * @date 2019-03-25
 */
public class UserInfo implements Serializable{
    private String uid;

    private String username;

    private String name;

    private String password;

    private String salt;

    private String state;	//是否禁用

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

	public UserInfo(String uid, String username, String name, String password, String salt, String state) {
		super();
		this.uid = uid;
		this.username = username;
		this.name = name;
		this.password = password;
		this.salt = salt;
		this.state = state;
	}

	public UserInfo() {
		super();
	}
	
	
    
    
}