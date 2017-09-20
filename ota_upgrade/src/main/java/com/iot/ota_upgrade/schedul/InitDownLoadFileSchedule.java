package com.iot.ota_upgrade.schedul;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.mina.service.impl.DeviceUpReqService;
import com.iot.ota_upgrade.service.InitPackageFileService;

@Component
public class InitDownLoadFileSchedule {
	
	private Logger logger = LogManager.getLogger(InitDownLoadFileSchedule.class);

	public static Set<String> fileMarkSet = new HashSet<>();
	
	@Autowired
	InitPackageFileService initPackageFileService;
	
	@Scheduled(fixedRate=2000)
	public void init(){
		for (String fileMark : fileMarkSet) {
			if (!DeviceUpReqService.fileValideCodeMap.containsKey(fileMark)) {
				String[] fileMarkInfos = fileMark.split("\\" + File.separator);
				JSONObject body = new JSONObject();
				body.put("terminal", fileMarkInfos[0]);
				body.put("package", fileMarkInfos[1]);
				body.put("version", fileMarkInfos[2]);
				System.out.println(fileMark + "\t" + "start inted");
				JSONObject initResult = initPackageFileService.initDownLoadFile(body);
				if ("0001".equals(initResult.get("code"))) {
					logger.error(initResult.get("message"));
				}
			}
		}
	}
	
}
