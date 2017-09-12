package com.iot.ota_upgrade.util;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestStatisticsUtil {

	/**
	 * 统计当前在线的设备数量.
	 */
	public static AtomicInteger currentRequestNum = new AtomicInteger(0);
	
	public static int getCurrentReqNum(){
		return currentRequestNum.intValue();
	}
	
}
