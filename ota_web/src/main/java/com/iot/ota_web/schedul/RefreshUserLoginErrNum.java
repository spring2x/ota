package com.iot.ota_web.schedul;


import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iot.ota_web.aop.LoginAspect;


@Component
public class RefreshUserLoginErrNum {
	
	private Logger logger = LogManager.getLogger(RefreshUserLoginErrNum.class);

	@Scheduled(cron="0 0 0 * * ?")
	public void refresh(){
		for(Entry<String, Integer> entry : LoginAspect.userLoginStatisticMap.entrySet()){
			entry.setValue(0);
			logger.info("用户-[" + entry.getKey() + "]  登录错误次数被重置");
		}
	}
}
