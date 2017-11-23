package com.iot.ota_upgrade.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.mina.service.impl.DeviceUpReqService;
import com.iot.ota_upgrade.util.FileUtil;

@RestController
@RequestMapping("/downloadFile")
public class InitDownLoadFileController {
	
	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public void initDownLoadFile(HttpServletRequest request, HttpServletResponse response){
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		String terminalMark = multipartHttpServletRequest.getParameter("terminal");
		String packageMark = multipartHttpServletRequest.getParameter("package");
		String versionMark = multipartHttpServletRequest.getParameter("version");
		String valideCode = multipartHttpServletRequest.getParameter("valideCode");
		JSONObject valideCodeJson = (JSONObject) JSONObject.parse(valideCode);
		String fileMark = terminalMark + File.separator + packageMark + File.separator + versionMark;
		
		Map<Integer, String> valideMap = new HashMap<>();
		for(Entry<String, Object> valideCodeEntry : valideCodeJson.entrySet()){
			String vlaideCodeKey = valideCodeEntry.getKey();
			if ("crcCode".equals(vlaideCodeKey)) {
				valideMap.put(1, (String) valideCodeEntry.getValue());
			}else if ("md5Code".equals(vlaideCodeKey)) {
				valideMap.put(2, (String) valideCodeEntry.getValue());
			}else if ("sha1Code".equals(vlaideCodeKey)) {
				valideMap.put(3, (String) valideCodeEntry.getValue());
			}
		}
		
		synchronized (DeviceUpReqService.fileValideCodeMap) {
			DeviceUpReqService.fileValideCodeMap.put(fileMark, valideMap);
			DeviceUpReqService.fileValideCodeMap.notifyAll();
		}
		
		Map<String, MultipartFile> fileMaps = multipartHttpServletRequest.getFileMap();
		for (Entry<String, MultipartFile> fileMap : fileMaps.entrySet()) {
			MultipartFile file = fileMap.getValue();
			try {
				FileUtil.initDownLoadFile(fileMark, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
