package com.iot.ota_web.service;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;

public interface UserLoginServiceInterf {
	
	void userLogin(User user, JSONObject result) throws Exception;
}
