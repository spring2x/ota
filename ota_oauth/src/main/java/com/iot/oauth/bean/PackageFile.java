package com.iot.oauth.bean;

import java.util.Date;

public class PackageFile {
	public Integer id;
	public Integer terminalId;
	public Integer packageId;
	public Integer packageVersionId;
	public Date createTime;
	public Integer createPeopleId;
	public String fileName;
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
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public Integer getPackageVersionId() {
		return packageVersionId;
	}
	public void setPackageVersionId(Integer packageVersionId) {
		this.packageVersionId = packageVersionId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getCreatePeopleId() {
		return createPeopleId;
	}
	public void setCreatePeopleId(Integer createPeopleId) {
		this.createPeopleId = createPeopleId;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public PackageFile(Integer id, Integer terminalId, Integer packageId, Integer packageVersionId, Date createTime,
			Integer createPeopleId, String fileName) {
		super();
		this.id = id;
		this.terminalId = terminalId;
		this.packageId = packageId;
		this.packageVersionId = packageVersionId;
		this.createTime = createTime;
		this.createPeopleId = createPeopleId;
		this.fileName = fileName;
	}
	public PackageFile() {
		super();
	}
	
	
	
}
