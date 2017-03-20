package com.jeeba.sys.constant;

import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserToken;

public class UserContext {
	
	private static ThreadLocal<User> localUser = new ThreadLocal<>();
	private static ThreadLocal<UserToken> localToken = new ThreadLocal<>();
	
	public static User getUser(){
		return localUser.get();
	}
	
	public static UserToken getToken(){
		return localToken.get();
	}
	
	
	public static void setUser(User user){
		localUser.set(user);
	}

	public static void setToken(UserToken token){
		localToken.set(token);
	}
	
	public static void cleanUser(){
		localUser.remove();
	}
	
	public static void cleanToken(){
		localToken.remove();
	}
	
}
