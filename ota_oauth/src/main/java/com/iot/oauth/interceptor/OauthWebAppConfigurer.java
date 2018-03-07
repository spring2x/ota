package com.iot.oauth.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class OauthWebAppConfigurer extends WebMvcConfigurerAdapter {

	@Autowired
	ValueOperations<String, Object> valueOperations;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DeviceInterceptor(valueOperations)).addPathPatterns("/ota/device", "/ota/version", "/ota/device/**", "/ota/version/**");
		super.addInterceptors(registry);
	}
}
