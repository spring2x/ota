package com.iot.ota_web.controller;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iot.ota_web.service.ValidCodeService;
import com.iot.ota_web.util.ExceptionUtil;

@RestController
@RequestMapping("/ota/validCode")
public class ValidateCodeController {
	
	private static Logger logger = LogManager.getLogger(ValidateCodeController.class);
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
	@Value("${valid.code.expire}")
	private long expireTime;

	@RequestMapping(value="/get", method={RequestMethod.GET})
	public String getValidCode(HttpServletRequest request, HttpServletResponse response){
		 // 设置响应的类型格式为图片格式  
	    response.setContentType("image/jpeg");
	    //禁止图像缓存.
	    response.setHeader("Pragma", "no-cache");
	    response.setHeader("Cache-Control", "no-cache");
	    response.setDateHeader("Expires", 0);
	  
	  
	    ValidCodeService vCode = new ValidCodeService(120,40,5,100);
		valueOperations.set(vCode.getCode().toLowerCase(), vCode.getCode(), expireTime, TimeUnit.MINUTES);
	    try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			ExceptionUtil.printExceptionToLog(logger, e);
		}
	    return null;
	}
	
	
	/**
	 * 生成验证码
	 * @param n	验证码长度
	 * @return
	 */
	public static String initValidCode(int n) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			String str = random.nextInt(2) % 2 == 0 ? "num" : "char";
			if ("char".equalsIgnoreCase(str)) { // 产生字母
				int nextInt = random.nextInt(2) % 2 == 0 ? 65 : 97;
				// System.out.println(nextInt + "!!!!"); 1,0,1,1,1,0,0
				val += (char) (nextInt + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(str)) { // 产生数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
}
