package com.iot.ota_web.bean;

import java.sql.Timestamp;

public class Token {
	//token id
	public Integer id;
	//token 值
	public String uuid;
	//token 过期时间
	public Timestamp expireTime;
	//token 类型   1-用户相关的token
	public Integer type;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Timestamp getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}
	
	public Token() {
		super();
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Token(Integer id, String uuid, Timestamp expireTime, Integer type) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.expireTime = expireTime;
		this.type = type;
	}
	
	
	
	
}
