package com.iot.ota_upgrade.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.bean.UpgradeProperty;
import com.iot.ota_upgrade.util.ExceptionUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class InitPackageFileService {
	
	private Logger logger = LogManager.getLogger(InitPackageFileService.class);

	@Autowired RestTemplate template;
	
	@Autowired UpgradeProperty upgradeProperty;
	
	@HystrixCommand(fallbackMethod="fallback", commandKey="initDownLoadFile", groupKey="downLoadFileGroup", threadPoolKey="initDownLoadFileThread",
			commandProperties={@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="60000")})
	public JSONObject initDownLoadFile(JSONObject body){
		body.put("responseUrl", "http://" + upgradeProperty.getServerIp() + ":" + upgradeProperty.getServerPort() + "/downloadFile");
		ResponseEntity<String> responseEntity = template.postForEntity(upgradeProperty.getPackageFileDownloadPath(), 
				body, String.class);
		String result = responseEntity.getBody();
		JSONObject jsonResult = (JSONObject) JSONObject.parse(result);
		return jsonResult;
	}
	
	public JSONObject fallback(JSONObject body, Throwable e){
		ExceptionUtil.printExceptionToLog(logger, new Exception(e));
		JSONObject result = new JSONObject();
		result.put("message", e.getMessage());
		return result;
	}
}
