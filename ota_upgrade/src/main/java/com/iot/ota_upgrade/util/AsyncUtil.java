package com.iot.ota_upgrade.util;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.iot.ota_upgrade.mina.service.impl.DeviceUpReqService;

@Component
public class AsyncUtil {
	
	private static Logger logger = LogManager.getLogger(AsyncUtil.class);

	/**
	 * 初始化校验和
	 */
	@Async
	public void initValideCodeMap(String fileMark, Map<Integer, String> valideMap){
		synchronized (DeviceUpReqService.fileValideCodeMap) {
			DeviceUpReqService.fileValideCodeMap.put(fileMark, valideMap);
			logger.info("###########file inited notifyAll");
			DeviceUpReqService.fileValideCodeMap.notifyAll();
		}
	}
}
