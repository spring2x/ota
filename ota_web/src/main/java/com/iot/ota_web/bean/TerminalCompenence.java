package com.iot.ota_web.bean;

public class TerminalCompenence {
	
	public Integer id;
	public Integer type;
	public String name;
	public String introduce;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public TerminalCompenence(Integer id, Integer type, String name, String introduce) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.introduce = introduce;
	}
	public TerminalCompenence() {
		super();
	}
	
	
	
}
