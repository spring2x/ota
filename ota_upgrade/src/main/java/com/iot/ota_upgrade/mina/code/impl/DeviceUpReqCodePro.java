package com.iot.ota_upgrade.mina.code.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_upgrade.constant.DeviceUpReqConstant;
import com.iot.ota_upgrade.message.BasicMessage;
import com.iot.ota_upgrade.message.DeviceUpReqMessage;
import com.iot.ota_upgrade.util.ByteUtil;
import com.iot.ota_upgrade.util.MinaUtil;

/**
 * 设备升级请求编解码
 * @author tangliang
 *
 */
public class DeviceUpReqCodePro extends BasicCodeProcessor {
	
	//Logger logger = LogManager.getLogger(DeviceUpReqCodePro.class.getName());
	
	/**
	 * 老版协议解码
	 * @param basicMessage
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	public DeviceUpReqMessage oldDecode(BasicMessage basicMessage, IoBuffer buffer) throws Exception {
		//授权码
		String authCode = MinaUtil.getStringFromIoBuffer(buffer, DeviceUpReqConstant.UPGRADE_AUTH_CODE_LENTH, DeviceUpReqConstant.CHARSET);
		//升级包，单包长度
		Short singlePackageLength = buffer.getShort();
		//设备标识
		byte deviceMark = buffer.get();
		//包标识
		byte packageMark = buffer.get();
		//包版本标识
		byte versionMark = buffer.get();
		//校验方式
		byte validMark = buffer.get();
		//传输模式
		byte transMode = buffer.get();
		//升级标识长度
		byte upMarkLenth = buffer.get();
		//升级标识
		String upMark = MinaUtil.getStringFromIoBuffer(buffer, upMarkLenth, DeviceUpReqConstant.CHARSET);
		
		DeviceUpReqMessage deviceUpReqMessage = new DeviceUpReqMessage(basicMessage.getMessageType(), basicMessage.getMessageLength(),
				basicMessage.getChecksum(), authCode, upMarkLenth, upMark, singlePackageLength, deviceMark, packageMark,
				versionMark, validMark, transMode);
		//计算的校验码与终端发送的校验码是否一致
		deviceUpReqMessage
				.setCheckSumResult(basicMessage.getChecksum() == calculateCheckSum(deviceUpReqMessage) ? true : false);
		//logger.debug(deviceUpReqMessage.toString());
		return deviceUpReqMessage;
	}
	
	@Override
	public DeviceUpReqMessage decode(BasicMessage basicMessage, IoBuffer buffer) throws Exception {
		DeviceUpReqMessage deviceUpReqMessage = null;
		int bodyLength = 0;
		try {
			//授权码
			String authCode = MinaUtil.getStringFromIoBuffer(buffer, DeviceUpReqConstant.UPGRADE_AUTH_CODE_LENTH, DeviceUpReqConstant.CHARSET);
			//升级包，单包长度
			Short singlePackageLength = buffer.getShort();
			//设备标识
			short deviceMark = buffer.getShort();
			//包标识
			short packageMark = buffer.getShort();
			//包版本标识
			short versionMark = buffer.getShort();
			//校验方式
			byte validMark = buffer.get();
			//传输模式
			byte transMode = buffer.get();
			//升级标识长度
			byte upMarkLenth = buffer.get();
			//升级标识
			String upMark = MinaUtil.getStringFromIoBuffer(buffer, upMarkLenth, DeviceUpReqConstant.CHARSET);
			
			deviceUpReqMessage = new DeviceUpReqMessage(basicMessage.getMessageType(), basicMessage.getMessageLength(),
					basicMessage.getChecksum(), authCode, upMarkLenth, upMark, singlePackageLength, deviceMark, packageMark,
					versionMark, validMark, transMode);
			//计算的校验码与终端发送的校验码是否一致
			deviceUpReqMessage
					.setCheckSumResult(basicMessage.getChecksum() == calculateCheckSum(deviceUpReqMessage) ? true : false);
			//logger.debug(deviceUpReqMessage.toString());
			bodyLength += authCode.length() + 2 + 2 + 2 + 2 + 1 + 1 + 1 + upMarkLenth;
			if (bodyLength > basicMessage.messageLength) {
				buffer.buf().position(3);
				deviceUpReqMessage = oldDecode(basicMessage, buffer);
			}
		} catch (Exception e) {
			buffer.buf().position(3);
			deviceUpReqMessage = oldDecode(basicMessage, buffer);
		}
		return deviceUpReqMessage;
	} 
	
	
	
	
	/**
	 * 计算消息的校验和.
	 */
	private static byte calculateCheckSum(DeviceUpReqMessage deviceUpReqMessage) {
		byte checkSum = 0;
		String authCode = deviceUpReqMessage.getAuthCode();
		for (int index = 0; index < authCode.length(); index ++) {
			checkSum ^= authCode.charAt(index);
		}
		
		byte[] packageLengthBytes = ByteUtil.shortToByteArray(deviceUpReqMessage.getSinglePackageLength());
		for(byte packageLengthByte : packageLengthBytes){
			checkSum ^= packageLengthByte;
		}
		
		checkSum ^= deviceUpReqMessage.getDeviceMark();
		checkSum ^= deviceUpReqMessage.getPackageMark();
		checkSum ^= deviceUpReqMessage.getVersionMark();
		checkSum ^= deviceUpReqMessage.getValidMark();
		checkSum ^= deviceUpReqMessage.getTransMode();
		
		checkSum ^= deviceUpReqMessage.getUpMarkLenth();
		String upMark = deviceUpReqMessage.getUpMark();
		for(int index = 0; index < upMark.length(); index ++){
			checkSum ^= upMark.charAt(index);;
		}
		return checkSum;
	}
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {
		JSONObject encodeMessage = (JSONObject)message;
		
		//buffer的容量
		int capacity = 0;
		
		byte messageType = encodeMessage.getByteValue(DeviceUpReqConstant.MESSAGE_TYPE_KEY);
		capacity += 1;
		byte messageLenth = encodeMessage.getByteValue(DeviceUpReqConstant.MESSAGE_LENTH_KEY);
		capacity += 1;
		byte checkSum = encodeMessage.getByteValue(DeviceUpReqConstant.CHECK_SUM_KEY);
		capacity += 1;
		int packageSize = encodeMessage.getIntValue(DeviceUpReqConstant.PACKAGE_SIZE_KEY);
		capacity += 4;
		short packageNum = encodeMessage.getShort(DeviceUpReqConstant.SPLIT_PACKAGE_NUM_KEY);
		capacity += 2;
		String packageValideCode = encodeMessage.getString(DeviceUpReqConstant.PACKAGE_VALIDE_CODE_KEY);
		capacity += packageValideCode.length();
		
		//logger.debug(encodeMessage.toJSONString());
		IoBuffer buffer = IoBuffer.allocate(capacity, false);
		buffer.setAutoExpand(true);
		buffer.put(messageType);
		buffer.put(messageLenth);
		buffer.put(checkSum);
		buffer.putInt(packageSize);
		buffer.putShort(packageNum);
		buffer.put(packageValideCode.getBytes());
		buffer.flip();
		out.write(buffer);
	}
}
