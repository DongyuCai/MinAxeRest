package com.jeeba.sys.entity;

import org.axe.annotation.persistence.Comment;
import org.axe.annotation.persistence.Table;
import org.axe.annotation.persistence.Unique;

import com.jeeba.common.entity.IdEntity;


/**  
 * Filename:    RoleResource.java  
 * 角色绑定资源
 * 角色1->n资源+type
 * 2015年12月30日	蔡东余		1.0			1.0 Version  
 */  
@Table(value="sys_role_resource")
public class RoleResource extends IdEntity{
	@Unique
	private long roleId;
	
	@Comment("资源id")
	@Unique
	private long resourceId;
	
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	
}
