package com.jeeba.sys.service;

import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;

import com.jeeba.sys.entity.RoleResource;
import com.jeeba.sys.repository.RoleResourceDao;

@Service
public class RoleResourceService {
	@Autowired
	private RoleResourceDao roleResourceDao;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	
	
	@Tns
	public void setResourcesOfRole(long roleId,String resourceIds){
		do{
			//先清空本来角色下的所有资源
			roleResourceDao.deleteByRoleId(roleId);
			
			//再存新的角色资源
			if(resourceIds == null) break;
			String[] resourceId_ary = resourceIds.split(",");
			for(String resourceId : resourceId_ary){
				if(resourceId == null || "".equals(resourceId.trim()))
					continue;
				RoleResource roleResource = new RoleResource();
				roleResource.setRoleId(roleId);
				roleResource.setResourceId(Long.parseLong(resourceId));
				
				roleResourceDao.saveEntity(roleResource);
			}
		}while(false);
	}
	
	@Tns
	public void setRolesOfResource(long resourceId,String roleIds){
		do{
			//不用清空，这是一部一部加的
			roleResourceDao.deleteByResourceId(resourceId);
			
			//保存新的角色
			if(roleIds == null) break;
			String[] roleId_ary = roleIds.split(",");
			for(String roleId : roleId_ary){
				if(roleId == null || "".equals(roleId.trim()))
					continue;
				RoleResource roleResource = new RoleResource();
				roleResource.setRoleId(Long.parseLong(roleId));
				roleResource.setResourceId(resourceId);
				
				roleResourceDao.saveEntity(roleResource);
			}
		}while(false);
	}
	
	public String getResourcesStrByRoleId(long roleId){
		List<RoleResource> roleResourceList = this.getListByRoleId(roleId);
		return this.getResourcesStrByRoleResourceList(roleResourceList);
	}
	

	public String getRolesStrByResourceId(long resrouceId){
		List<RoleResource> roleResourceList = this.getListByResourceId(resrouceId);
		return this.getRolesStrByRoleResourceList(roleResourceList);
	}
	
	public String getResourcesStrByRoleResourceList(List<RoleResource> roleResourceList){
		StringBuffer buf = new StringBuffer();
		if(roleResourceList != null && roleResourceList.size() > 0){
			buf.append(",");
			for(RoleResource roleResource:roleResourceList){
				buf.append(roleResource.getResourceId()).append(",");
			}
		}
		return buf.toString();
	}
	
	public String getRolesStrByRoleResourceList(List<RoleResource> roleResourceList){
		StringBuffer buf = new StringBuffer();
		if(roleResourceList != null && roleResourceList.size() > 0){
			buf.append(",");
			for(RoleResource roleResource:roleResourceList){
				buf.append(roleResource.getRoleId()).append(",");
			}
		}
		return buf.toString();
	}
	
	public List<RoleResource> getListByRoleId(long roleId){
		return roleResourceDao.getListByRoleId(roleId);
	}
	

	public List<RoleResource> getListByResourceId(long resourceId){
		return roleResourceDao.getListByResourceId(resourceId);
	}
	
	/**
	 * 根据roleId和ResourceId，查出一个列表，
	 * 如果空，就是没有权限
	 * 如果非空，就算有权限。
	*/
	public List<RoleResource> getRoleResourceListByRoleIdsAndResourceId(List<Long> roleIds,long resourceId){
		return roleResourceDao.getRoleResourceListByRoleIdsAndResourceId(roleIds, resourceId);
	}

	public RoleResourceDao getBaseRepository() {
		return roleResourceDao;
	}


}
