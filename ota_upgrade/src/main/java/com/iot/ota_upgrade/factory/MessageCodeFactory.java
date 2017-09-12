package com.iot.ota_upgrade.factory;

import com.iot.ota_upgrade.constant.DeviceUpDownConStant;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;
import com.iot.ota_upgrade.mina.code.impl.DeviceUpDownCodePro;
import com.iot.ota_upgrade.mina.code.impl.DeviceUpReqCodePro;
import com.iot.ota_upgrade.mina.code.impl.MessageExceptionCodePro;
import com.iot.ota_upgrade.mina.code.interf.CodeProcessorInterf;

/**
 * 具体消息编解码工厂
 * @author tangliang
 *
 */
public class MessageCodeFactory {
	
	/**
	 * 根据消息类型，获取编解码处理器。
	 * @param messageType 消息类型
	 */
	public static CodeProcessorInterf getCodeProcessor(int messageType){
		if (messageType == DeviceUpReqConstant.MESSAGE_TYPE || messageType == DeviceUpReqConstant.RESPONSE_MESSAGE_TYPE) {
			return new DeviceUpReqCodePro();
		}else if (messageType == DeviceUpDownConStant.REQUEST_MESSAGE_TYPE || messageType == DeviceUpDownConStant.RESPONSE_MESSAGE_TYPE) {
			return new DeviceUpDownCodePro();
		}else if (messageType == ExceptionMessageConstant.MESSAGE_TYPE) {
			return new MessageExceptionCodePro();
		}
		return null;
	}
	
}
