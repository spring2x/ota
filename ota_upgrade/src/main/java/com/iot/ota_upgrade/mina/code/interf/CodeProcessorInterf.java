package com.iot.ota_upgrade.mina.code.interf;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.iot.ota_upgrade.message.BasicMessage;

public interface CodeProcessorInterf {
	
	BasicMessage decode(BasicMessage basicMessage, IoBuffer buffer) throws Exception;
	
	void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception;
}
