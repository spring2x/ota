package com.iot.ota_web.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Terminal;
import com.iot.ota_web.bean.TerminalCompenence;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.mapper.PackageFileMapper;
import com.iot.ota_web.mapper.TerminalCompenenceMapper;
import com.iot.ota_web.mapper.TerminalMapper;
import com.iot.ota_web.mapper.UserTerminalCompetenceMapper;
import com.iot.ota_web.util.FileUtil;

/**
 * 设备终端相关的业务
 * @author liqiang
 *
 */
@Service
public class TerminalService {
	
	Logger logger = LogManager.getLogger(TerminalService.class.getName());
	
	@Autowired
	public TerminalMapper terminalMapper;
	@Autowired
	public UserTerminalCompetenceMapper competenceMapper;
	@Autowired
	public TerminalCompenenceMapper terminalCompenenceMapper;
	@Autowired
	PackageFileMapper packageFileMapper;
	
	@Autowired
	TerminalProperty terminalProperty;
	
	
	/**
	 * 添加终端类型，谁添加，谁就是超级管理员
	 * @param parmas
	 * @param result
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addTerminalTypeProcess(JSONObject params, JSONObject result) throws Exception{
		//参数校验
		if (!params.containsKey("type") || !params.containsKey("businessPlatformUrl")
				|| params.get("type") == null || "".equals(params.get("type"))
				|| params.get("businessPlatformUrl") == null || "".equals(params.get("businessPlatformUrl"))
				|| !params.containsKey("userId")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		
		//对终端的业务平台url进行md5加密
		//params.put("businessPlatformUrl", MD5Util.EncryptionStr(params.getString("businessPlatformUrl"), MD5Util.MD5));
		//设置用户对终端的权限为最高权限(0代表最高权限——超级管理员)
		params.put("competence_type", 0);
		
		List<Terminal> terminals = null;
		try {
			terminals = terminalMapper.getTerminalsByTypeOrUrl(params);
			if (terminals != null && !terminals.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "已有的终端类型，请联系终端管理员");
				return;
			}
			
			terminalMapper.addTerminal(params);
			competenceMapper.addUserCompetence(params);
			FileUtil.createFolder(params.get("terminal_id").toString(), terminalProperty.getUpgradePackagePath());
			result.put("terminalId", params.get("terminal_id"));
		} catch (Exception e) {
			logger.error("add terminals err!!!" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 获取终端以及人员信息
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public void getTerminalsProcess(Map<String, Object> params, JSONObject result) throws Exception{
		List<Map<String, Object>> terminalList = null;
		try {
			terminalList = terminalMapper.getTerminals(params);
			JSONObject terminalJson = new JSONObject();
			JSONArray terminalJsonArray = new JSONArray();
			terminalJson.put("terminalArray", terminalJsonArray);
			result.put("terminals", terminalJson);
			
			if (terminalList != null && !terminalList.isEmpty()) {
				for(Map<String, Object> map : terminalList){
					JSONObject terminal = new JSONObject();
					terminal.putAll(map);
					terminalJsonArray.add(terminal);
				}
			}
		} catch (Exception e) {
			logger.error("get terminals error!!!" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 获取终端的权限列表
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public void getTerminalCompetenceProcess(Map<String, Object> params, JSONObject result) throws Exception{
		List<TerminalCompenence> compenenceList = null;
		try {
			compenenceList = terminalCompenenceMapper.geTerminalCompenences(params);
			JSONObject compenenceJson = new JSONObject();
			JSONArray compenenceArray = new JSONArray();
			compenenceJson.put("compenenceArray", compenenceArray);
			result.put("compenence", compenenceJson);
			
			if (compenenceList != null && !compenenceList.isEmpty()) {
				for(TerminalCompenence terminalCompenence : compenenceList){
					JSONObject compenence = new JSONObject();
					compenence.put("id", terminalCompenence.getId());
					compenence.put("type", terminalCompenence.getType());
					compenence.put("name", terminalCompenence.getName());
					compenence.put("introduce", terminalCompenence.getIntroduce());
					compenenceArray.add(compenence);
				}
			}
		} catch (Exception e) {
			logger.error("get compenences error!!!" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 添加终端用户
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addTerminalUser(JSONObject params, JSONObject result) throws Exception{
		
		//1、参数校验：userId当前添加的用户id,terminal_id终端类型id,cur_userId当前操作的用户id
		if (!params.containsKey("userId") || !params.containsKey("terminal_id") || !params.containsKey("cur_userId") || ! params.containsKey("competence_type")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		if (params.getInteger("competence_type").intValue() == 0) {
			result.put("code", "0001");
			result.put("message", "超级管理员只能有一个");
			return;
		}
		
		//2、获取当前操作用户的终端权限
		JSONObject competenceParams = new JSONObject();
		competenceParams.put("userId", params.get("cur_userId"));
		competenceParams.put("terminal_id", params.get("terminal_id"));
		List<Map<String, Object>> userCompetences = competenceMapper.getUserCompetence(competenceParams);
		if (userCompetences == null || userCompetences.isEmpty()) {
			//用户没有任何终端权限
			result.put("code", "0001");
			result.put("message", "没有权限添加终端用户");
			return;
		}
		Map<String, Object> userCompetence = userCompetences.get(0);
		String userCompetenceType = userCompetence.get("competence_type").toString();
		if (!"0".equals(userCompetenceType)) {
			//用户不是最高权限
			result.clear();
			result.put("code", "0001");
			result.put("message", "没有权限添加终端用户");
			return;
		}
		
		List<Map<String, Object>> userTerminalCompetences = null;
		try {
			userTerminalCompetences = competenceMapper.getUserCompetence(params);
			if (userTerminalCompetences != null && !userTerminalCompetences.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "该用户已有权限");
				return;
			}
			competenceMapper.addUserCompetence(params);
		} catch (Exception e) {
			logger.error("add terminal user err" + e.getMessage());
			throw e;
		}
	}
	
	
	/**
	 * 更新终端用户的权限。
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updateTerminalUserCompetence(JSONObject params, JSONObject result) throws Exception {

		// 1、参数校验：userId当前添加的用户id,terminal_id终端类型id,cur_userId当前操作的用户id
		if (!params.containsKey("userId") || !params.containsKey("terminal_id") || !params.containsKey("cur_userId")
				|| !params.containsKey("competence_type")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		if (params.getInteger("competence_type").intValue() == 0) {
			result.put("code", "0001");
			result.put("message", "超级管理员只能有一个");
			return;
		}
		// 2、获取当前操作用户的终端权限
		JSONObject competenceParams = new JSONObject();
		competenceParams.put("userId", params.get("cur_userId"));
		competenceParams.put("terminal_id", params.get("terminal_id"));
		List<Map<String, Object>> userCompetences = competenceMapper.getUserCompetence(competenceParams);
		if (userCompetences == null || userCompetences.isEmpty()) {
			// 用户没有任何终端权限
			result.put("code", "0001");
			result.put("message", "没有权限修改终端用户");
			return;
		}
		Map<String, Object> userCompetence = userCompetences.get(0);
		String userCompetenceType = userCompetence.get("competence_type").toString();
		if (!"0".equals(userCompetenceType)) {
			// 用户不是最高权限
			result.put("code", "0001");
			result.put("message", "没有权限修改终端用户");
			return;
		}
		
		
		if (params.getInteger("userId").intValue() == params.getInteger("cur_userId").intValue()) {
			// 超级管理员不能修改自己的权限
			result.put("code", "0001");
			result.put("message", "超级管理员的权限不能被修改");
			return;
		}
		List<Map<String, Object>> userTerminalCompetences = null;
		try {
			userTerminalCompetences = competenceMapper.getUserCompetence(params);
			if (userTerminalCompetences == null || userTerminalCompetences.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "该用户还没有任何权限");
				return;
			}
			competenceMapper.updateUserCompetence(params);
		} catch (Exception e) {
			logger.error("add terminal user err" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 更新终端类型的信息
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	public void updateTerminalInfo(JSONObject params, JSONObject result) throws Exception {
		// 1、参数校验：userId当前操作的用户id,terminal_id终端类型id
		if (!params.containsKey("userId") || !params.containsKey("terminal_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}

		// 2、获取当前操作用户的终端权限
		List<Map<String, Object>> userCompetences = competenceMapper.getUserCompetence(params);
		if (userCompetences == null || userCompetences.isEmpty()) {
			// 用户没有任何终端权限
			result.put("code", "0001");
			result.put("message", "没有权限修改终端");
			return;
		}
		Map<String, Object> userCompetence = userCompetences.get(0);
		String userCompetenceType = userCompetence.get("competence_type").toString();
		if (!"0".equals(userCompetenceType)) {
			// 用户不是最高权限
			result.put("code", "0001");
			result.put("message", "没有权限修改终端");
			return;
		}
		
		/*if (params.containsKey("businessPlatformUrl")) {
			params.put("businessPlatformUrl", MD5Util.EncryptionStr(params.getString("businessPlatformUrl"), MD5Util.MD5));
		}*/
		
		try {
			
			if (params.containsKey("type") || params.containsKey("businessPlatformUrl")) {
				List<Terminal> terminals = terminalMapper.getTerminalsByTypeOrUrl(params);
				if (terminals != null && !terminals.isEmpty()) {
					Terminal terminal = terminals.get(0);
					if (terminal.getId().intValue() != params.getInteger("terminal_id").intValue()) {
						result.put("code", "0001");
						result.put("message", "终端类型或业务平台地址已经被使用");
						return;
					}
				}
			}
			
			terminalMapper.updateTerminal(params);
		} catch (Exception e) {
			logger.error("update terminal info err!!!   " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 删除终端类型，同时删除终端类型下的所有数据，包括升级包信息，版本信息(数据库级联删除)，升级文件
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteTerminalProcess(JSONObject params, JSONObject result) throws Exception{
		//1、参数检查。
		if (!params.containsKey("userId") || !params.containsKey("terminal_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		try {
			//2、获取用户的终端权限
			List<Map<String, Object>> userCompetences = competenceMapper.getUserCompetence(params);
			if (userCompetences == null || userCompetences.isEmpty()) {
				//用户没有任何终端权限
				result.put("code", "0001");
				result.put("message", "没有权限删除终端");
				return;
			}
			Map<String, Object> userCompetence = userCompetences.get(0);
			String userCompetenceType = userCompetence.get("competence_type").toString();
			if (!"0".equals(userCompetenceType)) {
				//用户不是最高权限
				result.clear();
				result.put("code", "0001");
				result.put("message", "没有权限删除终端");
				return;
			}
			//删除终端数据，级联删除终端下的其它数据
			terminalMapper.deleteTerminal(params);
			
			//删除终端的所有升级文件
			params.put("path", params.get("terminal_id"));
			FileUtil.deleteFolder(packageFileMapper, terminalProperty.getUpgradePackagePath() + File.separator + params.getString("path"));
			
		} catch (Exception e) {
			logger.error("delete terminal err   " + e.getMessage());
			throw e;
		}
	}
}
