package com.iot.ota_web.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.PackageVersion;

public interface PackageVersionMapper {
	
	
	List<PackageVersion> getPackageVersions(Map<String, Object> params);
	
	void addPackageVersion(JSONObject params);
	
	void deletePackageVersion(Map<String, Object> params);
	
	List<Map<String, Object>> getPackageVersionInfos(JSONObject params);
	
	void updatePackageVersionInfo(JSONObject params);
}
