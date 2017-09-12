package com.iot.ota_upgrade.mina.code.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.mina.code.interf.CodeProcessorInterf;


public abstract class BasicCodeProcessor implements CodeProcessorInterf {

	@Override
	public BasicMessage decode(BasicMessage basicMessage, IoBuffer buffer) throws Exception {
		return null;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {

	}

}
