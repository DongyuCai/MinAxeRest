package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.User;

@Dao
public interface UserDao extends BaseRepository{

	@Sql("select count(1) from User u where u.username = ?1")
	public int countUserByUsername(String username);

	@Sql("select u.* from User u where u.username= ?1")
	public User getUserByUsername(String username);

	@Sql("select * from User where 1=1 #3")
	public Page<User> page(String username,List<Long> ids,String append,PageConfig pageConfig);
}
