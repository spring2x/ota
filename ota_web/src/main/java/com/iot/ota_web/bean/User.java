package com.iot.ota_web.bean;

import java.sql.Timestamp;

public class User {
	//用户id
	public Integer id;
	//用户名
	public String name;
	//用户密码
	public String phone;
	//用户上一次登录时间
	public Timestamp lastLogin;
	//用户对应的token id
	public Integer userTokenId;
	//密码
	public String password;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	public Integer getUserTokenId() {
		return userTokenId;
	}
	public void setUserTokenId(Integer userTokenId) {
		this.userTokenId = userTokenId;
	}
	
	public User(Integer id, String name, String phone, Timestamp lastLogin, Integer userTokenId, String password) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.lastLogin = lastLogin;
		this.userTokenId = userTokenId;
		this.password = password;
	}
	public User() {
		super();
	}
	
	
	
}
