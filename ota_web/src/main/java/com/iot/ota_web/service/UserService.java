package com.iot.ota_web.service;


import java.sql.Timestamp;
import java.util.Calendar;
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
import com.iot.ota_web.mapper.TokenMapper;
import com.iot.ota_web.mapper.UserMapper;
import com.iot.ota_web.util.MD5Util;

@Service
public class UserService implements ApplicationContextAware{
	
	Logger logger = LogManager.getLogger(UserService.class.getName());
	
	private ApplicationContext applicationContext;
	
	@Autowired
	public UserMapper userMapper;
	@Autowired
	public TokenMapper tokenMapper;
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
	public void userRegistProcess(JSONObject params, JSONObject result) throws Exception{
		String phone = params.getString("phone");
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
			if (!password.matches(".*\\d+.*") || !password.matches(".*[a-zA-Z]+.*")/* || !password.matches(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*")*/) {
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
		}
	}
	
	/**
	 * 用户登录
	 * @param params
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public void userLoginProcess(JSONObject params, JSONObject result) throws Exception{
		int loginType = params.getInteger("login_type");
		UserLoginServiceInterf userLoginService = null;
		switch (loginType) {
		case 0:
			//采用密码的方式登录
			userLoginService = (UserLoginServiceInterf) applicationContext.getBean("userPasswordLoginService");
			params.put("token_type", 0);
			break;
		case 1:
			//采用验证码的方式登录
			userLoginService = (UserLoginServiceInterf) applicationContext.getBean("userVeryfyCodeLoginService");
			params.put("token_type", 0);
		}
		
		try {
			userLoginService.userLogin(params, result);
		} catch (Exception e) {
			logger.error("user login err!!!" + e.getMessage());
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void refreshUserTokenProcess(JSONObject params, JSONObject result) throws Exception {

		// 设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
		calendar.setTime(expiredDate);
		calendar.add(Calendar.MINUTE,userProperty.getTokenExpiredTime());
		params.put("expireTime", new Timestamp(calendar.getTimeInMillis()));
		try {
			tokenMapper.refreshToken(params);
		} catch (Exception e) {
			logger.error("refresh token err!!!   " + e.getMessage());
			throw e;
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void userLogoutProcess(JSONObject params, JSONObject result) throws Exception{
		try {
			userMapper.cleanUserToken(params);
		} catch (Exception e) {
			logger.error("user logout err!!!" + e.getMessage());
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
				if (params.containsKey("cur_userId") && user.getId() == params.getInteger("cur_userId")) {
					continue;
				}
				JSONObject _userJson = new JSONObject();
				_userJson.put("id", user.getId());
				_userJson.put("uname", user.getName());
				_userJson.put("phone", user.getPhone());
				_userJson.put("last_login", user.getLastLogin());
				_userJson.put("tokenId", user.getUserTokenId());
				userArray.add(_userJson);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}
	
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
