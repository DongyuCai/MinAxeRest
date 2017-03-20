package com.jeeba.sys.entity;

import org.axe.annotation.persistence.Table;

import com.jeeba.common.entity.IdEntity;

/**  
 * Filename:    RoleMenu.java  
 * Description:   角色菜单
 * Copyright:   Copyright (c)2015
 * @author:     李豪杰 
 * @version:    1.0  
 * Create at:   2015年12月30日 上午10:54:21  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2015年12月30日      李豪杰                   1.0        1.0 Version  
 */  
@Table(value="sys_role_menu")
public class RoleMenu extends IdEntity{
	
	private long roleId;
	
	private String menuId;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
}
