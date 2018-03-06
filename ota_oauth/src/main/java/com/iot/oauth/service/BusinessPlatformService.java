package com.iot.oauth.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.PlatformProperty;
import com.iot.oauth.bean.Terminal;
import com.iot.oauth.mapper.PlatformTokenMapper;
import com.iot.oauth.mapper.TerminalMapper;
import com.iot.oauth.mapper.TokenMapper;
import com.iot.oauth.util.DateUtil;
import com.iot.oauth.util.ExceptionUtil;
import com.iot.oauth.util.TokenUtil;

import io.jsonwebtoken.Claims;

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
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
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
	public void addPlatformToken(JSONObject params, JSONObject result) throws Exception{
		
		String authUrl = params.getString("authent_url");
		String uuid = (String) valueOperations.get(authUrl);
		if (uuid != null) {
			//将原来未过期的数据移除，防止数据积压
			valueOperations.getOperations().delete(uuid);
			valueOperations.getOperations().delete(authUrl);
		}
		
		
		JSONObject payload = new JSONObject();
		long ttlMillis = platformProperty.getPlatformTokenExpiredTime() * 60 * 1000;
		String token = TokenUtil.createToken(payload, ttlMillis, authUrl);
		Claims claims = TokenUtil.parseJWT(token, new JSONObject());
		uuid = claims.getId();
		String expired = DateUtil.DateFormat(claims.getExpiration().getTime());
		
		valueOperations.set(authUrl, uuid, platformProperty.getPlatformTokenExpiredTime(), TimeUnit.MINUTES);
		valueOperations.set(uuid, token, platformProperty.getPlatformTokenExpiredTime(), TimeUnit.MINUTES);
		
		result.put("token", uuid);
		result.put("token_expireTime", expired);
	}
	
	/**
	 * 刷新token.
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	public void updatePlatformToken(JSONObject params, JSONObject result) throws Exception{
		if (!params.containsKey("org_token") || null == params.get("org_token") || "".equals("org_token")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		Object orgUUID = params.get("org_token");
		String orgToken = (String) valueOperations.get(orgUUID);
		if (orgToken != null) {
			Claims orgClaims = TokenUtil.parseJWT(orgToken, result);
			String authUrl = orgClaims.getSubject();
			valueOperations.getOperations().delete(orgClaims.getId());
			valueOperations.getOperations().delete(authUrl);
			
			JSONObject payload = new JSONObject();
			long ttlMillis = platformProperty.getPlatformTokenExpiredTime() * 60 * 1000;
			String token = TokenUtil.createToken(payload, ttlMillis, authUrl);
			Claims claims = TokenUtil.parseJWT(token, new JSONObject());
			String uuid = claims.getId();
			String expired = DateUtil.DateFormat(claims.getExpiration().getTime());
			
			valueOperations.set(authUrl, uuid, platformProperty.getPlatformTokenExpiredTime(), TimeUnit.MINUTES);
			valueOperations.set(uuid, token, platformProperty.getPlatformTokenExpiredTime(), TimeUnit.MINUTES);
			
			result.put("token", uuid);
			result.put("token_expireTime", expired);
		}else {
			result.put("code", "0001");
			result.put("message", "token不存在");
			return;
		}
	}
}
