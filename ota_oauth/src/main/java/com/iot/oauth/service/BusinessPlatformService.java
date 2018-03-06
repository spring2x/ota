package com.iot.oauth.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.PlatformProperty;
import com.iot.oauth.bean.Terminal;
import com.iot.oauth.bean.Token;
import com.iot.oauth.mapper.PlatformTokenMapper;
import com.iot.oauth.mapper.TerminalMapper;
import com.iot.oauth.mapper.TokenMapper;
import com.iot.oauth.util.ExceptionUtil;

@Service
public class BusinessPlatformService {
	
	Logger logger = LogManager.getLogger(BusinessPlatformService.class.getName());
	
	@Autowired
	TerminalMapper terminalMapper;
	@Autowired
	PlatformTokenMapper platformTokenMapper;
	@Autowired
	TokenService tokenService;
	@Autowired
	TokenMapper tokenMapper;
	
	@Autowired
	PlatformProperty platformProperty;
	
	/**
	 * 业务平台鉴权服务
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public void platformAuthentProcess(JSONObject params, JSONObject result) throws Exception{
		if (!params.containsKey("authent_url") || "".equals(params.getString("authent_url")) || null == params.get("authent_url")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		//String md5AuthentUrl = MD5Util.EncryptionStr(params.getString("authent_url"), MD5Util.MD5);
		params.put("businessPlatformUrl", params.getString("authent_url"));
		
		try {
			List<Terminal> terminals = terminalMapper.getTerminalsByTypeOrUrl(params);
			if (terminals == null || terminals.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "无权限");
				return;
			}
			params.put("terminal_id", terminals.get(0).getId());
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
	}
	
	/**
	 * 生成token
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addPlatformToken(JSONObject params, JSONObject result) throws Exception{
		
		List<Map<String, Object>> platformtoken = platformTokenMapper.getPlatformToken(params);
		if (platformtoken != null && !platformtoken.isEmpty()) {
			String orgToken = (String) platformtoken.get(0).get("token");
			JSONObject param = new JSONObject();
			param.put("org_token", orgToken);
			updatePlatformToken(param, result);
			return;
		}
		
		params.put("token_type", 2);
		int expireTime = platformProperty.getPlatformTokenExpiredTime();
		try {
			Token token = tokenService.createAccessToken(params, expireTime);
			params.put("token_id", token.getId());
			platformTokenMapper.addPlatformToken(params);
			result.put("token", token.getUuid());
			result.put("token_expireTime", token.getExpireTime());
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
	}
	
	/**
	 * 刷新token.
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updatePlatformToken(JSONObject params, JSONObject result) throws Exception{
		if (!params.containsKey("org_token") || null == params.get("org_token") || "".equals("org_token")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		params.put("uuid", params.get("org_token"));
		List<Token> tokens = tokenMapper.getTokens(params);
		if (tokens == null || tokens.isEmpty()) {
			result.put("code", "0001");
			result.put("message", "token不存在");
			return;
		}
		
		int expireTime = platformProperty.getPlatformTokenExpiredTime();
		tokenService.updateToken(params, expireTime);
		result.put("token", params.get("uuid"));
		result.put("token_expireTime", params.get("expireTime"));
	}
}
