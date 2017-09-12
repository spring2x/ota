package com.iot.ota_web.bean;

/**
 * 升级包
 * @author liqiang
 *
 */
public class Package {
	public Integer id;
	public Integer terminalId;
	public String packageName;
	public String introduce;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Package(Integer id, Integer terminalId, String packageName, String introduce) {
		super();
		this.id = id;
		this.terminalId = terminalId;
		this.packageName = packageName;
		this.introduce = introduce;
	}
	public Package() {
		super();
	}
	
}
