package com.iot.ota_upgrade.message;

public class DeviceUpReqMessage extends BasicMessage {
	// 授权码
	private String authCode;
	// 升级标识长度
	private byte upMarkLenth;
	// 升级标识
	private String upMark;
	// 升级包，单包长度
	private short singlePackageLength;
	// 设备标识
	private byte deviceMark;
	// 包标识
	private byte packageMark;
	// 包版本标识
	private byte versionMark;
	// 校验方式
	private byte validMark;
	// 传输模式
	private byte transMode;
	
	public DeviceUpReqMessage() {
		super();
	}
	
	public DeviceUpReqMessage(byte messageType, byte messageLength, byte checksum, String authCode, byte upMarkLenth,
			String upMark, short singlePackageLength, byte deviceMark, byte packageMark, byte versionMark,
			byte validMark, byte transMode) {
		super(messageType, messageLength, checksum);
		this.authCode = authCode;
		this.upMarkLenth = upMarkLenth;
		this.upMark = upMark;
		this.singlePackageLength = singlePackageLength;
		this.deviceMark = deviceMark;
		this.packageMark = packageMark;
		this.versionMark = versionMark;
		this.validMark = validMark;
		this.transMode = transMode;
	}

	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public byte getUpMarkLenth() {
		return upMarkLenth;
	}
	public void setUpMarkLenth(byte upMarkLenth) {
		this.upMarkLenth = upMarkLenth;
	}
	public String getUpMark() {
		return upMark;
	}
	public void setUpMark(String upMark) {
		this.upMark = upMark;
	}
	public short getSinglePackageLength() {
		return singlePackageLength;
	}
	public void setSinglePackageLength(short singlePackageLength) {
		this.singlePackageLength = singlePackageLength;
	}
	public byte getDeviceMark() {
		return deviceMark;
	}
	public void setDeviceMark(byte deviceMark) {
		this.deviceMark = deviceMark;
	}
	public byte getPackageMark() {
		return packageMark;
	}
	public void setPackageMark(byte packageMark) {
		this.packageMark = packageMark;
	}
	public byte getVersionMark() {
		return versionMark;
	}
	public void setVersionMark(byte versionMark) {
		this.versionMark = versionMark;
	}
	public byte getValidMark() {
		return validMark;
	}
	public void setValidMark(byte validMark) {
		this.validMark = validMark;
	}
	public byte getTransMode() {
		return transMode;
	}
	public void setTransMode(byte transMode) {
		this.transMode = transMode;
	}

	@Override
	public String toString() {
		return super.toString() + "  authCode: " + authCode + "  upMarkLenth: " + upMarkLenth + "  upMark: " + upMark
				+ "  singlePackageLength: " + singlePackageLength + "  deviceMark: " + deviceMark + "  packageMark: "
				+ packageMark + "  versionMark: " + versionMark + "  validMark: " + validMark + "  transMode: " + transMode;
	}
}
