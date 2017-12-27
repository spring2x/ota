package com.iot.ota_web.aop;


import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * tangliang
 * 用户登录切面
 */
@Aspect
@Component
@Order(2)
public class LoginAspect {
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${login.err.day.num}")
	private int errNum;
	
	//对用户登录进行统计的map,每个用户每天最多错误次数
	public static final ConcurrentHashMap<String, Integer> userLoginStatisticMap = new ConcurrentHashMap<>();
	
	private static Logger logger = LogManager.getLogger(LoginAspect.class);

    @Pointcut("execution(public * com.iot.ota_web.controller.UserController.userLogin(..))")
    public void valid() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("valid()")
    public Object arround(ProceedingJoinPoint pjp) {
        JSONObject resultJson = new JSONObject();
        
        try {
            Object[] objects = pjp.getArgs();
            for (Object object : objects) {
                if (object instanceof User) {
                	User user = (User)object;
                	
                	Integer todayLoginNum = userLoginStatisticMap.get(user.getName());
                	if (todayLoginNum != null && todayLoginNum >= errNum) {
                		 resultJson.put("code", "0001");
                         resultJson.put("message", "今天错误次数已达上限,请明天再登录");
                         return resultJson.toJSONString();
					}
                	
                	
                	String validCode = (String) valueOperations.get(user.getValidCode().toLowerCase());
                	if (validCode == null) {
                		 resultJson.put("code", "0001");
                         resultJson.put("message", "验证码错误");
                         return resultJson.toJSONString();
					}
                	Object result = pjp.proceed();
                	redisTemplate.delete(user.getValidCode().toLowerCase());
                	return result;
                }
            }
            
            resultJson.put("code", "0001");
            resultJson.put("message", "请求参数格式错误");
            return resultJson.toJSONString();
        } catch (Throwable e) {
            ExceptionUtil.printExceptionToLog(logger, new Exception(e));
            resultJson.put("code", "0001");
            resultJson.put("message", "服务器错误");
            return resultJson.toJSONString();
        }
    }
}
