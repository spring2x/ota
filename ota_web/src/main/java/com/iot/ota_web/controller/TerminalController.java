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
import com.iot.ota_web.service.TerminalService;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * 设备终端相关的控制器
 * @author liqiang
 *
 */
@Controller
@RequestMapping("/ong/terminal")
public class TerminalController extends BasicController{
	
	@Autowired
	public TerminalService terminalService;
	
	private static Logger logger = LogManager.getLogger(TerminalController.class);
	
	@RequestMapping(value="", method={RequestMethod.POST})
	public @ResponseBody String dealPost(@RequestBody JSONObject params, HttpServletRequest request ){
		String cmd = request.getParameter("cmd");
		JSONObject result = generateResult();
		try {
			if ("add_terminal".equals(cmd)) {
				//添加终端
				terminalService.addTerminalTypeProcess(params, result);
			}else if ("add_terminal_user".equals(cmd)) {
				//添加终端的使用用户
				terminalService.addTerminalUser(params, result);
			}else if ("update_terminal_user_competence".equals(cmd)) {
				//更新终端用户的权限
				terminalService.updateTerminalUserCompetence(params, result);
			}else if("update_terminal_info".equals(cmd)){
				terminalService.updateTerminalInfo(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		
		return result.toJSONString();
	}
	
	/*@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.GET})
	public @ResponseBody String dealGet(HttpServletRequest request) {
		String cmd = request.getParameter("cmd");
		JSONObject result = generateResult();
		try {
			Map<String, Object> params = RequestUtil.getParams(request);
			if ("get_terminals".equals(cmd)) {
				terminalService.getTerminalsProcess(params, result);
			}else if ("get_terminal_competence".equals(cmd)) {
				terminalService.getTerminalCompetenceProcess(result);
			}else if ("get_user_terminal_competence".equals(cmd)) {
				terminalService.getTerminalsProcess(params, result);
			}
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "err");
		}
		return result.toJSONString();
	}*/
	
	
	@RequestMapping(value="/get_terminals", method={RequestMethod.POST})
	public @ResponseBody String terminals(@RequestBody JSONObject params, HttpServletRequest request){
		JSONObject result = generateResult();
		try {
			terminalService.getTerminalsProcess(params, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="/get_terminal_competence", method={RequestMethod.POST})
	public @ResponseBody String terminalCompetence(HttpServletRequest request){
		JSONObject result = generateResult();
		try {
			terminalService.getTerminalCompetenceProcess(result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="/get_user_terminal_competence", method={RequestMethod.POST})
	public @ResponseBody String userTerminalCompetence(@RequestBody JSONObject params, HttpServletRequest request){
		JSONObject result = generateResult();
		try {
			params.remove("userId");
			terminalService.getTerminalsProcess(params, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
			ExceptionUtil.printExceptionToLog(logger, e);
		}
		return result.toJSONString();
	}
	
	
	
	
	@RequestMapping(value="/deleteTerminal", method={RequestMethod.POST})
	public @ResponseBody String deleteTerminal(@RequestBody JSONObject params, HttpServletRequest request) {
		JSONObject result = generateResult();
		try {
			terminalService.deleteTerminalProcess(params, result);
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
