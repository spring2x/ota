package com.iot.ota_web.aop;


import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * tangliang
 * 用户登录切面
 */
@Aspect
@Component
@Order(2)
public class OperateAspect {
	
	
	@Value("${login.err.day.num}")
	private int errNum;
	
	//对用户登录进行统计的map,每个用户每天最多错误次数
	public static final ConcurrentHashMap<String, Integer> userLoginStatisticMap = new ConcurrentHashMap<>();
	
	private static Logger logger = LogManager.getLogger(OperateAspect.class);

    @Pointcut("execution(public * com.iot.ota_web.controller.TerminalController.*(..)) || execution(public * com.iot.ota_web.controller.PackageVersionController.*(..))"
    		+ " || execution(public * com.iot.ota_web.controller.PackageTypeController.*(..)) || execution(public * com.iot.ota_web.controller.PackageFileController.*(..))")
    public void valid() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("valid()")
	public Object arround(ProceedingJoinPoint pjp) {
		JSONObject resultJson = new JSONObject();

		try {
			Object[] objects = pjp.getArgs();
			Object userId = null;
			HttpServletRequest request = null;
			for (Object object : objects) {
				if (object instanceof HttpServletRequest) {
					request = (HttpServletRequest) object;
					userId = request.getAttribute("userId");
				}
			}
			if (userId != null) {
				for (Object object : objects) {
					if (object instanceof JSONObject) {
						JSONObject params = (JSONObject) object;
						if (!"add_terminal_user".equals(request.getParameter("cmd")) && !"update_terminal_user_competence".equals(request.getParameter("cmd"))) {
							params.put("userId", userId);
						}
					}
				}
			}
			return pjp.proceed();
		} catch (Throwable e) {
			ExceptionUtil.printExceptionToLog(logger, new Exception(e));
			resultJson.put("code", "0001");
			resultJson.put("message", "服务器错误");
			return resultJson.toJSONString();
		}
	}
}
