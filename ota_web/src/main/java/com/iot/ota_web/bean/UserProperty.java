package com.iot.ota_web.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProperty {
	
	@Value("${user_register_verify_code_expired_time}")
	private long verfyCodeExpiredTime;
	@Value("${user_longin_token_expire_time}")
	private int tokenExpiredTime;
	public long getVerfyCodeExpiredTime() {
		return verfyCodeExpiredTime;
	}
	public void setVerfyCodeExpiredTime(long verfyCodeExpiredTime) {
		this.verfyCodeExpiredTime = verfyCodeExpiredTime;
	}
	public int getTokenExpiredTime() {
		return tokenExpiredTime;
	}
	public void setTokenExpiredTime(int tokenExpiredTime) {
		this.tokenExpiredTime = tokenExpiredTime;
	}
	
	
}
