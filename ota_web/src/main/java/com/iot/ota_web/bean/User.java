package com.iot.ota_web.bean;

import java.sql.Timestamp;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.iot.ota_web.valid.group.ValidGroup1;
import com.iot.ota_web.valid.group.ValidGroup2;

public class User {
	
	//用户id
	public Integer userId;
	
	//用户名
	@NotBlank(message="用户名不能为空", groups={ValidGroup1.class, ValidGroup2.class})
	public String name;
	
	
	//用户上一次登录时间
	public Timestamp lastLogin;
	
	
	//用户对应的token id
	public Integer userTokenId;
	
	
	//密码
	@NotBlank(message="密码不能为空", groups={ValidGroup1.class, ValidGroup2.class})
	@Pattern(regexp="(?=(.*[a-zA-Z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}", message="密码必须包含数字字母和特殊字符", groups={ValidGroup1.class, ValidGroup2.class})
	@Length(min=8, max=16, message="密码长度不能低于8位或高于16位", groups={ValidGroup1.class, ValidGroup2.class})
	public String password;
	
	//重复输入的密码
	@NotBlank(message="确认密码不能为空", groups={ValidGroup1.class})
	@Pattern(regexp="(?=(.*[a-zA-Z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}", message="确认密码格式错误", groups={ValidGroup1.class})
	@Length(min=8, max=16, message="确认密码格式错误", groups={ValidGroup1.class})
	public String repeatPassword;
	
	public Integer login_type;
	
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
	public Integer getUserTokenId() {
		return userTokenId;
	}
	public void setUserTokenId(Integer userTokenId) {
		this.userTokenId = userTokenId;
	}
	
	public Integer getLogin_type() {
		return login_type;
	}
	public void setLogin_type(Integer login_type) {
		this.login_type = login_type;
	}
	public User(Integer userId, String name, Timestamp lastLogin, Integer userTokenId, String password) {
		super();
		this.userId = userId;
		this.name = name;
		this.lastLogin = lastLogin;
		this.userTokenId = userTokenId;
		this.password = password;
	}
	public User() {
		super();
	}
	
	@Override
	public String toString() {
		return "userId:" + userId + " name:" + name + " password:" + password + " lastLogin:" + lastLogin + " tokenId:" + userTokenId;
	}
	
}
