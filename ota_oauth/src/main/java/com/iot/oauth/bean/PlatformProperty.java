package com.iot.oauth.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlatformProperty {

	
	@Value("${business_platform_token_expire_time}")
	private int platformTokenExpiredTime;
	
	@Value("${device_token_expire_time}")
	private int deviceTokenExpiredTime;
	
	@Value("${ota.server.ip}")
	private String otaServerIp;
	
	@Value("${ota.server.port}")
	private int otaServerPort;

	public int getPlatformTokenExpiredTime() {
		return platformTokenExpiredTime;
	}

	public void setPlatformTokenExpiredTime(int platformTokenExpiredTime) {
		this.platformTokenExpiredTime = platformTokenExpiredTime;
	}

	public int getDeviceTokenExpiredTime() {
		return deviceTokenExpiredTime;
	}

	public void setDeviceTokenExpiredTime(int deviceTokenExpiredTime) {
		this.deviceTokenExpiredTime = deviceTokenExpiredTime;
	}

	public String getOtaServerIp() {
		return otaServerIp;
	}

	public void setOtaServerIp(String otaServerIp) {
		this.otaServerIp = otaServerIp;
	}

	public int getOtaServerPort() {
		return otaServerPort;
	}

	public void setOtaServerPort(int otaServerPort) {
		this.otaServerPort = otaServerPort;
	}
	
	
	
}
