package com.iot.oauth.controller;


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
import com.iot.oauth.bean.PlatformProperty;
import com.iot.oauth.service.DeviceTokenService;
import com.iot.oauth.util.RequestUtil;

/**
 * 与设备交互相关的控制器
 * @author liqiang
 *
 */
@Controller
@RequestMapping("/ong/device")
public class DeviceController extends BasicController{
	
	Logger log = LogManager.getLogger(DeviceController.class.getName());
	
	@Autowired
	DeviceTokenService deviceTokenService;
	
	@Autowired
	PlatformProperty platformProperty;
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@RequestBody JSONObject params, HttpServletRequest request){
		JSONObject result = generateResult();
		String cmd = request.getParameter("cmd");
		try {
			if ("device_authent".equals(cmd)) {
				deviceTokenService.addDeviceToken(params, result);
			}else if ("update_device_token".equals(cmd)) {
				deviceTokenService.updateDeviceToken(params, result);
			}
			
			String serverIp = platformProperty.getOtaServerIp();
			int serverPort = platformProperty.getOtaServerPort();
			result.put("serverIp", serverIp);
			result.put("serverPort", serverPort);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.GET})
	public @ResponseBody String dealGet(HttpServletRequest request){
		JSONObject result = generateResult();
		String cmd = request.getParameter("cmd");
		try {
			Map<String, Object> params = RequestUtil.getParams(request);
			if ("new_version".equals(cmd)) {
				deviceTokenService.getNewVerson(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
			log.debug(e);
		}
		return result.toJSONString();
	}
}
