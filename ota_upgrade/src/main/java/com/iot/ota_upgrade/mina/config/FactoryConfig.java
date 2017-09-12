package com.iot.ota_upgrade.mina.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.factory.DeviceServiceFactory;
import com.iot.ota_upgrade.mina.service.interf.DeviceActionServiceInterf;
import com.iot.ota_upgrade.service.DeviceTokenService;
import com.iot.ota_upgrade.service.InitPackageFileService;

@Configuration
public class FactoryConfig {
	@Bean
    public DeviceServiceFactory getDeviceServiceFactory(DeviceTokenService deviceTokenService, UpgradeProperty upgradeProperty, InitPackageFileService initPackageFileService){
        return new DeviceServiceFactory(deviceTokenService, upgradeProperty, initPackageFileService);
    }
	
	@Bean(name="deviceUpReqService")
	public DeviceActionServiceInterf getDeviceUpReqService(DeviceServiceFactory deviceServiceFactory){
		return deviceServiceFactory.getDeviceActionService("deviceUpReqService");
	}
	
	@Bean(name="deviceUpDownService")
	public DeviceActionServiceInterf getDeviceUpDownService(DeviceServiceFactory deviceServiceFactory){
		return deviceServiceFactory.getDeviceActionService("deviceUpDownService");
	}
}
