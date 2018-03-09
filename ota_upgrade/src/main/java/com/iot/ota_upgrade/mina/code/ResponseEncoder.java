package com.iot.ota_upgrade.mina.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.BasicDeviceConstant;
import com.iot.ota_upgrade.factory.MessageCodeFactory;
import com.iot.ota_upgrade.mina.code.interf.CodeProcessorInterf;
import com.iot.ota_upgrade.util.ExceptionUtil;

public class ResponseEncoder extends ProtocolEncoderAdapter {
	
	private static Logger logger = LogManager.getLogger(ResponseEncoder.class);

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		try {
			JSONObject result = (JSONObject) message;
			int messageType = (int) result.get(BasicDeviceConstant.MESSAGE_TYPE_KEY);
			CodeProcessorInterf codeProcess = MessageCodeFactory.getCodeProcessor(messageType);
			codeProcess.encode(session, message, out);
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
		
	}

}
