package com.iot.oauth.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.Token;
import com.iot.oauth.mapper.TokenMapper;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;


@Service
public class TokenService {
	
	private Logger logger = LogManager.getLogger(TokenService.class.getName());
	@Autowired
	private TokenMapper tokenMapper;
	
	/**
	 * 生成token.
	 * @param paramJson
	 * @param expireTime
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public Token createAccessToken(JSONObject paramJson, int expireTime) throws Exception{
		String uuid = generateToken(paramJson);
		
		//设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
        calendar.setTime(expiredDate);
        calendar.add(Calendar.MINUTE, expireTime);
		
        Token token = new Token(null, uuid, new Timestamp(calendar.getTimeInMillis()), paramJson.getInteger("token_type"));
		
		try {
			tokenMapper.addToken(token);
		} catch (Exception e) {
			logger.error("add token occur error!!!   " + e.getMessage());
			throw e;
		}
		return token;
	}
	
	/**
	 * 刷新token.
	 * @param params
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateToken(JSONObject params, int expireTime) throws Exception {
		
		String uuid = generateToken(params);
		// 设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
		calendar.setTime(expiredDate);
		calendar.add(Calendar.MINUTE, expireTime);
		params.put("expireTime", new Timestamp(calendar.getTimeInMillis()));
		params.put("uuid", uuid);
		try {
			tokenMapper.refreshToken(params);
		} catch (Exception e) {
			logger.error("update token err   " + e.getMessage());
			throw e;
		}
	}
	
	
	public String generateToken(JSONObject paramJson) throws Exception{
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            
            for(Entry<String, Object> entry : paramJson.entrySet()){
            	 dos.write(entry.getValue().toString().getBytes());
            }
            dos.writeLong(System.currentTimeMillis());
            dos.flush();
            return md5Hex(baos.toByteArray());
        } catch (IOException e) {
        	logger.error("Failed to generate access token!!!" + e.getMessage());
        	throw e;
        }
    }
}
