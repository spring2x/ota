package com.iot.ota_upgrade.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UpgradeProperty {

	@Value("${device_token_expire_time}")
	private int deviceTokenExpiredTime;
	
	@Value("${upgrade_package_path}")
	private String upgradePackagePath;
	
	@Value("${SINGLE_PACKAGE_LENTH_UNIT}")
	private int singlePackageLenthUtil;
	
	@Value("${default_terminal_valide_code}")
	private String defaultTerminalValideCode;
	
	@Value("${package_file_download_path}")
	private String packageFileDownloadPath;
	
	@Value("${server.ip}")
	private String serverIp;
	
	@Value("${server.port}")
	private int serverPort;
	
	@Value("${device.req.statistics}")
	private boolean deviceReqStatistics;
	
	@Value("${file.init.test.time}")
	private int fileInitTestNum;
	
	@Value("${file.init.test.interval}")
	private int fileInitTestInterval;

	public boolean isDeviceReqStatistics() {
		return deviceReqStatistics;
	}

	public void setDeviceReqStatistics(boolean deviceReqStatistics) {
		this.deviceReqStatistics = deviceReqStatistics;
	}

	public int getDeviceTokenExpiredTime() {
		return deviceTokenExpiredTime;
	}

	public void setDeviceTokenExpiredTime(int deviceTokenExpiredTime) {
		this.deviceTokenExpiredTime = deviceTokenExpiredTime;
	}

	public String getUpgradePackagePath() {
		return upgradePackagePath;
	}

	public void setUpgradePackagePath(String upgradePackagePath) {
		this.upgradePackagePath = upgradePackagePath;
	}

	public int getSinglePackageLenthUtil() {
		return singlePackageLenthUtil;
	}

	public void setSinglePackageLenthUtil(int singlePackageLenthUtil) {
		this.singlePackageLenthUtil = singlePackageLenthUtil;
	}

	public String getDefaultTerminalValideCode() {
		return defaultTerminalValideCode;
	}

	public void setDefaultTerminalValideCode(String defaultTerminalValideCode) {
		this.defaultTerminalValideCode = defaultTerminalValideCode;
	}

	public String getPackageFileDownloadPath() {
		return packageFileDownloadPath;
	}

	public void setPackageFileDownloadPath(String packageFileDownloadPath) {
		this.packageFileDownloadPath = packageFileDownloadPath;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getFileInitTestNum() {
		return fileInitTestNum;
	}

	public void setFileInitTestNum(int fileInitTestNum) {
		this.fileInitTestNum = fileInitTestNum;
	}

	public int getFileInitTestInterval() {
		return fileInitTestInterval;
	}

	public void setFileInitTestInterval(int fileInitTestInterval) {
		this.fileInitTestInterval = fileInitTestInterval;
	}
	
	
	
}
