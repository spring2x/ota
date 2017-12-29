package com.iot.ota_web.interceptor;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.service.UserService;
import com.iot.ota_web.util.TokenUtil;
import io.jsonwebtoken.Claims;

public class UserInterceptor implements HandlerInterceptor {
	
	UserService userService;
	
	
	public UserInterceptor( UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		JSONObject result = new JSONObject();
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter writer = null;
		
		final String authHeader = request.getHeader("Authorization");
		if(StringUtils.isEmpty(authHeader)) {
			writer = response.getWriter();
			result.put("code", "0001");
			result.put("message", "未授权");
			response.setStatus(401);
			writer.write(result.toJSONString());
			writer.flush();
			writer.close();
			return false;
		}
		Claims claims = null;
		try {
			claims = TokenUtil.parseJWT(authHeader, result);
			if (claims == null) {
				response.setStatus(401);
				if (StringUtils.endsWithIgnoreCase(request.getRequestURI(), "logout") && TokenUtil.TOKEN_EXPIRED_MESSAGE.equals(result.get("message"))) {
					result.put("code", "0000");
					result.put("message", "success");
					response.setStatus(200);
				}
				writer = response.getWriter();
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
		
		/*try {
			userService.refreshUserTokenProcess(authHeader, result);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "服务器错误");
			writer.write(result.toString());
			writer.flush();
			writer.close();
			return false;
		}*/
		
		request.setAttribute("userId", claims.get("userId"));
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setContentType("application/json");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}
}
