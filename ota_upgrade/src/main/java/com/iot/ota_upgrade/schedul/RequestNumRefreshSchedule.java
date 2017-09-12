package com.iot.ota_upgrade.schedul;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.ReqStatistic;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.mapper.ReqStatisticMapper;
import com.iot.ota_upgrade.util.DateUtil;
import com.iot.ota_upgrade.util.RequestStatisticsUtil;

@Component
public class RequestNumRefreshSchedule {
	
	private Logger logger = LogManager.getLogger(RequestNumRefreshSchedule.class);
	
	@Autowired
	ReqStatisticMapper reqStatisticMapper;
	
	@Autowired
	UpgradeProperty upgradeProperty;

	@Scheduled(fixedRate=5000)
	public void refresh(){
		
		if (RequestStatisticsUtil.getCurrentReqNum() == 0) {
			return;
		}
		
		logger.info("device req statistics:   " + "host:" + upgradeProperty.getServerIp() + "\t" + "port:" + upgradeProperty.getServerPort()
				+ "\t" + "reqNum:" + RequestStatisticsUtil.currentRequestNum + "\t" + "time:" + DateUtil.DateFormat(System.currentTimeMillis()));
		
		JSONObject reqJson = new JSONObject();
		reqJson.put("host", upgradeProperty.getServerIp());
		reqJson.put("port", upgradeProperty.getServerPort());
		if (upgradeProperty.isDeviceReqStatistics()) {
			reqJson.put("num", RequestStatisticsUtil.getCurrentReqNum());
			reqStatisticMapper.addStatisticData(reqJson);
		}else {
			List<ReqStatistic> reqStatistics = reqStatisticMapper.getStatisticData(reqJson);
			reqJson.put("num", RequestStatisticsUtil.getCurrentReqNum());
			if (reqStatistics == null || reqStatistics.isEmpty()) {
				reqStatisticMapper.addStatisticData(reqJson);
			}else {
				ReqStatistic reqStatistic = reqStatistics.get(0);
				System.out.println(reqStatistic.getUpdateTime());
				reqJson.put("update_time", reqStatistic.getUpdateTime());
				reqStatisticMapper.updateStatisticData(reqJson);
			}
		}
	}
}
