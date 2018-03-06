package com.iot.oauth.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.PackageVersion;
import com.iot.oauth.mapper.PackageVersionMapper;
import com.iot.oauth.util.ExceptionUtil;

@Service
public class VersionServive {
	
	private static Logger logger = LogManager.getLogger(VersionServive.class);
	
	@Autowired
	private PackageVersionMapper packageVersionMapper;

	public String getInfo(PackageVersion version){
		JSONObject result = new JSONObject();
		List<Map<String, Object>> versionInfos = null;
		try {
			versionInfos = packageVersionMapper.getVersionInfo(version);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "内部错误");
			return result.toJSONString();
		}
		if (versionInfos == null || versionInfos.size() == 0) {
			result.put("code", "0001");
			result.put("message", "请求版本不存在");
			return result.toJSONString();
		}
		
		JSONArray resultArray = new JSONArray();
		for(Map<String, Object> versionInfo : versionInfos){
			resultArray.add(versionInfo);
		}
		
		
		//result.putAll(versionInfos.get(0));
		return resultArray.toJSONString();
	}
	
}
