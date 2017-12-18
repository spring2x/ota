package com.iot.ota_web.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Token;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.bean.UserProperty;
import com.iot.ota_web.mapper.TokenMapper;
import com.iot.ota_web.mapper.UserMapper;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.MD5Util;

@Service("userPasswordLoginService")
public class UserPasswordLoginService implements UserLoginServiceInterf {
	
	public Logger logger = LogManager.getLogger(UserPasswordLoginService.class.getName());
	
	@Autowired
	public UserMapper userMapper;
	@Autowired
	public TokenService tokenService;
	
	@Autowired
	UserProperty userProperty;
	@Autowired
	TokenMapper tokenMapper;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void userLogin(User user, JSONObject result) throws Exception {
		
		String md5Password = MD5Util.EncryptionStr(user.getPassword(), MD5Util.MD5);
		
		user.setPassword(md5Password);
		try {
			List<User> users = userMapper.checkUser(user);
			if (!users.isEmpty()) {
				Integer tokenId = users.get(0).getUserTokenId();
				
				int expireTime = userProperty.getTokenExpiredTime();
				Token token = null;
				if (tokenId == null) {
					token = tokenService.createAccessToken(user, expireTime);
					user.setUserTokenId(token.getId());
				}else {
					token = tokenService.updateToken(expireTime, users.get(0));
					user.setUserTokenId(tokenId);
				}
				
				userMapper.updateUser(user);
				result.put("userId", user.getUserId());
				result.put("token", token.getUuid());
				result.put("name", user.getName());
			}else {
				result.put("code", "0001");
				result.put("message", "用户名密码错误");
			}
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
	}

}
