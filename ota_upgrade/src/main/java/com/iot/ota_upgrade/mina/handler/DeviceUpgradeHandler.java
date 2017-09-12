package com.iot.ota_upgrade.mina.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.BasicDeviceConstant;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.constant.ExceptionMessageConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.mina.service.interf.DeviceActionServiceInterf;
import com.iot.ota_upgrade.util.RequestStatisticsUtil;


@Component("deviceUpgradeHandler")
public class DeviceUpgradeHandler extends IoHandlerAdapter implements ApplicationContextAware{
	
	private static Logger logger = LogManager.getLogger(DeviceUpgradeHandler.class.getName());
	
	private ApplicationContext applicationContext;
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("remote client [" + session.getRemoteAddress().toString() + "] opened.");
		super.sessionOpened(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("remote client [" + session.getRemoteAddress().toString() + "] connected.");
		RequestStatisticsUtil.currentRequestNum.getAndIncrement();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		BasicMessage basicMessage = (BasicMessage)message;
		
		//未经过授权的设备，直接关闭session
		if (basicMessage.messageType != DeviceUpReqConstant.MESSAGE_TYPE && !session.containsAttribute("remote_addr")) {
			session.close(true);
			return;
		}
		JSONObject result = new JSONObject();
		boolean checkSumResult = basicMessage.getCheckSumResult();
		
		//校验成功的情况
		if (checkSumResult) {
			String serverName = BasicDeviceConstant.DEVICE_SERVICE_MAP.get((int)basicMessage.getMessageType());
			DeviceActionServiceInterf service = applicationContext.getBean(serverName, DeviceActionServiceInterf.class);
			try {
				service.process(session, basicMessage, result);
				//logger.debug(result.toJSONString());
				session.write(result);
				if (result.containsKey("ERR_MESSAGE_CODE_MARK") && result.getIntValue("ERR_MESSAGE_CODE_MARK") == ExceptionMessageConstant.VALIDE_FAIL) {
					session.close(false);
				}
			} catch (Exception e) {
				result.clear();
				result.put(ExceptionMessageConstant.MESSAGE_TYPE_KEY, ExceptionMessageConstant.MESSAGE_TYPE);
				result.put(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK, basicMessage.messageType);
				result.put(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK, ExceptionMessageConstant.SERVER_ERR);
				session.write(result);
				logger.error(e.getMessage());
			}
		}else {
			result.clear();
			result.put(ExceptionMessageConstant.MESSAGE_TYPE_KEY, ExceptionMessageConstant.MESSAGE_TYPE);
			result.put(ExceptionMessageConstant.ERR_MESSAGE_ID_MARK, basicMessage.messageType);
			result.put(ExceptionMessageConstant.ERR_MESSAGE_CODE_MARK, ExceptionMessageConstant.CHECK_SUM_ERR);
			session.write(result);
			if (DeviceUpReqConstant.MESSAGE_TYPE == basicMessage.messageType) {
				session.close(false);
			}
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.debug("session idle, so disconnecting......");
		session.close(false);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		RequestStatisticsUtil.currentRequestNum.getAndDecrement();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.debug("session occured exception, so close it." + cause.getMessage());
		session.close(false);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
