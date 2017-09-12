package com.iot.ota_web.bean;

public class PackageVersion {
	public Integer id;
	public Integer packageId;
	public String packageVersion;
	public String introduce;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public PackageVersion(Integer id, Integer packageId, String packageVersion, String introduce) {
		super();
		this.id = id;
		this.packageId = packageId;
		this.packageVersion = packageVersion;
		this.introduce = introduce;
	}
	public PackageVersion() {
		super();
	}
	
}
