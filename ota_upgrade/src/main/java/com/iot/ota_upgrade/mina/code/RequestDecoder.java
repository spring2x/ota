package com.iot.ota_upgrade.mina.code;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;
import com.iot.ota_upgrade.factory.MessageCodeFactory;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.mina.code.interf.CodeProcessorInterf;


public class RequestDecoder extends CumulativeProtocolDecoder {
	
	Logger logger = LogManager.getLogger(RequestDecoder.class.getName());

	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
		
		byte messageType = -1;
		try {
			//消息类型
			messageType = buffer.get();
			//消息体的字节数
			byte messageLenth = buffer.get();
			//消息校验和
			byte messageChecksum = buffer.get();
			
			//buffer中的字节数，与发送的字节数一致，才能继续执行
			if (buffer.limit() == messageLenth + 3) {
				BasicMessage basicMessage = new BasicMessage(messageType, messageLenth, messageChecksum);
				CodeProcessorInterf codeProcess = MessageCodeFactory.getCodeProcessor(messageType);
				out.write(codeProcess.decode(basicMessage, buffer));
			}else {
				//数据长度验证不过，直接抛异常
				throw new Exception("message length is not right");
			}
		} catch (Exception e) {
			//出现异常，清空buffer内的数据
			buffer.limit(0);
			
			JSONObject result = new JSONObject();
			result.put(ExceptionMessageConstant.MESSAGE_TYPE_KEY, ExceptionMessageConstant.MESSAGE_TYPE);
			result.put(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK, messageType);
			result.put(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK, ExceptionMessageConstant.MESSAGE_FORMAR_ERR);
			session.write(result);
			logger.error("Request decode faild!!!  " + e.getMessage());
			throw e;
		}
		return false;
	}

}
