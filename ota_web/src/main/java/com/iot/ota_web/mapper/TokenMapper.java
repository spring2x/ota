package com.iot.ota_web.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Token;


public interface TokenMapper {
	
	/**
	 * 添加token
	 * @param token
	 */
	void addToken(Token token);
	
	/**
	 * 删除token
	 * @param params
	 */
	void deleteToken(JSONObject params);
	
	/**
	 * 根据用户的id,删除用户的token
	 * @param params
	 */
	void deleteUserToken(JSONObject params);
	
	/**
	 * 刷新token
	 * @param params
	 */
	void refreshToken(JSONObject params);
	
	/**
	 * 获取token.
	 * @param params
	 * @return
	 */
	List<Token> getTokens(JSONObject params);
}
