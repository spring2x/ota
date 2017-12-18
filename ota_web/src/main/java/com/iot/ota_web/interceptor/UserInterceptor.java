package com.iot.ota_web.interceptor;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Token;
import com.iot.ota_web.mapper.TokenMapper;
import com.iot.ota_web.service.UserService;

public class UserInterceptor implements HandlerInterceptor {
	
	TokenMapper tokenMapper;
	
	UserService userService;
	
	
	public UserInterceptor(TokenMapper tokenMapper, UserService userService) {
		this.tokenMapper = tokenMapper;
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		JSONObject result = new JSONObject();
		String token = request.getHeader("uuid") == null ? request.getParameter("uuid") : request.getHeader("uuid");
		if (token == null || "".equals(token)) {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			response.setStatus(401);
			result.put("code", "0001");
			result.put("message", "token不能为空");
			writer.write(result.toString());
			writer.flush();
			writer.close();
			return false;
		}
		JSONObject param = new JSONObject();
		param.put("uuid", request.getHeader("uuid") == null ? request.getParameter("uuid") : request.getHeader("uuid"));
		List<Token> tokens = tokenMapper.getTokens(param);
		
		if (tokens == null || tokens.isEmpty()) {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			response.setStatus(401);
			result.put("code", "0001");
			result.put("message", "token无效");
			writer.write(result.toString());
			writer.flush();
			writer.close();
			return false;
		}
		
		Token token1  = tokens.get(0);
		Timestamp expireTime = token1.getExpireTime();
		if (expireTime.getTime() < System.currentTimeMillis()) {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			response.setStatus(401);
			result.put("code", "0001");
			result.put("message", "用户授权过期，请重新登录");
			writer.write(result.toString());
			writer.flush();
			writer.close();
			return false;
		}
		
		JSONObject params = new JSONObject();
		params.put("org_token", param.get("uuid"));
		try {
			userService.refreshUserTokenProcess(params, result);
		} catch (Exception e) {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			result.put("code", "0001");
			result.put("message", "服务器错误");
			writer.write(result.toString());
			writer.flush();
			writer.close();
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
