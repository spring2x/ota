package com.iot.ota_web.timer;

import java.util.Map;
import java.util.TimerTask;

/**
 * 用户注册时，短信验证码过期定时器
 * @author liqiang
 *
 */
public class VerifyTimerTask extends TimerTask{
	
	public String phone;
	public Map<String, String> verifyCodeMap;
	
	@Override
	public void run() {
		if (verifyCodeMap.containsKey(phone)) {
			verifyCodeMap.remove(phone);
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public VerifyTimerTask(String phone, Map<String, String> verifyCodeMap) {
		super();
		this.phone = phone;
		this.verifyCodeMap = verifyCodeMap;
	}

	public Map<String, String> getVerifyCodeMap() {
		return verifyCodeMap;
	}

	public void setVerifyCodeMap(Map<String, String> verifyCodeMap) {
		this.verifyCodeMap = verifyCodeMap;
	}
	
	
}
