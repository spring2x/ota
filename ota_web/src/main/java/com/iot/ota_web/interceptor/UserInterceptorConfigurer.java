package com.iot.ota_web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.iot.ota_web.service.UserService;


@Configuration
public class UserInterceptorConfigurer extends WebMvcConfigurerAdapter {

	@Autowired
	UserService userService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserInterceptor(userService)).addPathPatterns("/ota/user/**", "/ong/terminal", "/ong/terminal/**", "/ong/package", "/ong/package/**", "/ong/version", "/ong/version/**", "/ong/packageFile", "/ong/packageFile/**")
		.excludePathPatterns("/ota/user/login", "/ota/user/registor");
		super.addInterceptors(registry);
	}
}
