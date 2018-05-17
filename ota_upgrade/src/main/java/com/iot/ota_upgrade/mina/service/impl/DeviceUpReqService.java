package com.iot.ota_upgrade.mina.service.impl;

import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.bean.ValideMarkEnum;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpReqMessage;
import com.iot.ota_upgrade.service.DeviceTokenService;
import com.iot.ota_upgrade.service.InitPackageFileService;
import com.iot.ota_upgrade.util.ByteUtil;
import com.iot.ota_upgrade.util.FileCacheUtil;

public class DeviceUpReqService extends BasicDeviceActionService {

	public DeviceTokenService deviceTokenService;

	public InitPackageFileService initPackageFileService;

	UpgradeProperty upgradeProperty;

	
	private RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void process(IoSession session, BasicMessage basicMessage, JSONObject result) throws Exception {
		DeviceUpReqMessage message = (DeviceUpReqMessage) basicMessage;
		String authCode = message.getAuthCode();
		String upMark = message.getUpMark();

		// 默认的终端授权码与设备发送的授权码不一致的情况
		if (!upgradeProperty.getDefaultTerminalValideCode().equals(authCode)) {
			ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
			String uuid = (String) valueOperations.get(upMark);
			String token = (String) valueOperations.get(authCode);
			if (token == null || token.equals("") || !authCode.equals(uuid)) {
				result.clear();
				result.put(ExceptionMessageConstant.MESSAGE_TYPE_KEY, ExceptionMessageConstant.MESSAGE_TYPE);
				result.put(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK, DeviceUpReqConstant.MESSAGE_TYPE);
				result.put(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK, ExceptionMessageConstant.VALIDE_FAIL);
				return;
			}
		}

		short singlePackLength = message.getSinglePackageLength();
		short deviceMark = message.getDeviceMark();
		short packageMark = message.getPackageMark();
		short versionMark = message.getVersionMark();
		
		//文件基本信息的key
		String baseInfoKey = deviceMark + "_" + packageMark + "_" + versionMark;
		//文件数据的key
		String fileDataKey = baseInfoKey + "_data";
		
		//文件数据在java内存中过期时间为2个小时
		redisTemplate.opsForValue().set(baseInfoKey + "_expireTool", "1", 120, TimeUnit.MINUTES);
		
		
		// 是否需要把文件数据缓存到本地
		if (!FileCacheUtil.fileCacheLocalMap.containsKey(fileDataKey)) {
			synchronized (FileCacheUtil.fileCacheLocalMap) {
				if (!FileCacheUtil.fileCacheLocalMap.containsKey(fileDataKey)) {
					if (!redisTemplate.hasKey(fileDataKey)) {
						JSONObject body = new JSONObject();
						body.put("fileMark", baseInfoKey);
						JSONObject initResult = initPackageFileService.initDownLoadFile(body);
						if (!"0000".equals(initResult.get("code"))) {
							String msg = initResult.getString("message");
							Exception exception = new Exception(msg);
							throw exception;
						}
					}
					FileCacheUtil.cacheFileToLocal(redisTemplate, baseInfoKey, upgradeProperty.getSinglePackageLenthUtil());
				}
			}
		}
		
		// 文件校验方式
		int validMark = message.getValidMark();
		String upgradePackageValidCode = (String) redisTemplate.opsForHash().get(baseInfoKey, String.valueOf(ValideMarkEnum.values()[validMark - 1]));
		// 消息体长度
		byte messageLenth = (byte) (DeviceUpReqConstant.RESPONSE_PACKAGE_SIZE_LENTH
				+ DeviceUpReqConstant.RESPONSE_SPLIT_PACKAGE_LENTH + upgradePackageValidCode.length());
		// 升级包所占字节数
		int packageSize = new Long((long) redisTemplate.opsForHash().get(baseInfoKey, "fileSize")).intValue();

		// 分包个数
		short splitPackageNum = (short) (packageSize
				% (singlePackLength * upgradeProperty.getSinglePackageLenthUtil()) == 0
						? packageSize / (singlePackLength * upgradeProperty.getSinglePackageLenthUtil())
						: (packageSize / (singlePackLength * upgradeProperty.getSinglePackageLenthUtil()) + 1));

		// 消息体的校验和
		byte checkSum = calculateCheckSum(packageSize, splitPackageNum, upgradePackageValidCode);

		result.put(DeviceUpReqConstant.MESSAGE_TYPE_KEY, DeviceUpReqConstant.RESPONSE_MESSAGE_TYPE);
		result.put(DeviceUpReqConstant.MESSAGE_LENTH_KEY, messageLenth);
		result.put(DeviceUpReqConstant.CHECK_SUM_KEY, checkSum);
		result.put(DeviceUpReqConstant.PACKAGE_SIZE_KEY, packageSize);
		result.put(DeviceUpReqConstant.SPLIT_PACKAGE_NUM_KEY, splitPackageNum);
		result.put(DeviceUpReqConstant.PACKAGE_VALIDE_CODE_KEY, upgradePackageValidCode);

		// 将单包长度保存在session中
		session.setAttribute(DeviceUpReqConstant.SINGLE_PACKAGE_LENTH_KEY, singlePackLength);
		session.setAttribute("remote_addr", session.getRemoteAddress());
	}

	/**
	 * 计算设备升级响应的校验和
	 * 
	 * @param packageSize
	 *            升级包大小
	 * @param splitPackageNum
	 *            升级包的分包数
	 * @param upgradePackageValidCode
	 *            文件校验码
	 * @return 消息校验和
	 */
	public byte calculateCheckSum(int packageSize, short splitPackageNum, String upgradePackageValidCode) {
		byte checkSum = 0;
		byte[] packageSizeBytes = ByteUtil.intToBytes2(packageSize);
		byte[] splitPackageNumBytes = ByteUtil.intToBytes2(splitPackageNum);
		for (byte packageSizeByte : packageSizeBytes) {
			checkSum ^= packageSizeByte;
		}

		for (byte splitPackageNumByte : splitPackageNumBytes) {
			checkSum ^= splitPackageNumByte;
		}

		for (int index = 0; index < upgradePackageValidCode.length(); index++) {
			checkSum ^= upgradePackageValidCode.charAt(index);
		}
		return checkSum;
	}

	public DeviceTokenService getDeviceTokenService() {
		return deviceTokenService;
	}

	public void setDeviceTokenService(DeviceTokenService deviceTokenService) {
		this.deviceTokenService = deviceTokenService;
	}

	public InitPackageFileService getInitPackageFileService() {
		return initPackageFileService;
	}

	public void setInitPackageFileService(InitPackageFileService initPackageFileService) {
		this.initPackageFileService = initPackageFileService;
	}

	public UpgradeProperty getUpgradeProperty() {
		return upgradeProperty;
	}

	public void setUpgradeProperty(UpgradeProperty upgradeProperty) {
		this.upgradeProperty = upgradeProperty;
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	public DeviceUpReqService() {
		// TODO Auto-generated constructor stub
	}
}
