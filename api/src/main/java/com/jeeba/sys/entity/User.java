package com.jeeba.sys.entity;

import org.axe.annotation.persistence.Table;
import org.axe.annotation.persistence.Unique;

import com.jeeba.common.entity.IdEntity;

@Table(value="sys_user")
public class User extends IdEntity{//SSO未通，暂时使用本地User管理，User表id为long比较方便，但其余用到user_id的地方，都用String，与SSO保持一致
	
	@Unique
	private String username;//登录名，不可重复
	private String password;
	private String salt;
	
	private String name;
	private String email;
	private String mobile;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	//除了登录名，其他的都不能从接口返回给浏览器
	public User returnPublicUserInfo(){
		User user = new User();
		user.id = this.id;
		user.username = this.username;
		user.name = this.name;
		user.email = this.email;
		user.mobile = this.mobile;
		return user;
	}
	
}
