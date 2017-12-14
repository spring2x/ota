package com.iot.oauth.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.service.BusinessPlatformService;

/**
 * 业务平台相关的控制器
 * @author liqiang
 *
 */
@Controller
@RequestMapping("/platform")
public class BusinessPlatformController extends BasicController {
	
	@Autowired
	BusinessPlatformService businessPlatformService;
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@RequestBody JSONObject params, HttpServletRequest request){
		JSONObject result = generateResult();
		String cmd = request.getParameter("cmd");
		try {
			if ("platform_authent".equals(cmd)) {
				//业务平台鉴权
				businessPlatformService.platformAuthentProcess(params, result);
				if ("0000".equals(result.getString("code"))) {
					businessPlatformService.addPlatformToken(params, result);
				}
			}else if ("update_token".equals(cmd)) {
				businessPlatformService.updatePlatformToken(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		return result.toJSONString();
	}
}
