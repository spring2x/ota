package com.iot.ota_upgrade.mina.service.impl;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.mina.service.interf.DeviceActionServiceInterf;


public abstract class BasicDeviceActionService implements DeviceActionServiceInterf {

	@Override
	public void process(IoSession session, BasicMessage message, JSONObject result) throws Exception {

	}

}
