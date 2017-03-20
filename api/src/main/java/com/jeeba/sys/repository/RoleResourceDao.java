package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.RoleResource;

@Dao
public interface RoleResourceDao extends BaseRepository{
	
	@Sql("select rs.* from RoleResource rs where rs.roleId in (?1) and rs.resourceId = ?2")
	public List<RoleResource> getRoleResourceListByRoleIdsAndResourceId(List<Long> roleIds,long resourceId);

	@Sql("delete from RoleResource where resourceId = ?1")
	public void deleteByResourceId(long resourceId);
	
	@Sql(" delete from RoleResource where roleId = ?1")
	public void deleteByRoleId(long roleId);
	
	@Sql("select rs.* from RoleResource rs where rs.roleId = ?1")
	public List<RoleResource> getListByRoleId(long roleId);

	@Sql("select rs.* from RoleResource rs where rs.resourceId = ?1")
	public List<RoleResource> getListByResourceId(long resourceId);

}

