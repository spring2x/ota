package com.iot.ota_web.service;

import java.io.File;
import java.util.List;
import java.util.Map;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.PackageFile;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.mapper.PackageFileMapper;
import com.iot.ota_web.mapper.UserTerminalCompetenceMapper;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.FileUtil;

@Service
public class PackageFileService {
	
	Logger logger = LogManager.getLogger(PackageFileService.class.getName());
	@Autowired
	public PackageFileMapper packageFileMapper;
	@Autowired
	UserTerminalCompetenceMapper userTerminalCompetenceMapper;
	
	@Autowired
	TerminalProperty terminalProperty;
	
	@Autowired
	FileCacheService fileCacheService;
	
	@Transactional(rollbackFor=Exception.class)
	public void saveFile(MultipartFile file, Map<String, Object> params, JSONObject result) throws Exception {
		
		if (!params.containsKey("terminal_id") || !params.containsKey("package_id") || !params.containsKey("package_version_id")
				|| !params.containsKey("userId")
				|| "".equals(params.get("terminal_id").toString())
				|| "".equals(params.get("package_id").toString())
				|| "".equals(params.get("package_version_id").toString())
				|| "".equals(params.get("userId").toString())) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		List<Map<String, Object>> userCompetences = userTerminalCompetenceMapper.getUserCompetence(new JSONObject(params));
		if (userCompetences == null || userCompetences.isEmpty()) {
			result.put("code", "0001");
			result.put("message", "用户没有该终端的任何权限");
			return;
		}
		
		Map<String, Object> userCompetence = userCompetences.get(0);
		if (Integer.parseInt(userCompetence.get("competence_type").toString()) != 0 &&
				Integer.parseInt(userCompetence.get("competence_type").toString()) != 1) {
			result.put("code", "0001");
			result.put("message", "用户没有权限添加升级包文件");
			return;
		}
		
		String rootPath = terminalProperty.getUpgradePackagePath();
		
		try {
			
			List<PackageFile> versionFiles = packageFileMapper.getPackageFiles(params);
			if (versionFiles == null || versionFiles.isEmpty()) {
				params.put("fileName", file.getOriginalFilename());
				packageFileMapper.addPackageFile(params);
			}else {
				result.put("code", "0001");
				result.put("message", "该版本文件已经存在");
				return;
			}
			
			String filePath = FileUtil.savePackage(file, rootPath, params.get("terminal_id").toString(), 
					params.get("package_id").toString(), params.get("package_version_id").toString());
			
			File cacheFile = new File(filePath);
			String key = params.get("terminal_id").toString() + "_" + params.get("package_id").toString() + "_" + params.get("package_version_id").toString();
			try {
				fileCacheService.ayncCacheProcess(key, cacheFile);
			} catch (Exception e) {
				ExceptionUtil.printExceptionToLog(logger, e);
				try {
					fileCacheService.removeFileFromRedis(key);
				} catch (Exception e1) {
					ExceptionUtil.printExceptionToLog(logger, e1);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取终端的升级包信息
	 * @param params
	 * @param result
	 */
	@Transactional(readOnly = true)
	public void getPackageFileInfo(Map<String, Object> params, JSONObject result) throws Exception{
		JSONObject packageFilesObject = new JSONObject();
		result.put("packageFiles", packageFilesObject);
		JSONArray packageFileArray = new JSONArray();
		packageFilesObject.put("packageFilesArray", packageFileArray);
		try {
			List<PackageFile> packageFiles = packageFileMapper.getPackageFiles(params);
			if (packageFiles != null && !packageFiles.isEmpty()) {
				for(PackageFile packageFile : packageFiles){
					JSONObject fileObject = new JSONObject();
					fileObject.put("terminalId", packageFile.getTerminalId());
					fileObject.put("packageId", packageFile.getPackageId());
					fileObject.put("versionId", packageFile.getPackageVersionId());
					fileObject.put("fileName", packageFile.getFileName());
					packageFileArray.add(fileObject);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
	}
	
}
