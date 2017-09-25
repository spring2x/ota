package com.iot.ota_gateway.filter;


import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;

@Component
public class OtaTestFilter extends ZuulFilter {

	@Override
	public Object run() {
		Exception e = new RuntimeException("This is a test");
		throw new ZuulRuntimeException(e);
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "post";
	}

}
