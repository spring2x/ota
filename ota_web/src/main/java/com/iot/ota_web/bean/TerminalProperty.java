package com.iot.ota_web.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TerminalProperty {

	@Value("${file_download_path}")
	private String fileDownloadPath;
	
	@Value("${upgrade_package_path}")
	private String upgradePackagePath;

	public String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public void setFileDownloadPath(String fileDownloadPath) {
		this.fileDownloadPath = fileDownloadPath;
	}

	public String getUpgradePackagePath() {
		return upgradePackagePath;
	}

	public void setUpgradePackagePath(String upgradePackagePath) {
		this.upgradePackagePath = upgradePackagePath;
	}
}
