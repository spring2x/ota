package com.iot.ota_web.controller;


import java.io.IOException;

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
import com.iot.ota_web.service.PackageService;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * 设备终端升级包相关的控制器
 * @author tangliang
 *
 */
@Controller
@RequestMapping("/ong/package")
public class PackageTypeController extends BasicController{
	
	private static Logger logger = LogManager.getLogger(PackageTypeController.class);
	
	@Autowired
	PackageService packageService;
	
	@RequestMapping(value="", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@RequestBody JSONObject params, HttpServletRequest request ){
		String cmd = request.getParameter("cmd");
		JSONObject result = generateResult();
		try {
			if ("add_package".equals(cmd)) {
				packageService.addPackageProcess(params, result);
			}else if ("update_package".equals(cmd)) {
				packageService.updatePackageProcess(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		
		return result.toJSONString();
	}
	
	@RequestMapping(value="/get_packages", method={RequestMethod.POST})
	public @ResponseBody String packages(@RequestBody JSONObject params, HttpServletRequest request) {
		JSONObject result = generateResult();
		try {
			packageService.getPackageProcess(params, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="/delete_package", method={RequestMethod.POST})
	public @ResponseBody String deletePackage(@RequestBody JSONObject params, HttpServletRequest request) {
		
		JSONObject result = generateResult();
		try {
			packageService.deletePackageProcess(params, result);
		} catch (Exception e) {
			if (e instanceof IOException) {
				result.put("code", "0001");
				result.put("message", e.getMessage());
			}else {
				result.put("code", "0001");
				result.put("message", "服务器错误");
			}
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		return result.toJSONString();
	}
}
