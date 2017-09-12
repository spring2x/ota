package com.iot.ota_web.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;

public interface UserMapper {
	
	/**
	 * 添加用户
	 * @param params
	 * @return
	 */
	void addUser(JSONObject params);
	
	/**
	 * 获取用户
	 * @param params
	 * @return
	 */
	List<User> getUsers(JSONObject params);
	
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
