package com.iot.ota_upgrade.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.DeviceToken;

public interface DeviceTokenMapper {

	/**
	 * 添加设备的token
	 * @param deviceToken
	 */
	void addDeviceToken(DeviceToken deviceToken);
	
	/**
	 * 获取token.
	 * @param params
	 * @return
	 */
	List<DeviceToken> getDeviceTokens(JSONObject params);
	
	/**
	 * 更新设备的token.
	 * @param params
	 */
	void updateDeviceToken(JSONObject params);
}
