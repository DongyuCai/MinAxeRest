package com.jeeba.sys.entity;

import org.axe.annotation.persistence.Table;

import com.jeeba.common.entity.IdEntity;


 
/**  
 * Filename:    UserRole.java  
 * Description:   
 * Copyright:   Copyright (c)2015
 * @author:     李豪杰 
 * @version:    1.0  
 * Create at:   2015年12月31日 上午11:17:17  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2015年12月31日      李豪杰                   1.0        1.0 Version  
 */  
@Table(value="sys_user_role")
public class UserRole extends IdEntity{

	private long userId;
	
	private long roleId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
}
