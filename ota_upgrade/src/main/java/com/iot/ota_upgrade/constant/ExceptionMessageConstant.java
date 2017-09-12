package com.iot.ota_upgrade.constant;

public class ExceptionMessageConstant extends BasicDeviceConstant {
	/**
	 * 消息类型
	 */
	public static final int MESSAGE_TYPE = 0x0a;
	
	/**
	 * 消息体长度
	 */
	public static final byte MESSAGE_LENTH = 6;
	
	/**
	 * 错误消息id的标识
	 */
	public static final String ERR_MESSAGE_ID_MARK = "err_message_id";
	
	/**
	 * 错误消息码的标识。
	 */
	public static final String ERR_MESSAGE_CODE_MARK = "err_message_code";
	
	/**
	 * 服务器错误码
	 */
	public static final byte SERVER_ERR = 0;
	
	/**
	 * 消息格式错误码
	 */
	public static final byte MESSAGE_FORMAR_ERR = 1;
	
	/**
	 * 鉴权失败错误码
	 */
	public static final byte VALIDE_FAIL = 2;
	
	/**
	 * 校验和错误
	 */
	public static final byte CHECK_SUM_ERR = 3;
}
