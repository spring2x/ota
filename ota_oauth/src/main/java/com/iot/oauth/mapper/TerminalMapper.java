package com.iot.oauth.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.Terminal;

/**
 * 设备终端相关的接口
 * @author liqiang
 *
 */
public interface TerminalMapper {

	/**
	 * 添加终端类型
	 * @param params
	 */
	void addTerminal(JSONObject params);
	
	/**
	 * 根据终端类型，或业务平台url，获取对应的设备终端
	 * @param params
	 * @return
	 */
	List<Terminal> getTerminalsByTypeOrUrl(JSONObject params);
	
	/**
	 * 获取已有的终端类型，包括创建人信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getTerminals(Map<String, Object> params);
	
	/**
	 * 删除终端类型。
	 * @param params
	 */
	void deleteTerminal(Map<String, Object> params);
	
	/**
	 * 更新终端的信息
	 * @param params
	 */
	void updateTerminal(JSONObject params);
}
