package com.iot.ota_upgrade.constant;

/**
 * 设备升级请求常量类。
 * @author liqiang
 *
 */
public class DeviceUpReqConstant extends BasicDeviceConstant {
	
	/**
	 * 升级授权码长度.
	 */
	public static final int UPGRADE_AUTH_CODE_LENTH = 16;
	
	/**
	 * 设备升级请求的消息类型。
	 */
	public static final int MESSAGE_TYPE = 6;
	
	/**
	 * 设备升级请求的响应消息类型。
	 */
	public static final int RESPONSE_MESSAGE_TYPE = 7;
	
	/**
	 * 设备升级请求的响应消息中，升级包大小所占的字节数。
	 */
	public static final byte RESPONSE_PACKAGE_SIZE_LENTH = 4;
	
	/**
	 * 设备升级请求的响应消息中，分包大小所占的字节数。
	 */
	public static final byte RESPONSE_SPLIT_PACKAGE_LENTH = 2;
	
	public static final String SINGLE_PACKAGE_LENTH_KEY = "single_package_length";
	
	public static final String PACKAGE_SIZE_KEY = "package_size";
	
	public static final String SPLIT_PACKAGE_NUM_KEY = "split_package_num";
	
	public static final String PACKAGE_VALIDE_CODE_KEY = "package_valide_code";
}
