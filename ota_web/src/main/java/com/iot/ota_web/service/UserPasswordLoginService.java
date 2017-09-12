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
import com.iot.ota_web.mapper.UserMapper;
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
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void userLogin(JSONObject params, JSONObject result) throws Exception {
		
		String md5Password = MD5Util.EncryptionStr(params.getString("password"), MD5Util.MD5);
		
		params.put("password", md5Password);
		try {
			List<User> users = userMapper.getUsers(params);
			if (!users.isEmpty()) {
				int expireTime = userProperty.getTokenExpiredTime();
				Token token = tokenService.createAccessToken(params, expireTime);
				User user = users.get(0);
				user.setUserTokenId(token.getId());
				userMapper.updateUser(user);
				result.put("userId", user.getId());
				result.put("token", token.getUuid());
				result.put("name", user.getName());
			}else {
				result.put("code", "0001");
				result.put("message", "用户名密码错误");
			}
		} catch (Exception e) {
			logger.error("user login by password err!!!" + e.getMessage());
			throw e;
		}
	}

}
