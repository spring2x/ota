package com.iot.oauth.bean;

public class PackageVersion {
	public Integer versionId;
	public Integer packageId;
	public Integer terminalId;
	public String packageVersion;
	public String introduce;
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public String getPackageVersion() {
		return packageVersion;
	}
	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	
	public Integer getVersionId() {
		return versionId;
	}
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public PackageVersion(Integer versionId, Integer packageId, String packageVersion, String introduce, Integer terminalId) {
		super();
		this.versionId = versionId;
		this.packageId = packageId;
		this.packageVersion = packageVersion;
		this.introduce = introduce;
		this.terminalId = terminalId;
	}
	public PackageVersion() {
		super();
	}
	
}
