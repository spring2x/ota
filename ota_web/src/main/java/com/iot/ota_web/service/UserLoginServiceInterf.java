package com.iot.ota_web.service;

import com.alibaba.fastjson.JSONObject;

public interface UserLoginServiceInterf {
	
	void userLogin(JSONObject params, JSONObject result) throws Exception;
}
