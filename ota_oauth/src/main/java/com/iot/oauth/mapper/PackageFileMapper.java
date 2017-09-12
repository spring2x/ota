package com.iot.oauth.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.PackageFile;

public interface PackageFileMapper {
	
	/**
	 * 获取已经有了的升级包文件
	 * @param params
	 * @return
	 */
	List<PackageFile> getPackageFiles(Map<String, Object> params);
	
	/**
	 * 新增升级包文件
	 * @param params
	 */
	void addPackageFile(Map<String, Object> params);
	
	/**
	 * 删除升级包文件信息
	 * @param params
	 */
	void deletePackageFile(JSONObject params);
	
	List<PackageFile> getNewPackageFiles(Map<String, Object> params);
}
