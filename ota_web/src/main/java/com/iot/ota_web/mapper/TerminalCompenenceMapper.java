package com.iot.ota_web.mapper;

import java.util.List;
import java.util.Map;

import com.iot.ota_web.bean.TerminalCompenence;


public interface TerminalCompenenceMapper {

	
	List<TerminalCompenence> geTerminalCompenences(Map<String, Object> params);
}
