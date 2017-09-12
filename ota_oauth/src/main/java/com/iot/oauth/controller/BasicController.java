package com.iot.oauth.controller;

import com.alibaba.fastjson.JSONObject;

public class BasicController {
	
	public JSONObject generateResult(){
		JSONObject result = new JSONObject();
		result.put("code", "0000");
		result.put("message", "success");
		return result;
	}
	
}
