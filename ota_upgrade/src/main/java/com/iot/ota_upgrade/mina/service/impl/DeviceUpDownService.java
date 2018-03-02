package com.iot.ota_upgrade.mina.service.impl;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.constant.DeviceUpDownConStant;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpDownMessage;
import com.iot.ota_upgrade.util.ByteUtil;
import com.iot.ota_upgrade.util.FileUtil;


public class DeviceUpDownService extends BasicDeviceActionService {
	
	Logger logger = LogManager.getLogger(DeviceUpDownService.class.getName());
	private UpgradeProperty upgradeProperty;
	
	@Override
	public void process(IoSession session, BasicMessage message, JSONObject result) throws Exception {
		DeviceUpDownMessage deviceUpDownMessage = (DeviceUpDownMessage) message;
		short deviceTypeMark = deviceUpDownMessage.getDeviceTypeMark();
		short packageMark = deviceUpDownMessage.getPacakgeMark();
		short packageVersionMark = deviceUpDownMessage.getPacakgeVersionMark();
		short packageNo = deviceUpDownMessage.getPackageNo();
		
		String fileMark = new StringBuilder("").append(deviceTypeMark).append(File.separator).append(packageMark).append(File.separator).append(packageVersionMark).toString();
		
		
		// 升级包所在的文件夹
		if (!FileUtil.fileMap.containsKey(fileMark) || FileUtil.fileMap.get(fileMark).length == 0) {
			logger.error("the download file " + fileMark + " is not inited");
			throw new Exception("the download file  " + fileMark + " is not inited");
		} else {
			// 需要下载的升级包
			//File upgradePackage = upgradePackages[0];
			
			int len = Integer.parseInt(session.getAttribute(DeviceUpReqConstant.SINGLE_PACKAGE_LENTH_KEY).toString()) * upgradeProperty.getSinglePackageLenthUtil();
			int pos = (packageNo - 1) * len;
			byte[] packageBytes = FileUtil.getFileBytes(fileMark, pos, len);
			//计算消息体的长度，升级包编号长度2+升级包的字节数
			int messageLength = (DeviceUpDownConStant.PACKAGE_NO_LENTH + packageBytes.length);
			
			//计算消息的校验和
			byte checkSum = calculateCheckSum(packageNo, packageBytes);
			
			result.put(DeviceUpDownConStant.MESSAGE_TYPE_KEY, DeviceUpDownConStant.RESPONSE_MESSAGE_TYPE);
			result.put(DeviceUpDownConStant.MESSAGE_LENTH_KEY, messageLength);
			result.put(DeviceUpDownConStant.CHECK_SUM_KEY, checkSum);
			result.put(DeviceUpDownConStant.PACKAGE_NO_KEY, packageNo);
			result.put(DeviceUpDownConStant.PACKAGE_BYTE_DATA_KEY, packageBytes);
		}
	}
	
	/**
	 * 设备升级包下载请求指令 校验和计算
	 * @param packageNo	分包号
	 * @param packageBytes	分包字节
	 * @return
	 */
	public byte calculateCheckSum(short packageNo, byte[] packageBytes) {
		// 计算消息的校验和
		byte checkSum = 0;
		byte[] packageNoBytes = ByteUtil.shortToByteArray(packageNo);
		for (byte packageNoByte : packageNoBytes) {
			checkSum ^= packageNoByte;
		}

		for (byte packageByte : packageBytes) {
			checkSum ^= packageByte;
		}

		return checkSum;
	}

	public DeviceUpDownService(UpgradeProperty upgradeProperty) {
		super();
		this.upgradeProperty = upgradeProperty;
	}
}
