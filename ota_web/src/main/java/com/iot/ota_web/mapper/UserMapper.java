package com.iot.ota_web.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;

public interface UserMapper {
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	void addUser(User user);
	
	/**
	 * 获取用户
	 * @param params
	 * @return
	 */
	List<User> getUsers(JSONObject params);
	
	/**
	 * 获取用户
	 * @param user
	 * @return
	 */
	List<User> existUser(User user);
	
	/**
	 * 验证当前用户是否存在
	 * @param user
	 * @return
	 */
	List<User> checkUser(User user);
	
	/**
	 * 更新用户信息.
	 * @param user
	 */
	void updateUser(User user);
	
	/**
	 * 更新用户过期的token id为null.
	 */
	void cleanUserToken(JSONObject params);
	
}
