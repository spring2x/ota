package com.iot.ota_upgrade.constant;

/**
 * 升级包下载需要的常量
 * @author liqiang
 *
 */
public class DeviceUpDownConStant extends BasicDeviceConstant{
	
	/**
	 * 设备升级包下载请求指令
	 */
	public static final int REQUEST_MESSAGE_TYPE = 8;
	
	/**
	 * 设备升级包下发
	 */
	public static final int RESPONSE_MESSAGE_TYPE = 9;
	
	/**
	 * 升级包编号  key
	 */
	public static final String PACKAGE_NO_KEY = "package_no";
	
	/**
	 * 升级包编号所占的字节数
	 */
	public static final int PACKAGE_NO_LENTH = 2;
	
	/**
	 * 升级包byte数据  key
	 */
	public static final String PACKAGE_BYTE_DATA_KEY = "package_byte_data";
}
