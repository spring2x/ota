package com.iot.ota_web.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户与设备终端权限相关的接口
 * @author liqiang
 *
 */
public interface UserTerminalCompetenceMapper {
	
	/**
	 * 添加用户的终端权限
	 * @param params
	 */
	void addUserCompetence(JSONObject params);
	
	/**
	 * 获取用户的终端权限.
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getUserCompetence(JSONObject params);
	
	/**
	 * 更新终端用户的权限。
	 * @param params
	 */
	void updateUserCompetence(JSONObject params);
	
}
