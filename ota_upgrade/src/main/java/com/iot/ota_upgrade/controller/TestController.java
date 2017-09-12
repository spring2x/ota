package com.iot.ota_upgrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("")
public class TestController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping(value="test")
	public String test(){
		return restTemplate.getForEntity("http://ota-web/test", String.class).getBody();
	}
}
