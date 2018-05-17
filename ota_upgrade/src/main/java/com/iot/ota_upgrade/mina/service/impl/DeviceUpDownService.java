package com.iot.ota_upgrade.mina.service.impl;

import org.apache.mina.core.session.IoSession;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.constant.DeviceUpDownConStant;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpDownMessage;
import com.iot.ota_upgrade.util.ByteUtil;
import com.iot.ota_upgrade.util.FileCacheUtil;


public class DeviceUpDownService extends BasicDeviceActionService {
	
	private UpgradeProperty upgradeProperty;
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void process(IoSession session, BasicMessage message, JSONObject result) throws Exception {
		DeviceUpDownMessage deviceUpDownMessage = (DeviceUpDownMessage) message;
		short deviceTypeMark = deviceUpDownMessage.getDeviceTypeMark();
		short packageMark = deviceUpDownMessage.getPacakgeMark();
		short packageVersionMark = deviceUpDownMessage.getPacakgeVersionMark();
		short packageNo = deviceUpDownMessage.getPackageNo();

		String key = deviceTypeMark + "_" + packageMark + "_" + packageVersionMark;
		//文件数据的key
		String fileDataKey = key + "_data";
		if (!FileCacheUtil.fileCacheLocalMap.containsKey(fileDataKey)) {
			Exception exception = new Exception("download file  -[" + key + "] is not inited");
			throw exception;
		}

		// 需要下载的升级包
		int len = Integer.parseInt(session.getAttribute(DeviceUpReqConstant.SINGLE_PACKAGE_LENTH_KEY).toString())
				* upgradeProperty.getSinglePackageLenthUtil();
		int pos = (packageNo - 1) * len;
		byte[] packageBytes = FileCacheUtil.getFileBytes(redisTemplate, fileDataKey, pos, len,
				upgradeProperty.getSinglePackageLenthUtil());
		// 计算消息体的长度，升级包编号长度2+升级包的字节数
		int messageLength = (DeviceUpDownConStant.PACKAGE_NO_LENTH + packageBytes.length);

		// 计算消息的校验和
		byte checkSum = calculateCheckSum(packageNo, packageBytes);
		result.put(DeviceUpDownConStant.MESSAGE_TYPE_KEY, DeviceUpDownConStant.RESPONSE_MESSAGE_TYPE);
		result.put(DeviceUpDownConStant.MESSAGE_LENTH_KEY, messageLength);
		result.put(DeviceUpDownConStant.CHECK_SUM_KEY, checkSum);
		result.put(DeviceUpDownConStant.PACKAGE_NO_KEY, packageNo);
		result.put(DeviceUpDownConStant.PACKAGE_BYTE_DATA_KEY, packageBytes);
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

	public DeviceUpDownService(UpgradeProperty upgradeProperty, RedisTemplate<String, Object> redisTemplate) {
		super();
		this.upgradeProperty = upgradeProperty;
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
