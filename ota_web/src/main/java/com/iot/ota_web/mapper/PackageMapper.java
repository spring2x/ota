package com.iot.ota_web.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.Package;

public interface PackageMapper {
	
	/**
	 * 根据条件获取升级包信息
	 * @param params
	 * @return
	 */
	List<Package> getPackages(Map<String, Object> params);
	
	/**
	 * 新增升级包信息。
	 * @param params
	 */
	void addPackage(Map<String, Object> params);
	
	/**
	 * 删除升级包名称
	 * @param params
	 */
	void deletePackage(JSONObject params);
	
	/**
	 * 修改升级包信息
	 * @param params
	 */
	void updatePackage(JSONObject params);
}
