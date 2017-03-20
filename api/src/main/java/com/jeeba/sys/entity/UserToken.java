package com.jeeba.sys.entity;

import java.util.Date;

import org.axe.annotation.persistence.Id;
import org.axe.annotation.persistence.Table;

@Table(value="sys_user_token")
public class UserToken {
	@Id
	private long userId;
	
	private String username;
	
	private String token;
	
	
	private Date expire;//token暂留时间，如果1天时间没操作，就过期
	private Date cookieExpire;//token自动登录，留在cookie中的失效时间，默认一个月

	private String ip;

	private Date creatTime;
	
	private Date updateTime;
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public Date getCookieExpire() {
		return cookieExpire;
	}

	public void setCookieExpire(Date cookieExpire) {
		this.cookieExpire = cookieExpire;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
