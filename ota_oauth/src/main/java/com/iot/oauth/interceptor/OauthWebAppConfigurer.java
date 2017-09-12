package com.iot.oauth.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.iot.oauth.mapper.TokenMapper;

@Configuration
public class OauthWebAppConfigurer extends WebMvcConfigurerAdapter {

	@Autowired
	TokenMapper tokenMapper;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DeviceInterceptor(tokenMapper)).addPathPatterns("/device/**");
		super.addInterceptors(registry);
	}
}
