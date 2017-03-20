package com.jeeba.sys.service;

import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;
import org.axe.exception.RestException;
import org.axe.util.StringUtil;

import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserRole;
import com.jeeba.sys.repository.UserRoleDao;

@Service
public class UserRoleService{
	
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	public UserRole saveEntity(UserRole userRole) {
		return userRoleDao.saveEntity(userRole);
	}

	@Tns
	public void editRole(long id, String roleIds){
		User user = userService.getEntity(id);
		if(user == null) throw new RestException("用户不存在");
		do{
			//先清空本来的所有角色
			userRoleDao.deleteByUserId(id);
			
			//再存新的角色
			if(roleIds == null) break;
			String[] roleId_ary = roleIds.split(",");
			//保存用户角色关系
			for(String roleId:roleId_ary){
				if(StringUtil.isNotEmpty(roleId)){
					UserRole userRole = new UserRole();
					userRole.setUserId(user.getId());
					userRole.setRoleId(Long.parseLong(roleId));
					this.saveEntity(userRole);
				}
			}
		}while(false);
	}
	
	public String getRolesStrByUserId(long userId){
		List<UserRole> userRoleList = this.getListByUserId(userId);
		return this.getRolesStrByUserRoleList(userRoleList);
	}
	
	public String getRolesStrByUserRoleList(List<UserRole> userRoleList){
		StringBuffer buf = new StringBuffer();
		if(userRoleList != null && userRoleList.size() > 0){
			buf.append(",");
			for(UserRole userRole:userRoleList){
				buf.append(userRole.getRoleId()).append(",");
			}
		}
		return buf.toString();
	}
	
	public List<UserRole> getListByUserId(long userId){
		return userRoleDao.getUserRoleListByUserId(userId);
	}
	
	public List<UserRole> getListByRoleId(long roleId){
		return userRoleDao.getUserRoleListByRoleId(roleId);
	}

	public UserRoleDao getBaseRepository() {
		return userRoleDao;
	}

}
