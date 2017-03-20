package com.jeeba.sys.entity;

import org.axe.annotation.persistence.Table;
import org.axe.annotation.persistence.Unique;

import com.jeeba.common.entity.IdEntity;

/**  
 * Filename:    Role.java  
 * Description:   角色
 * Copyright:   Copyright (c)2015
 * @author:     李豪杰 
 * @version:    1.0  
 * Create at:   2015年12月30日 上午10:51:14  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2015年12月30日      李豪杰                   1.0        1.0 Version  
 */  
@Table(value="sys_role")
public class Role extends IdEntity{
	
	@Unique
	private String name;//角色名，不可重复
	
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	

}
