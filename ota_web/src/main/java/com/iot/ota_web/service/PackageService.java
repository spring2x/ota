package com.iot.ota_web.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.mapper.PackageFileMapper;
import com.iot.ota_web.mapper.PackageMapper;
import com.iot.ota_web.mapper.UserTerminalCompetenceMapper;
import com.iot.ota_web.util.FileUtil;
import com.iot.ota_web.bean.Package;
import com.iot.ota_web.bean.TerminalProperty;


/**
 * 升级包相关的服务
 * @author liqiang
 *
 */
@Service
public class PackageService {
	
	Logger logger = LogManager.getLogger(PackageService.class.getName());
	@Autowired
	PackageMapper packageMapper;
	
	@Autowired
	UserTerminalCompetenceMapper userTerminalCompetenceMapper;
	
	@Autowired
	PackageFileMapper packageFileMapper;
	
	@Autowired
	TerminalProperty terminalProperty;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 新增升级包
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addPackageProcess(JSONObject params, JSONObject result) throws Exception {
		// 请求参数必须携带用户id和设备终端id。否则无法验证用户终端权限
		if (!params.containsKey("userId") || !params.containsKey("terminal_id") || !params.containsKey("package_name")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}

		boolean noAnyAutority = getUserAuthority(params, result);
		if (noAnyAutority) {
			// 用户没有任何权限，直接返回
			return;
		}

		String userCompetenceType = result.getString("competence_type");
		result.clear();

		if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
			result.put("code", "0001");
			result.put("message", "没有权限添加升级包名称");
			return;
		}

		try {
			List<Package> packages = packageMapper.getPackages(params);
			if (packages != null && !packages.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "该升级包名称已经存在");
				return;
			}

			packageMapper.addPackage(params);
			FileUtil.createFolder(params.get("terminal_id").toString() + File.separator + params.get("package_id"), terminalProperty.getUpgradePackagePath());
			result.put("code", "0000");
			result.put("message", "sucess");
			result.put("package_id", params.get("package_id"));
		} catch (Exception e) {
			logger.error("add package err  " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 删除升级包名称
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deletePackageProcess(JSONObject params, JSONObject result) throws Exception{
		//1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("terminal_id") || !params.containsKey("package_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		
		boolean noAnyAutority = getUserAuthority(params, result);
		if (noAnyAutority) {
			//用户没有任何权限，直接返回
			return;
		}
		
		String userCompetenceType = result.getString("competence_type");
		result.clear();
		if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
			result.put("code", "0001");
			result.put("message", "没有权限删除升级包名称");
			return;
		}
		
		
		try {
			packageMapper.deletePackage(params);
			params.put("path", params.get("terminal_id") + File.separator + params.get("package_id"));
			FileUtil.deleteFolder(packageFileMapper, terminalProperty.getUpgradePackagePath() + File.separator + params.getString("path"));
			redisTemplate.delete(redisTemplate.keys(params.get("terminal_id") + "_" + params.get("package_id") + "_*"));
			result.put("code", "0000");
			result.put("message", "sucess");
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取升级包名称列表。
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public void getPackageProcess(JSONObject params, JSONObject result) throws Exception {
		// 1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("terminal_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		boolean noAnyAutority = getUserAuthority(params, result);
		if (noAnyAutority) {
			// 用户没有任何权限，直接返回
			return;
		}

		result.clear();
		try {
			List<Package> packages = packageMapper.getPackages(params);
			JSONArray packageArray = new JSONArray();
			result.put("packageArray", packageArray);
			for (Package package1 : packages) {
				JSONObject packageJson = new JSONObject();
				packageArray.add(packageJson);
				packageJson.put("package_id", package1.getId());
				packageJson.put("package_name", package1.getPackageName());
				packageJson.put("package_terminalId", package1.getTerminalId());
				packageJson.put("package_introduce", package1.getIntroduce());
			}
			result.put("code", "0000");
			result.put("message", "sucess");
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新升级包信息
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updatePackageProcess(JSONObject params, JSONObject result) throws Exception {
		// 1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("terminal_id") || !params.containsKey("package_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		boolean noAnyAutority = getUserAuthority(params, result);
		if (noAnyAutority) {
			// 用户没有任何权限，直接返回
			return;
		}
		
		String userCompetenceType = result.getString("competence_type");
		result.clear();
		if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
			result.put("code", "0001");
			result.put("message", "没有权限修改升级包");
			return;
		}
		
		//检查修改的包名称在数据库中是否已经存在
		if (params.containsKey("package_name")) {
			JSONObject _params = new JSONObject();
			_params.put("package_name", params.get("package_name"));
			List<Package> packages = packageMapper.getPackages(_params);
			if (packages != null && !packages.isEmpty()) {
				for(Package package1 : packages){
					if (package1.getTerminalId().intValue() == params.getInteger("terminal_id").intValue() 
							&& package1.getId().intValue() != params.getInteger("package_id").intValue()
							&& package1.getPackageName().equals(params.get("package_name"))) {
						result.put("code", "0001");
						result.put("message", "修改的包名称已经存在");
						return;
					}
				}
			}
		}

		try {
			packageMapper.updatePackage(params);
			result.put("code", "0000");
			result.put("message", "sucess");
		} catch (Exception e) {
			logger.error("update package info err" + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 获取用户的终端权限
	 * @param params
	 * @param result
	 * @return  该用户是否没有任何权限
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public boolean getUserAuthority(JSONObject params, JSONObject result) throws Exception{
		List<Map<String, Object>> userCompetences = null;
		try {
			userCompetences = userTerminalCompetenceMapper.getUserCompetence(params);
			if (userCompetences == null || userCompetences.isEmpty()) {
				result.clear();
				result.put("code", "0001");
				result.put("message", "该用户没有对应终端权限");
				return true;
			}
			
			Map<String, Object> userCompetence = userCompetences.get(0);
			result.putAll(userCompetence);
		} catch (Exception e) {
			logger.error("get user authority err" + e.getMessage());
			throw e;
		}
		return false;
	}
	
}
