package com.iot.ota_upgrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;

@Service
public class InitPackageFileService {

	@Autowired RestTemplate template;
	
	@Autowired UpgradeProperty upgradeProperty;
	
	public JSONObject initDownLoadFile(JSONObject body){
		body.put("responseUrl", "http://" + upgradeProperty.getServerIp() + ":" + upgradeProperty.getServerPort() + "/downloadFile");
		ResponseEntity<String> responseEntity = template.postForEntity(upgradeProperty.getPackageFileDownloadPath(), 
				body, String.class);
		String result = responseEntity.getBody();
		JSONObject jsonResult = (JSONObject) JSONObject.parse(result);
		return jsonResult;
	}
	
}
