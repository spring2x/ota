package com.iot.ota_upgrade.util;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;


public class MinaUtil {
	
	private static Logger logger = LogManager.getLogger(MinaUtil.class.getName());
	
	/**
	 * 
	 * @param buffer
	 *            从中读取字符串
	 * @param size
	 *            所需要读取的字节数
	 * @param charset
	 *            解码的字符集
	 * @return
	 */
	public static String getStringFromIoBuffer(IoBuffer buffer, int size, String charset) {
		String result = null;
		try {
			result = buffer.getString(size, Charset.forName(charset).newDecoder());
		} catch (CharacterCodingException e) {
			logger.error("get string from buffer is error!!!");
			e.printStackTrace();
		}
		return result;
	}
}
