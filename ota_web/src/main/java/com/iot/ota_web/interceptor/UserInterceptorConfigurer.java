package com.iot.ota_web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.iot.ota_web.mapper.TokenMapper;
import com.iot.ota_web.service.UserService;


@Configuration
public class UserInterceptorConfigurer extends WebMvcConfigurerAdapter {

	@Autowired
	TokenMapper tokenMapper;
	@Autowired
	UserService userService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserInterceptor(tokenMapper, userService)).addPathPatterns("/ota/user/**").excludePathPatterns("/ota/user/login", "/ota/user/registor", "/ota/user/logout", "/ota/user/refreshToken");
		super.addInterceptors(registry);
	}
}
