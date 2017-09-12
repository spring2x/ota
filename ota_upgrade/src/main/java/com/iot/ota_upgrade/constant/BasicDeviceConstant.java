package com.iot.ota_upgrade.constant;

import java.util.HashMap;
import java.util.Map;

public class BasicDeviceConstant extends BasicConstant{
	
	public static final String CHARSET = "utf-8";
	
	public static final String MESSAGE_TYPE_KEY = "message_type";
	public static final String MESSAGE_LENTH_KEY = "message_lenth";
	public static final String CHECK_SUM_KEY = "check_sum";
	
	public static final Map<Integer, String> DEVICE_SERVICE_MAP = new HashMap<>();
	
	static{
		DEVICE_SERVICE_MAP.put(DeviceUpReqConstant.MESSAGE_TYPE, "deviceUpReqService");
		DEVICE_SERVICE_MAP.put(DeviceUpDownConStant.REQUEST_MESSAGE_TYPE, "deviceUpDownService");
	}
	
	
}
