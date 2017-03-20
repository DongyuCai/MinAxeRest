package com.jeeba.sys.repository;

import java.util.List;

import org.axe.annotation.persistence.Dao;
import org.axe.annotation.persistence.Sql;
import org.axe.interface_.persistence.BaseRepository;

import com.jeeba.sys.entity.UserToken;

@Dao
public interface UserTokenDao extends BaseRepository{
	
	@Sql("select ut.* from UserToken ut where ut.token = ?1")
	public UserToken getUserTokenByToken(String token);

	@Sql("select ut.* from UserToken ut where ut.userId = ?1")
	public UserToken getUserTokenByUserId(long userId);
	
	
	@Sql("select ut.* from UserToken ut,User u where ut.userId = u.id")
	public List<UserToken> getUserToken4Cache();
}
