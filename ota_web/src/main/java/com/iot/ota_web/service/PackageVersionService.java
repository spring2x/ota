package com.iot.ota_web.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.util.FileUtil;
import com.iot.ota_web.bean.Package;
import com.iot.ota_web.bean.PackageFile;
import com.iot.ota_web.bean.PackageVersion;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.mapper.PackageFileMapper;
import com.iot.ota_web.mapper.PackageMapper;
import com.iot.ota_web.mapper.PackageVersionMapper;
import com.iot.ota_web.mapper.UserTerminalCompetenceMapper;

/**
 * 与升级包版本相关的业务
 * @author liqiang
 *
 */
@Service
public class PackageVersionService {
	
	Logger logger = LogManager.getLogger(PackageVersionService.class.getName());
	
	@Autowired
	PackageMapper packageMapper;
	
	@Autowired
	UserTerminalCompetenceMapper userTerminalCompetenceMapper;
	
	@Autowired
	PackageVersionMapper packageVersionMapper;
	
	@Autowired
	PackageFileMapper packageFileMapper;
	
	@Autowired
	TerminalProperty terminalProperty;
	
	/**
	 * 新增升级包版本。
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void addVersionProcess(JSONObject params, JSONObject result) throws Exception {

		// 1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("package_id") || !params.containsKey("package_version")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}

		// 2、用户鉴权
		boolean isReturn = getUserAuthority(params, result);
		if (isReturn) {
			return;
		}

		String userCompetenceType = result.getString("competence_type");

		if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
			result.clear();
			result.put("code", "0001");
			result.put("message", "没有权限添加升级包版本信息");
			return;
		}

		//3、添加升级包版本信息
		try {
			List<PackageVersion> packageVersions = packageVersionMapper.getPackageVersions(params);
			if (packageVersions != null && !packageVersions.isEmpty()) {
				result.clear();
				result.put("code", "0001");
				result.put("message", "该升级包版本信息已经存在");
				return;
			}
			packageVersionMapper.addPackageVersion(params);
			FileUtil.createFolder(params.get("terminal_id").toString() + File.separator + params.get("package_id") + File.separator + params.get("package_version_id"), terminalProperty.getUpgradePackagePath());
			result.clear();
			result.put("code", "0000");
			result.put("message", "success");
			result.put("package_version_id", params.get("package_version_id"));
		} catch (Exception e) {
			result.clear();
			logger.error("add package version err" + e.getMessage());
			throw e;
		}

	}
	
	
	/**
	 * 删除升级包版本信息
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void deleteVersionProcess(JSONObject params, JSONObject result) throws Exception{
		//1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("version_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		try {
			//2、用户鉴权
			List<PackageVersion> packageVersions = packageVersionMapper.getPackageVersions(params);
			if (packageVersions == null || packageVersions.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "升级包版本信息不存在");
				return;
			}
			PackageVersion packageVersion = packageVersions.get(0);
			int packageId = packageVersion.getPackageId();
			params.put("package_id", packageId);
			boolean isReturn = getUserAuthority(params, result);
			if (isReturn) {
				return;
			}
			
			String userCompetenceType = result.getString("competence_type");

			if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
				result.clear();
				result.put("code", "0001");
				result.put("message", "没有权限删除升级包版本信息");
				return;
			}
			
			//3、删除升级包版本信息
			packageVersionMapper.deletePackageVersion(params);
			
			params.put("path", result.get("terminalId") + File.separator + params.get("package_id") + File.separator + params.get("version_id"));
			result.clear();
			FileUtil.deleteFolder(packageFileMapper, terminalProperty.getUpgradePackagePath() + File.separator + params.getString("path"));
			result.put("code", "0000");
			result.put("message", "success");
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新升级包版本信息
	 * @throws Exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void updatePackageVersionInfo(JSONObject params, JSONObject result) throws Exception{
		//1、参数校验
		if (!params.containsKey("userId") || !params.containsKey("version_id")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		//2、用户鉴权
		JSONObject _params = new JSONObject();
		_params.put("version_id", params.get("version_id"));
		List<PackageVersion> packageVersions = packageVersionMapper.getPackageVersions(_params);
		if (packageVersions == null || packageVersions.isEmpty()) {
			result.put("code", "0001");
			result.put("message", "升级包版本信息不存在");
			return;
		}
		PackageVersion packageVersion = packageVersions.get(0);
		int packageId = packageVersion.getPackageId();
		params.put("package_id", packageId);
		boolean isReturn = getUserAuthority(params, result);
		if (isReturn) {
			return;
		}
		String userCompetenceType = result.getString("competence_type");
		
		result.clear();
		if (!"0".equals(userCompetenceType) && !"1".equals(userCompetenceType)) {
			result.put("code", "0001");
			result.put("message", "没有权限修改升级包版本信息");
			return;
		}
		
		//3、避免修改成相同的升级包版本
		List<PackageVersion> existPackageVersions = packageVersionMapper.getPackageVersions(new JSONObject());
		if (params.containsKey("package_version")) {
			for (PackageVersion packageVersion1 : existPackageVersions) {
				if (packageVersion1.getId() != params.getInteger("version_id") 
						&& packageVersion1.getPackageVersion().equals(params.get("package_version"))
						&& packageVersion1.getPackageId() == packageId) {
					result.put("code", "0001");
					result.put("message", "升级包版本已经存在");
					return;
				}
			}
		}
		
		//4、更新升级包版本信息
		try {
			packageVersionMapper.updatePackageVersionInfo(params);
			result.put("code", "0000");
			result.put("message", "success");
		} catch (Exception e) {
			logger.error("update package version err" + e.getMessage());
			throw e;
		}
	}
	
	
	
	/**
	 * 获取升级包的版本信息。
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public void getPackageVersions(JSONObject params, JSONObject result) throws Exception{
		//1、参数校验
		if (!params.containsKey("userId")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		try {
			
			//2、根据用户id,获取用户的终端权限。用户只能查看有终端权限的升级包版本信息
			List<Map<String, Object>> userTerminalCompetences = userTerminalCompetenceMapper.getUserCompetence(params);
			if (userTerminalCompetences == null || userTerminalCompetences.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "用户没有任何终端权限");
				return;
			}
			
			//3、获取升级包版本信息
			List<Map<String, Object>> packageVersionInfos = packageVersionMapper.getPackageVersionInfos(params);
			JSONArray packageVersionInfoArray = new JSONArray();
			result.put("package_version_info", packageVersionInfoArray);
			for(Map<String, Object> packageVersionInfo : packageVersionInfos){
				JSONObject packageVersionInfoObject = new JSONObject(packageVersionInfo);
				Map<String, Object> getPackageFileInfoParam = new HashMap<>();
				getPackageFileInfoParam.put("terminal_id", packageVersionInfo.get("terminal_id"));
				getPackageFileInfoParam.put("package_id", packageVersionInfo.get("package_id"));
				getPackageFileInfoParam.put("package_version_id", packageVersionInfo.get("package_version_id"));
				List<PackageFile> packageFiles = packageFileMapper.getPackageFiles(getPackageFileInfoParam);
				if (!packageFiles.isEmpty()) {
					PackageFile packageFile = packageFiles.get(0);
					StringBuilder filePath = new StringBuilder(terminalProperty.getFileDownloadPath()).append("?")
							.append("terminal_id=" + packageFile.getTerminalId())
							.append("&").append("package_id=" + packageFile.getPackageId())
							.append("&").append("version_id=" + packageFile.getPackageVersionId())
							.append("&").append("fileName=" + packageFile.getFileName());
					packageVersionInfoObject.put("filePath", filePath);
				}
				packageVersionInfoArray.add(packageVersionInfoObject);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	/**
	 * 获取用户的权限
	 * @param params
	 * @param result
	 * @return 用户是否没有任何权限
	 * @throws Exception
	 */
	@Transactional(readOnly=true)
	public boolean getUserAuthority(JSONObject params, JSONObject result) throws Exception{
		
		List<Map<String, Object>> userCompetences = null;
		try {
			
			//获取指定的升级包
			List<Package> packages = packageMapper.getPackages(params);
			if (packages == null ||packages.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "请求的升级包不存在");
				return true;
			}
			
			Package package1 = packages.get(0);
			//获取升级包对应的终端
			int terminalId = package1.getTerminalId();
			
			params.put("terminal_id", String.valueOf(terminalId));
			//获取用户的终端权限
			userCompetences = userTerminalCompetenceMapper.getUserCompetence(params);
			if (userCompetences == null || userCompetences.isEmpty()) {
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
