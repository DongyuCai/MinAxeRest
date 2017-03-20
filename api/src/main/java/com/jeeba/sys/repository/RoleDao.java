package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.Role;

@Dao
public interface RoleDao extends BaseRepository{
	
	
	@Sql("select r.* from Role r where r.name = ?1")
	public Role findRoleByName(String name);
	
	@Sql("select r.* from Role r where r.name = ?1 and r.id <> ?2")
	public Role findRoleByNameNotId(String name,long id);
	
	@Sql("select r.* from Role r")
	public List<Role> getAll();

	@Sql("select r.* from Role r")
	public Page<Role> getPage(PageConfig pageConfig);
}
