package com.iot.ota_web.bean;

public class Terminal {
	
	/**
	 * 终端id.
	 */
	public Integer id;
	/**
	 * 终端类型
	 */
	public String type;
	/**
	 * 终端简介
	 */
	public String introduce;
	/**
	 * 终端对应的业务平台的url(采用md5加密)
	 */
	public String businessPlatformUrl;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getBusinessPlatformUrl() {
		return businessPlatformUrl;
	}
	public void setBusinessPlatformUrl(String businessPlatformUrl) {
		this.businessPlatformUrl = businessPlatformUrl;
	}
	public Terminal(Integer id, String type, String introduce, String businessPlatformUrl) {
		super();
		this.id = id;
		this.type = type;
		this.introduce = introduce;
		this.businessPlatformUrl = businessPlatformUrl;
	}
	public Terminal() {
		super();
	}
	
	
	
	
}
