package com.iot.ota_upgrade.mina.service.impl;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ConcurrentHashSet;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpReqMessage;
import com.iot.ota_upgrade.schedul.InitDownLoadFileSchedule;
import com.iot.ota_upgrade.service.DeviceTokenService;
import com.iot.ota_upgrade.service.InitPackageFileService;
import com.iot.ota_upgrade.util.ByteUtil;
import com.iot.ota_upgrade.util.FileUtil;

public class DeviceUpReqService extends BasicDeviceActionService {

	public DeviceTokenService deviceTokenService;

	public InitPackageFileService initPackageFileService;

	UpgradeProperty upgradeProperty;

	private Logger logger = LogManager.getLogger(DeviceUpReqService.class.getName());

	public static ConcurrentHashMap<String, Map<Integer, String>> fileValideCodeMap = new ConcurrentHashMap<>();

	public static ConcurrentHashSet<String> fileMarkSet = new ConcurrentHashSet<>();

	@Override
	public void process(IoSession session, BasicMessage basicMessage, JSONObject result) throws Exception {
		DeviceUpReqMessage message = (DeviceUpReqMessage) basicMessage;
		String authCode = message.getAuthCode();
		String upMark = message.getUpMark();

		JSONObject params = new JSONObject();

		// 默认的终端授权码与设备发送的授权码不一致的情况
		if (!upgradeProperty.getDefaultTerminalValideCode().equals(authCode)) {
			params.put("uuid", authCode);
			params.put("mark", upMark);
			try {
				// logger.debug("******************************* " + "device
				// token valid start....");
				// logger.debug("******************************* " + "device
				// token valid param: " + params.toJSONString());
				boolean validResult = deviceTokenService.validDeviceToken(params, result);
				// logger.debug("******************************* " + "device
				// token valid end");
				// logger.debug("******************************* " + "device
				// token valid result: " + validResult);
				if (!validResult) {
					result.clear();
					result.put(ExceptionMessageConstant.MESSAGE_TYPE_KEY, ExceptionMessageConstant.MESSAGE_TYPE);
					result.put(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK, DeviceUpReqConstant.MESSAGE_TYPE);
					result.put(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK, ExceptionMessageConstant.VALIDE_FAIL);
					return;
				}
			} catch (Exception e) {
				throw e;
			}
		}

		short singlePackLength = message.getSinglePackageLength();
		byte deviceMark = message.getDeviceMark();
		byte packageMark = message.getPackageMark();
		byte versionMark = message.getVersionMark();
		String fileMark = deviceMark + File.separator + packageMark + File.separator + versionMark;

		if (!fileValideCodeMap.containsKey(fileMark)) {
			synchronized (fileValideCodeMap) {
				int waitTime = upgradeProperty.getFileInitTestNum() * upgradeProperty.getFileInitTestInterval();
				if (!fileValideCodeMap.containsKey(fileMark) && !fileMarkSet.contains(fileMark)) {
					JSONObject body = new JSONObject();
					body.put("terminal", deviceMark);
					body.put("package", packageMark);
					body.put("version", versionMark);
					logger.info(fileMark + "\t" + "start inted");
					JSONObject initResult = initPackageFileService.initDownLoadFile(body);
					if ("0001".equals(initResult.get("code"))) {
						logger.error(initResult.get("message"));
					} else {
						fileMarkSet.add(fileMark);
						fileValideCodeMap.wait(waitTime);
					}
				} else if (!fileValideCodeMap.containsKey(fileMark) && fileMarkSet.contains(fileMark)) {
					fileValideCodeMap.wait(waitTime);
				}
			}
		}

		if (!fileValideCodeMap.containsKey(fileMark)) {
			InitDownLoadFileSchedule.fileMarkSet.remove(fileMark);
			logger.error("request file can not be init  " + fileMark);
			throw new Exception("request file can not be init  " + fileMark);
		}

		// 文件校验方式
		int validMark = message.getValidMark();

		String upgradePackageValidCode = fileValideCodeMap.get(fileMark).get(validMark);
		// 消息体长度
		byte messageLenth = (byte) (DeviceUpReqConstant.RESPONSE_PACKAGE_SIZE_LENTH
				+ DeviceUpReqConstant.RESPONSE_SPLIT_PACKAGE_LENTH + upgradePackageValidCode.length());
		// 升级包所占字节数
		int packageSize = FileUtil.fileMap.get(fileMark).length;

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
}
