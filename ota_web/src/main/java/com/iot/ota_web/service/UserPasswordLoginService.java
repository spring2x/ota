package com.iot.ota_web.service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.aop.LoginAspect;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.bean.UserProperty;
import com.iot.ota_web.mapper.UserMapper;
import com.iot.ota_web.util.MD5Util;
import com.iot.ota_web.util.RSAUtil;
import com.iot.ota_web.util.TokenUtil;

@Service("userPasswordLoginService")
public class UserPasswordLoginService implements UserLoginServiceInterf {
	
	public Logger logger = LogManager.getLogger(UserPasswordLoginService.class.getName());
	
	@Autowired
	public UserMapper userMapper;
	@Autowired
	UserProperty userProperty;
	
	@Value("${login.err.day.num}")
	private int errNum;
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void userLogin(User user, JSONObject result) throws Exception {
		
		//获取缓存的密钥对
        String privateKeyStr = valueOperations.get(user.getPrivateKeyId()).toString();
        RSAPrivateKey rsaPrivateKey = JSONObject.toJavaObject(JSONObject.parseObject(privateKeyStr), RSAPrivateKey.class);
        //解密
        String de_password = RSAUtil.decryptStringByJs(rsaPrivateKey,user.getPassword());
        
        if (de_password == null) {
			userLoginErrProcess(user, result);
			return;
		}
		
		String md5Password = MD5Util.EncryptionStr(de_password, MD5Util.MD5);
		
		user.setPassword(md5Password);
		try {
			List<User> users = userMapper.checkUser(user);
			if (!users.isEmpty()) {
				
				//token生成需要的有效载荷
				JSONObject payload = new JSONObject();
				payload.put("lastLogin", users.get(0).getLastLogin() == null ? new Date() : users.get(0).getLastLogin());
				payload.put("userId", users.get(0).getUserId());
				//创建token
				String token = TokenUtil.createToken(payload, userProperty.getTokenExpiredTime() * 60 * 1000, user);
				result.put("userId", users.get(0).getUserId());
				result.put("token", token);
				result.put("name", user.getName());
			}else {
				userLoginErrProcess(user, result);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void userLoginErrProcess(User user, JSONObject result){
		if (LoginAspect.userLoginStatisticMap.containsKey(user.getName())) {
			LoginAspect.userLoginStatisticMap.put(user.getName(), LoginAspect.userLoginStatisticMap.get(user.getName()) + 1);
		}else {
			LoginAspect.userLoginStatisticMap.put(user.getName(), 1);
		}
		result.put("code", "0001");
		int todayErrNum = LoginAspect.userLoginStatisticMap.get(user.getName());
		if (todayErrNum == errNum) {
			result.put("message", "今天错误次数已达上限,请明天再登录");
		}else {
			result.put("message", "用户名密码错误,今日已错误" + todayErrNum + "次,今日剩余错误次数-[" + (errNum - todayErrNum + "次]"));
		}
	}
}
