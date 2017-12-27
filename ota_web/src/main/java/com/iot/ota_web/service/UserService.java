package com.iot.ota_web.service;


import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.bean.UserProperty;
import com.iot.ota_web.mapper.UserMapper;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.MD5Util;
import com.iot.ota_web.util.TokenUtil;

import io.jsonwebtoken.Claims;

@Service
public class UserService implements ApplicationContextAware{
	
	Logger logger = LogManager.getLogger(UserService.class.getName());
	
	private ApplicationContext applicationContext;
	
	@Autowired
	public UserMapper userMapper;
	@Autowired
	UserProperty userProperty;
	
	/**
	 * 平台用户注册
	 * @param params
	 * @param result
	 * @param verifyCodeMap 保存验证码的map
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void userRegistProcess(User user, JSONObject result) throws Exception{
		/*String phone = params.getString("phone");
		String verifyCode = params.getString("code");
		
		//验证码校验
		if (!CommonService.registerVerifyMap.containsKey(phone) || !CommonService.registerVerifyMap.get(phone).equals(verifyCode)) {
			result.put("code", "0001");
			result.put("message", "验证码错误");
			return;
		}
		
		CommonService.registerVerifyMap.remove(phone);
		
		try {
			String password = params.getString("password");
			if (!password.matches(".*\\d+.*") || !password.matches(".*[a-zA-Z]+.*") || !password.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*")) {
				result.put("code", "0001");
				result.put("message", "密码必须包含数字和字母");
				return;
			}
			
			if (password.length() <8) {
				result.put("code", "0001");
				result.put("message", "密码长度必须大于等于8位");
				return;
			}
			
			String md5Password = MD5Util.EncryptionStr(password, MD5Util.MD5);
			params.put("password", md5Password);
			List<User> users = userMapper.getUsers(params);
			
			if (users != null && !users.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "手机号已经被注册");
				return;
			}
			
			userMapper.addUser(params);
			result.put("userId", params.get("type_id"));
		} catch (Exception e) {
			logger.error("add user err!!!   " + e.getMessage());
			throw e;
		}*/
		if (!user.getPassword().equals(user.getRepeatPassword())) {
			result.put("code", "0001");
			result.put("message", "两次输入的密码不一致");
			return;
		}
		
		String md5Password = MD5Util.EncryptionStr(user.getPassword(), MD5Util.MD5);
		user.setPassword(md5Password);
		
		try {
			List<User> users = userMapper.existUser(user);
			
			if (users != null && !users.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "该用户已经注册");
				return;
			}
			
			userMapper.addUser(user);
			result.put("userId", user.getUserId());
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 用户登录
	 * @param params
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public void userLoginProcess(User user, JSONObject result) throws Exception{
		int loginType = user.getLogin_type() == null ? 0 : user.getLogin_type();
		UserLoginServiceInterf userLoginService = null;
		switch (loginType) {
		case 0:
			//采用密码的方式登录
			userLoginService = (UserLoginServiceInterf) applicationContext.getBean("userPasswordLoginService");
			break;
		case 1:
			//采用验证码的方式登录
			userLoginService = (UserLoginServiceInterf) applicationContext.getBean("userVeryfyCodeLoginService");
		}
		
		try {
			userLoginService.userLogin(user, result);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void refreshUserTokenProcess(String orgToken, JSONObject result) throws Exception {
		Claims claims = TokenUtil.parseJWT(orgToken, result);
		if (claims == null) {
			return;
		}
		String userName = claims.getSubject();
		String uuid = claims.getId();
		User user = new User();
		user.setName(userName);
		
		try {
			List<User> users = userMapper.existUser(user);
			if (users != null && !users.isEmpty()) {
				user = users.get(0);
				JSONObject payload = new JSONObject();
				payload.put("lastLogin", user.getLastLogin() == null ? new Date() : user.getLastLogin());
				payload.put("userId", user.getUserId());
				//创建token
				TokenUtil.TOKEN_MAP.remove(uuid);
				String token = TokenUtil.createToken(payload, userProperty.getTokenExpiredTime() * 60 * 1000, user);
				result.put("token", token);
			}else {
				result.put("code", "0001");
				result.put("message", "未找到该用户");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void userLogoutProcess(String token, JSONObject result) throws Exception{
		try {
			Claims claims = TokenUtil.parseJWT(token, result);
			if (claims == null) {
				if (TokenUtil.TOKEN_EXPIRED_MESSAGE.equals(result.get("message"))) {
					result.put("code", "0000");
					result.put("message", "success");
				}
				return;
			}
			String uuid = claims.getId();
			TokenUtil.TOKEN_MAP.remove(uuid);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly=true)
	public void getUserList(JSONObject params, JSONObject result) throws Exception{
		try {
			List<User> users = userMapper.getUsers(params);
			JSONObject usersJson = new JSONObject();
			result.put("users", usersJson);
			if (users == null || users.isEmpty()) {
				return;
			}
			JSONArray userArray = new JSONArray();
			usersJson.put("userArray", userArray);
			for(User user : users){
				if (user.getUserId() == params.getInteger("cur_userId")) {
					continue;
				}
				JSONObject _userJson = new JSONObject();
				_userJson.put("id", user.getUserId());
				_userJson.put("uname", user.getName());
				_userJson.put("last_login", user.getLastLogin());
				userArray.add(_userJson);
			}
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
