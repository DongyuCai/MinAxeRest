package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.RoleMenu;

@Dao
public interface RoleMenuDao extends BaseRepository{
	
	
	@Sql("select rm.* from RoleMenu rm where rm.roleId = ?1")
	public List<RoleMenu> getListByRoleId(long roleId);
	
	@Sql(" delete from RoleMenu where roleId = ?1")
	public void deleteByRoleId(long roleId);

}
