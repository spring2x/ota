package com.iot.oauth.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.PackageVersion;

public interface PackageVersionMapper {
	
	
	List<PackageVersion> getPackageVersions(Map<String, Object> params);
	
	void addPackageVersion(JSONObject params);
	
	void deletePackageVersion(Map<String, Object> params);
	
	List<Map<String, Object>> getPackageVersionInfos(JSONObject params);
	
	void updatePackageVersionInfo(JSONObject params);
}
