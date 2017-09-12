package com.iot.ota_upgrade.message;

/**
 * 设备升级包下载请求指令
 * @author liqiang
 *
 */
public class DeviceUpDownMessage extends BasicMessage {
	
	/**
	 * 终端类型的标识
	 */
	public byte deviceTypeMark;
	
	/**
	 * 升级包的标识
	 */
	public byte pacakgeMark;
	
	/**
	 * 升级包版本的标识
	 */
	public byte pacakgeVersionMark;
	
	/**
	 * 当前请求的升级包编号
	 */
	public short packageNo;
	
	public byte getDeviceTypeMark() {
		return deviceTypeMark;
	}

	public void setDeviceTypeMark(byte deviceTypeMark) {
		this.deviceTypeMark = deviceTypeMark;
	}

	public byte getPacakgeMark() {
		return pacakgeMark;
	}

	public void setPacakgeMark(byte pacakgeMark) {
		this.pacakgeMark = pacakgeMark;
	}

	public byte getPacakgeVersionMark() {
		return pacakgeVersionMark;
	}

	public void setPacakgeVersionMark(byte pacakgeVersionMark) {
		this.pacakgeVersionMark = pacakgeVersionMark;
	}

	public short getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(short packageNo) {
		this.packageNo = packageNo;
	}
	
	public DeviceUpDownMessage(byte messageType, byte messageLength, byte checksum, byte deviceTypeMark,
			byte pacakgeMark, byte pacakgeVersionMark, short packageNo) {
		super(messageType, messageLength, checksum);
		this.deviceTypeMark = deviceTypeMark;
		this.pacakgeMark = pacakgeMark;
		this.pacakgeVersionMark = pacakgeVersionMark;
		this.packageNo = packageNo;
	}

	public DeviceUpDownMessage(byte messageType, byte messageLength, byte checksum) {
		super(messageType, messageLength, checksum);
	}
	
	@Override
	public String toString() {
		return super.toString() + "  deviceTypeMark: " + deviceTypeMark + "  pacakgeMark: " + pacakgeMark
				+ "  pacakgeVersionMark: " + pacakgeVersionMark + "  packageNo: " + packageNo;
	}
}
