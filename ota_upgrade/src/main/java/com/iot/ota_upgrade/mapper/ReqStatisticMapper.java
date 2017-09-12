package com.iot.ota_upgrade.mapper;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.ReqStatistic;


public interface ReqStatisticMapper {
	
	/**
	 * 获取统计信息
	 * @param params
	 */
	List<ReqStatistic> getStatisticData(JSONObject params);

	/**
	 * 插入请求的统计信息
	 * @param params
	 */
	void addStatisticData(JSONObject params);
	
	/**
	 * 更新请求的统计信息
	 * @param params
	 */
	void updateStatisticData(JSONObject params);
}
