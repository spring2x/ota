package com.iot.ota_web.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Token;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.mapper.TokenMapper;
import com.iot.ota_web.util.ExceptionUtil;

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
	public Token createAccessToken(User user, int expireTime) throws Exception{
		String uuid = generateToken(user);
		
		//设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
        calendar.setTime(expiredDate);
        calendar.add(Calendar.MINUTE, expireTime);
		
        Token token = new Token(null, uuid, new Timestamp(calendar.getTimeInMillis()), 0);
		
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
	public Token updateToken(int expireTime, User user) throws Exception {
		JSONObject params = new JSONObject();
		String uuid = generateToken(user);
		// 设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
		calendar.setTime(expiredDate);
		calendar.add(Calendar.MINUTE, expireTime);
		params.put("expireTime", new Timestamp(calendar.getTimeInMillis()));
		params.put("userId", user.getUserId());
		params.put("uuid", uuid);
		try {
			tokenMapper.refreshToken(params);
			Token token = new Token(null, uuid, new Timestamp(calendar.getTimeInMillis()), 0);
			return token;
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
	}
	
	
	public String generateToken(User user) throws Exception{
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            
            dos.write(user.toString().getBytes());
            dos.writeLong(System.currentTimeMillis());
            dos.flush();
            return md5Hex(baos.toByteArray());
        } catch (IOException e) {
        	logger.error("Failed to generate access token!!!" + e.getMessage());
        	throw e;
        }
    }
}
