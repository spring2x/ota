package com.iot.ota_web.service;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.SMSObject;
import com.iot.ota_web.bean.UserProperty;
import com.iot.ota_web.timer.VerifyTimerTask;

@Service
public class CommonService {
	
	@Autowired
	UserProperty userProperty;
	
	Logger logger = LogManager.getLogger(CommonService.class.getName());
	
	//注册时，保存手机验证码的 map
	public static final Map<String, String> registerVerifyMap = new ConcurrentHashMap<>();
	
	/**
	 * 获取短信验证码
	 * @param params
	 * @param result
	 * @return
	 * @throws IOException
	 */
	public void getVerifyCode(Map<String, Object> params, JSONObject result) throws IOException {
		String phone = String.valueOf(params.get("phone"));
		String code = (int) ((Math.random() * 9 + 1) * 100000) + "";

		// 短信模板参数集合
		SMSObject smsObject = new SMSObject();
		smsObject.addParam("sicode", "2eed2e55640d4db18d0a8321f3282a5e");
		smsObject.addParam("tempid", "10756");
		smsObject.addParam("mobiles", phone);
		smsObject.addParam("code", code);

		SMSObject.SMSSendResponse sendResponse = smsObject.sendSMS();

		int responseCode = sendResponse.getResponseCode();
		if (responseCode == 200) {
			registerVerifyMap.put(phone, code);
			VerifyTimerTask task = new VerifyTimerTask(phone, registerVerifyMap);
			Timer timer = new Timer(true);
			timer.schedule(task, userProperty.getVerfyCodeExpiredTime());
			//result.put("verify_code", code);
		} else {
			result.put("code", "0001");
			result.put("message", "获取验证码错误");
		}
	}
}
