package com.iot.ota_web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.service.CommonService;
import com.iot.ota_web.service.UserService;
import com.iot.ota_web.util.RequestUtil;

@Controller
@RequestMapping("/ong/user")
public class UserController extends BasicController {
	
	Logger logger = LogManager.getLogger(UserController.class.getName());
	
	
	@Autowired
	public UserService userService;
	@Autowired
	public CommonService commonService;
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@RequestBody JSONObject params, HttpServletRequest request){
		JSONObject result = generateResult();
		String cmd = request.getParameter("cmd");
		try {
			if ("add_user".equals(cmd)) {
				//用户注册
				userService.userRegistProcess(params, result);
			}else if ("user_login".equals(cmd)) {
				userService.userLoginProcess(params, result);
			}else if ("refresh_user_token".equals(cmd)){
				userService.refreshUserTokenProcess(params, result);
			}else if ("user_logout".equals(cmd)) {
				userService.userLogoutProcess(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.GET})
	public @ResponseBody String dealGet(HttpServletRequest request){
		JSONObject result = generateResult();
		try {
			Map<String, Object> params = RequestUtil.getParams(request);
			String cmd = (String) params.get("cmd");
			if ("get_verifyCode".equals(cmd)) {
				//用户获取手机验证码
				commonService.getVerifyCode(params, result);
			}else if ("get_users".equals(cmd)) {
				userService.getUserList(new JSONObject(params), result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
			logger.error(e.toString());
		}
		
		return result.toJSONString();
	}
	
}
