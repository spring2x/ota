package com.iot.ota_upgrade.mina.code;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * mina编解码器.
 * @author liqiang
 *
 */
public class CodeFactory implements ProtocolCodecFactory {
	private final ResponseEncoder encoder;
	private final RequestDecoder decoder;

	public CodeFactory() {
		encoder = new ResponseEncoder();
		decoder = new RequestDecoder();
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}
	
}
