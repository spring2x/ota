package com.iot.oauth.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.Token;

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
