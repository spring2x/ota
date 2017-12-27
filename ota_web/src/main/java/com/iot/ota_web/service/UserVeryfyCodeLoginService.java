package com.iot.ota_web.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.bean.UserProperty;
import com.iot.ota_web.mapper.UserMapper;

@Service("userVeryfyCodeLoginService")
public class UserVeryfyCodeLoginService implements UserLoginServiceInterf {
	@Autowired
	public UserMapper userMapper;
	@Autowired
	UserProperty userProperty;
	
	Logger logger = LogManager.getLogger(UserVeryfyCodeLoginService.class.getName());
	
	@Override
	public void userLogin(User user, JSONObject result) throws Exception {
		
		/*String verifyCode = params.getString("code");
		String phone = params.getString("phone");
		//验证码校验
		if (!CommonService.registerVerifyMap.containsKey(phone) || !CommonService.registerVerifyMap.get(phone).equals(verifyCode)) {
			result.put("code", "0001");
			result.put("message", "验证码错误");
			return;
		}
		
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
				result.put("message", "用户未注册");
			}
		} catch (Exception e) {
			logger.error("user login by verify cod err!!!" + e.getMessage());
			throw e;
		}*/
	}

}
