package com.iot.oauth.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;

public class DeviceInterceptor implements HandlerInterceptor {
	
	private ValueOperations<String, Object> valueOperations;
	
	
	public DeviceInterceptor(ValueOperations<String, Object> valueOperations) {
		this.valueOperations = valueOperations;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		JSONObject result = new JSONObject();
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter writer = null;
		String uuid = request.getHeader("token") == null ? request.getParameter("token") : request.getHeader("token");
		if(StringUtils.isEmpty(uuid)) {
			writer = response.getWriter();
			result.put("code", "0001");
			result.put("message", "未授权");
			response.setStatus(401);
			writer.write(result.toJSONString());
			writer.flush();
			writer.close();
			return false;
		}
		
		try {
			String token = (String) valueOperations.get(uuid);
			if (token == null) {
				writer = response.getWriter();
				result.put("code", "0001");
				result.put("message", "未授权或授权过期");
				response.setStatus(401);
				writer.write(result.toJSONString());
				writer.flush();
				writer.close();
				return false;
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			writer = response.getWriter();
			response.setStatus(500);
			writer.write(result.toJSONString());
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
