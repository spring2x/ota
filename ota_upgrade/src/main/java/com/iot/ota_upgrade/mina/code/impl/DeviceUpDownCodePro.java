package com.iot.ota_upgrade.mina.code.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.DeviceUpDownConStant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpDownMessage;
import com.iot.ota_upgrade.util.ByteUtil;

/**
 * 设备升级包下载   编解码
 * @author liqiang
 *
 */
public class DeviceUpDownCodePro extends BasicCodeProcessor {
	//static Logger logger = LogManager.getLogger(DeviceUpReqCodePro.class.getName());
	
	public BasicMessage oldDecode(BasicMessage basicMessage, IoBuffer buffer) throws Exception {
		//logger.debug("*****************************  " + "message decode start...");
		byte deviceTypeMark = buffer.get();
		byte packageMark = buffer.get();
		byte packageVersionMark = buffer.get();
		short packageNo = buffer.getShort();
		
		DeviceUpDownMessage deviceUpDownMessage = new DeviceUpDownMessage(basicMessage.getMessageType(),
				basicMessage.getMessageLength(), basicMessage.getChecksum(), deviceTypeMark, packageMark,
				packageVersionMark, packageNo);
		//计算的校验码与终端发送的校验码是否一致
		deviceUpDownMessage
						.setCheckSumResult(basicMessage.getChecksum() == calculateCheckSum(deviceUpDownMessage) ? true : false);
		//logger.debug("*****************************  " + "message decode end");
		//logger.debug(deviceUpDownMessage.toString());
		return deviceUpDownMessage;
	}
	
	@Override
	public BasicMessage decode(BasicMessage basicMessage, IoBuffer buffer) throws Exception {
		//logger.debug("*****************************  " + "message decode start...");
		
		DeviceUpDownMessage deviceUpDownMessage = null;
		int bodyLength = 0;
		try {
			short deviceTypeMark = buffer.getShort();
			short packageMark = buffer.getShort();
			short packageVersionMark = buffer.getShort();
			short packageNo = buffer.getShort();
			
			deviceUpDownMessage = new DeviceUpDownMessage(basicMessage.getMessageType(),
					basicMessage.getMessageLength(), basicMessage.getChecksum(), deviceTypeMark, packageMark,
					packageVersionMark, packageNo);
			//计算的校验码与终端发送的校验码是否一致
			deviceUpDownMessage
							.setCheckSumResult(basicMessage.getChecksum() == calculateCheckSum(deviceUpDownMessage) ? true : false);
			//logger.debug("*****************************  " + "message decode end");
			//logger.debug(deviceUpDownMessage.toString());
			bodyLength += 2 + 2 + 2 + 2;
			if (bodyLength > basicMessage.messageLength) {
				buffer.buf().position(3);
				deviceUpDownMessage = (DeviceUpDownMessage) oldDecode(basicMessage, buffer);
			}
		} catch (Exception e) {
			buffer.buf().position(3);
			deviceUpDownMessage = (DeviceUpDownMessage) oldDecode(basicMessage, buffer);
		}
		
		return deviceUpDownMessage;
	}
	
	/**
	 * 计算消息的校验和.
	 */
	private static byte calculateCheckSum(DeviceUpDownMessage deviceUpDownMessage) {
		byte checkSum = 0;
		
		checkSum ^= deviceUpDownMessage.getDeviceTypeMark();
		checkSum ^= deviceUpDownMessage.getPacakgeMark();
		checkSum ^= deviceUpDownMessage.getPacakgeVersionMark();
		byte[] packageNoBytes = ByteUtil.shortToByteArray(deviceUpDownMessage.getPackageNo());
		for(byte packageNoByte : packageNoBytes){
			checkSum ^= packageNoByte;
		}
		return checkSum;
	}
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {
		JSONObject encodeMessage = (JSONObject)message;
		
		//buffer的容量
		int capacity = 0;
		
		byte messageType = encodeMessage.getByteValue(DeviceUpDownConStant.MESSAGE_TYPE_KEY);
		capacity += 1;
		int messageLenth = encodeMessage.getIntValue(DeviceUpDownConStant.MESSAGE_LENTH_KEY);
		capacity += 4;
		byte checkSum = encodeMessage.getByteValue(DeviceUpDownConStant.CHECK_SUM_KEY);
		capacity += 1;
		short packageNo = encodeMessage.getShort(DeviceUpDownConStant.PACKAGE_NO_KEY);
		capacity += 2;
		byte[] packageBytesData = encodeMessage.getBytes(DeviceUpDownConStant.PACKAGE_BYTE_DATA_KEY);
		capacity += packageBytesData.length;
		//logger.debug(encodeMessage.toJSONString());
		IoBuffer buffer = IoBuffer.allocate(capacity, false);
		buffer.setAutoExpand(true);
		buffer.put(messageType);
		buffer.putInt(messageLenth);
		buffer.put(checkSum);
		buffer.putShort(packageNo);
		buffer.put(packageBytesData);
		buffer.flip();
		out.write(buffer);
	}
	
}
