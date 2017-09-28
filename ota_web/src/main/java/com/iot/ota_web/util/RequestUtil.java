package com.iot.ota_web.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
	
	public static Map<String, Object> getParams(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			paramMap.put(paramName, request.getParameter(paramName));
		}

		return paramMap;
	}
}
