package com.iot.ota_web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.service.CommonService;
import com.iot.ota_web.service.UserService;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.TokenUtil;
import com.iot.ota_web.valid.group.ValidGroup1;
import com.iot.ota_web.valid.group.ValidGroup2;

import io.jsonwebtoken.Claims;

@Controller
@RequestMapping("/ota/user")
public class UserController extends BasicController {
	
	Logger logger = LogManager.getLogger(UserController.class.getName());
	
	
	@Autowired
	public UserService userService;
	@Autowired
	public CommonService commonService;
	
	/**
	 * 用户注册
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value="/registor", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String userRegistor(@Validated(value={ValidGroup1.class}) @RequestBody User user, BindingResult bindingResult){
		JSONObject result = generateResult();
		try {
			userService.userRegistProcess(user, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		
		return result.toJSONString();
	}
	
	/**
	 * 登录
	 * @param user
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value="/login", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String userLogin(@Validated(value={ValidGroup2.class}) @RequestBody User user, BindingResult bindingResult){
		JSONObject result = generateResult();
		try {
			userService.userLoginProcess(user, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		
		return result.toJSONString();
	}
	
	/**
	 * 刷新token
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/refreshToken", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String refreshUserToken(@RequestHeader("Authorization") String token){
		JSONObject result = generateResult();
		
		try {
			userService.refreshUserTokenProcess(token, result);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "服务器错误");
		}
		
		return result.toJSONString();
	}
	
	/**
	 * 用户登出
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/logout", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String userLogout(@RequestHeader("Authorization") String token){
		JSONObject result = generateResult();
		
		try {
			userService.userLogoutProcess(token, result);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "服务器错误");
		}
		
		return result.toJSONString();
	}
	
	/**
	 * 获取用户列表
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/getUsers", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String getUsers(@RequestHeader("Authorization") String token){
		JSONObject result = generateResult();
		JSONObject params = new JSONObject();
		try {
			Claims claims = TokenUtil.parseJWT(token, result);
			if (claims == null) {
				return result.toJSONString();
			}
			params.put("cur_userId", claims.get("userId"));
			userService.getUserList(params, result);
		}catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
		}
		
		return result.toJSONString();
	}
	
	
	
	/*@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@Valid @RequestBody User user, BindingResult bindingResult, HttpServletRequest request){
		if (bindingResult.hasErrors()) {
			return bindingResult.getFieldError().getDefaultMessage();
		}
		
		
		JSONObject result = generateResult();
		String cmd = request.getParameter("cmd");
		JSONObject params = new JSONObject();
		try {
			if ("add_user".equals(cmd)) {
				//用户注册
				userService.userRegistProcess(user, result);
			}else if ("user_login".equals(cmd)) {
				userService.userLoginProcess(user, result);
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
	}*/
	
	/*@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.GET})
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
	}*/
	
}
