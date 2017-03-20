package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.Resource;

@Dao
public interface ResourceDao extends BaseRepository{
	
	@Sql("select rs.* from Resource rs where rs.url = ?1 and rs.type = ?2")
	public Resource getResourceByUrlAndType(String url,String type);

	@Sql("select rs.* from Resource rs")
	public List<Resource> getResourceAll();

	@Sql("select rs.* from Resource rs where 1=1 #1")
	public Page<Resource> page(String append,PageConfig pageConfig);
}
