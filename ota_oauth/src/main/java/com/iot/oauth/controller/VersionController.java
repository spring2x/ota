package com.iot.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iot.oauth.bean.PackageVersion;
import com.iot.oauth.service.VersionServive;


@RestController
@RequestMapping("/ota/version")
public class VersionController {
	
	@Autowired
	private VersionServive versionService;

	@RequestMapping(value="/info", method={RequestMethod.GET})
	public String versionInfo(PackageVersion version){
		return versionService.getInfo(version);
	}
	
}
