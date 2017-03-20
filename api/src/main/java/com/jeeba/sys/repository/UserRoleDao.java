package com.jeeba.sys.repository;


import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.UserRole;

@Dao
public interface UserRoleDao extends BaseRepository{

	@Sql("select ur.* from UserRole ur where ur.roleId = ?1")
	public List<UserRole> getUserRoleListByRoleId(long roleId);
	
	
	@Sql("select ur.* from UserRole ur where ur.userId = ?1")
	public List<UserRole> getUserRoleListByUserId(long userId);
	
	@Sql("delete from UserRole where roleId = ?1")
	public void deleteByRoleId(Long roleId);
	
	@Sql("delete from UserRole where userId = ?1")
	public void deleteByUserId(long userId);
	
}
