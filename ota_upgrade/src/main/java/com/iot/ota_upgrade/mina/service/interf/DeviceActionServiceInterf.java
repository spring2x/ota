package com.iot.ota_upgrade.mina.service.interf;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.message.BasicMessage;


/**
 * 与终端设备交互的服务
 * @author tangliang
 *
 */
public interface DeviceActionServiceInterf {
	/**
	 * 主处理函数。
	 * @throws Exception
	 */
	void process(IoSession session, BasicMessage message, JSONObject result) throws Exception;
}
