package com.iot.ota_web.controller;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.service.FileCacheService;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * 升级包文件下发到具体的升级服务器
 * @author tangliang
 *
 */
@RestController
@RequestMapping("/fileIssued")
public class PackageFileIssuedController {
	
	private static Logger logger = LogManager.getLogger(PackageFileIssuedController.class);
	
	@Autowired
	TerminalProperty terminalProperty;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	FileCacheService fileCacheService;
	
	@Value("${file.redis.cache.expire}")
	private Integer fileDataCacheTime;

	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public String dealIssuedRequest(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response){
		JSONObject result = new JSONObject();
		result.put("code", "0000");
		String fileMark = params.getString("fileMark");
		
		logger.info("request to init file-[" + fileMark + "] to redis");
		if (!redisTemplate.hasKey(fileMark)) {
			logger.info("request file-[" + fileMark +"] base info is not exits in redis   check if it exits in disk!!!");
			result.put("code", "0001");
			result.put("message", "file-[" + fileMark + "] can not be init to redis, file base info is not exits in redis!!!");
			return result.toJSONString();
		}
		
		String filePath = (String) redisTemplate.opsForHash().get(fileMark, "filePath");
		File file = new File(filePath);
		if (!file.exists()) {
			logger.info("request file-[" + fileMark +"] is not exits in disk");
			result.put("code", "0001");
			result.put("message", "file-[" + fileMark + "] can not be init to redis, file is not exits in disk!!!");
			return result.toJSONString();
		}
		String key = fileMark + "_data";
		try {
			//redisTemplate.expire(key, fileDataCacheTime, TimeUnit.MINUTES);
			
			fileCacheService.putFileToRedisCache(key, file, FileCacheService.FILE_SPLIT_NUM_TO_REDIS);
			redisTemplate.opsForHash().getOperations().expire(key, fileDataCacheTime, TimeUnit.MINUTES);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "file-[" + fileMark + "] can not be init to redis, exception catched!!!");
			return result.toJSONString();
		}
		return result.toJSONString();
	}
	
}
