package com.iot.ota_upgrade.mina.code;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.BasicDeviceConstant;
import com.iot.ota_upgrade.factory.MessageCodeFactory;
import com.iot.ota_upgrade.mina.code.interf.CodeProcessorInterf;

public class ResponseEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		JSONObject result = (JSONObject) message;
		int messageType = (int) result.get(BasicDeviceConstant.MESSAGE_TYPE_KEY);
		CodeProcessorInterf codeProcess = MessageCodeFactory.getCodeProcessor(messageType);
		codeProcess.encode(session, message, out);
	}

}
