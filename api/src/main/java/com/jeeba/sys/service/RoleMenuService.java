package com.jeeba.sys.service;

import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;

import com.jeeba.sys.entity.RoleMenu;
import com.jeeba.sys.repository.RoleMenuDao;


@Service
public class RoleMenuService {
	
	@Autowired
	private RoleMenuDao roleMenuDao;
	
	
	@Tns
	public void setMenusOfRole(long roleId,String menuIds){
		do{
			//先清空本来角色下的所有菜单
			roleMenuDao.deleteByRoleId(roleId);
			
			//再存新的角色菜单
			if(menuIds == null) break;
			String[] menuId_ary = menuIds.split(",");
			for(String menuId : menuId_ary){
				if(menuId == null || "".equals(menuId.trim()))
					continue;
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(roleId);
				roleMenu.setMenuId(menuId);
				
				roleMenuDao.saveEntity(roleMenu);
			}
		}while(false);
	}
	
	public String getMenusStrByRoleId(long roleId){
		List<RoleMenu> roleMenuList = this.getListByRoleId(roleId);
		return this.getMenusStrByRoleMenuList(roleMenuList);
	}
	
	public String getMenusStrByRoleMenuList(List<RoleMenu> roleMenuList){
		StringBuffer buf = new StringBuffer();
		if(roleMenuList != null && roleMenuList.size() > 0){
			buf.append(",");
			for(RoleMenu roleMenu:roleMenuList){
				buf.append(roleMenu.getMenuId()).append(",");
			}
		}
		return buf.toString();
	}
	
	public List<RoleMenu> getListByRoleId(long roleId){
		return roleMenuDao.getListByRoleId(roleId);
	}

	public RoleMenuDao getBaseRepository() {
		return roleMenuDao;
	}
}
