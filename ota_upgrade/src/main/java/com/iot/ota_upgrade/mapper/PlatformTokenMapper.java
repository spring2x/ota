package com.iot.ota_upgrade.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface PlatformTokenMapper {

	/**
	 * 获取业务平台的token
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getPlatformToken(JSONObject params);
	
	/**
	 * 增加业务平台token
	 * @param params
	 */
	void addPlatformToken(JSONObject params);
}
