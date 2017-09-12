package com.iot.ota_upgrade.message;

/**
 * 升级平台与设备交互消息，基本消息
 * @author tangliang
 *
 */
public class BasicMessage {
	//消息类型
	public byte messageType;
	//消息长度
	public byte messageLength;
	//消息校验和
	public byte checksum;
	
	//校验和验证结果
	public boolean checkSumResult;
	
	public byte getMessageType() {
		return messageType;
	}
	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
	public byte getMessageLength() {
		return messageLength;
	}
	public void setMessageLength(byte messageLength) {
		this.messageLength = messageLength;
	}
	public byte getChecksum() {
		return checksum;
	}
	public void setChecksum(byte checksum) {
		this.checksum = checksum;
	}
	public BasicMessage(byte messageType, byte messageLength, byte checksum) {
		super();
		this.messageType = messageType;
		this.messageLength = messageLength;
		this.checksum = checksum;
	}
	public boolean getCheckSumResult() {
		return checkSumResult;
	}
	public void setCheckSumResult(boolean checkSumResult) {
		this.checkSumResult = checkSumResult;
	}
	public BasicMessage() {
		super();
	}
	
	@Override
	public String toString() {
		return "messageType: " + messageType + "  messageLength: " + messageLength + "  checksum: " + checksum;
	}
}
