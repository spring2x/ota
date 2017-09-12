package com.iot.ota_upgrade.bean;

import java.sql.Timestamp;

public class DeviceToken {

	
	public Integer id;
	public String mark;
	public String uuid;
	public Integer type;
	public Timestamp expireTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Timestamp getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Timestamp expireTime) {
		this.expireTime = expireTime;
	}
	public DeviceToken(Integer id, String mark, String uuid, Integer type, Timestamp expireTime) {
		super();
		this.id = id;
		this.mark = mark;
		this.uuid = uuid;
		this.type = type;
		this.expireTime = expireTime;
	}
	public DeviceToken() {
		super();
	}
	
	
	
}
