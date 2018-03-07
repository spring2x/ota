package com.iot.ota_upgrade.factory;

import org.springframework.data.redis.core.ValueOperations;

import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.mina.service.impl.DeviceUpDownService;
import com.iot.ota_upgrade.mina.service.impl.DeviceUpReqService;
import com.iot.ota_upgrade.mina.service.interf.DeviceActionServiceInterf;
import com.iot.ota_upgrade.service.DeviceTokenService;
import com.iot.ota_upgrade.service.InitPackageFileService;

public class DeviceServiceFactory {
	
	private DeviceTokenService deviceTokenService;
	private UpgradeProperty upgradeProperty;
	private InitPackageFileService initPackageFileService;
	private ValueOperations<String, Object> valueOperations;
	
	public DeviceActionServiceInterf getDeviceActionService(String messageType) {
		if ("deviceUpReqService".equals(messageType)) {
			DeviceUpReqService deviceUpReqService = new DeviceUpReqService();
			deviceUpReqService.setDeviceTokenService(deviceTokenService);
			deviceUpReqService.setUpgradeProperty(upgradeProperty);
			deviceUpReqService.setInitPackageFileService(initPackageFileService);
			deviceUpReqService.setValueOperations(valueOperations);
			return deviceUpReqService;
		}else if ("deviceUpDownService".equals(messageType)) {
			return new DeviceUpDownService(upgradeProperty);
		}
		return null;
	}

	public DeviceServiceFactory(DeviceTokenService deviceTokenService, UpgradeProperty upgradeProperty, InitPackageFileService initPackageFileService, ValueOperations<String, Object> valueOperations) {
		super();
		this.deviceTokenService = deviceTokenService;
		this.upgradeProperty = upgradeProperty;
		this.initPackageFileService = initPackageFileService;
		this.valueOperations = valueOperations;
	}

	public DeviceTokenService getDeviceTokenService() {
		return deviceTokenService;
	}

	public void setDeviceTokenService(DeviceTokenService deviceTokenService) {
		this.deviceTokenService = deviceTokenService;
	}

	public UpgradeProperty getUpgradeProperty() {
		return upgradeProperty;
	}

	public void setUpgradeProperty(UpgradeProperty upgradeProperty) {
		this.upgradeProperty = upgradeProperty;
	}

	public InitPackageFileService getInitPackageFileService() {
		return initPackageFileService;
	}

	public void setInitPackageFileService(InitPackageFileService initPackageFileService) {
		this.initPackageFileService = initPackageFileService;
	}
}
