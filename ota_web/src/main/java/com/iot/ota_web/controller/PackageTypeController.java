package com.iot.ota_web.controller;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.service.PackageService;
import com.iot.ota_web.util.RequestUtil;

/**
 * 设备终端升级包相关的控制器
 * @author liqiang
 *
 */
@Controller
@RequestMapping("/ong/package")
public class PackageTypeController extends BasicController{
	
	@Autowired
	PackageService packageService;
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
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
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.GET})
	public @ResponseBody String dealGet(HttpServletRequest request) {
		String cmd = request.getParameter("cmd");
		JSONObject result = generateResult();
		try {
			Map<String, Object> requestParams = RequestUtil.getParams(request);
			JSONObject params = new JSONObject(requestParams);
			if ("get_packages".equals(cmd)) {
				packageService.getPackageProcess(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.DELETE})
	public @ResponseBody String dealDelete(@RequestBody JSONObject params, HttpServletRequest request) {
		
		String cmd = request.getParameter("cmd");
		JSONObject result = generateResult();
		try {
			if ("delete_package".equals(cmd)) {
				packageService.deletePackageProcess(params, result);
			}
		} catch (Exception e) {
			if (e instanceof IOException) {
				result.put("code", "0001");
				result.put("message", e.getMessage());
			}else {
				result.put("code", "0001");
				result.put("message", "err");
			}
		}
		return result.toJSONString();
	}
}
