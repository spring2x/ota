package com.iot.ota_upgrade.mina.code.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;


public class MessageExceptionCodePro extends BasicCodeProcessor {
	
	Logger logger = LogManager.getLogger(MessageExceptionCodePro.class.getName());
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception{
		JSONObject encodeMessage = (JSONObject)message;
		
		//buffer的容量
		int capacity = 0;
		byte messageType = encodeMessage.getByteValue(ExceptionMessageConstant.MESSAGE_TYPE_KEY);
		capacity += 1;
		byte messageLenth = ExceptionMessageConstant.MESSAGE_LENTH;
		capacity += 1;
		byte errMessageId = encodeMessage.getByteValue(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK);
		capacity += 1;
		byte errCode = encodeMessage.getByteValue(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK);
		capacity += 1;
		byte checkSum = 0;
		checkSum ^= errMessageId;
		checkSum ^= errCode;
		capacity += 1;
		//预留位
		capacity += 4;
		byte reservedBit = (byte)255;
		checkSum ^= reservedBit;
		checkSum ^= reservedBit;
		checkSum ^= reservedBit;
		checkSum ^= reservedBit;
		
		
		IoBuffer buffer = IoBuffer.allocate(capacity, false);
		buffer.setAutoExpand(true);
		buffer.put(messageType);
		buffer.put(messageLenth);
		buffer.put(checkSum);
		buffer.put(errMessageId);
		buffer.put(errCode);
		
		//预留位
		buffer.put(reservedBit);
		buffer.put(reservedBit);
		buffer.put(reservedBit);
		buffer.put(reservedBit);
		buffer.flip();
		out.write(buffer);
	}
	
}
