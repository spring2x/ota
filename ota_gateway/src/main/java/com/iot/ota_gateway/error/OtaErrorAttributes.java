package com.iot.ota_gateway.error;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;


/**
 * 自定义异常信息
 * @author tangliang
 *
 */
@Component
public class OtaErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
		Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
		//result.remove("exception");
		return result;
	}
}
