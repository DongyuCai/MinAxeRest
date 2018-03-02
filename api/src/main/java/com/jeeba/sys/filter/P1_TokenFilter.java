package com.jeeba.sys.filter;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.axe.bean.mvc.Handler;
import org.axe.bean.mvc.Param;
import org.axe.exception.RestException;
import org.axe.helper.ioc.BeanHelper;
import org.axe.interface_.mvc.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeba.sys.constant.UserContext;
import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserToken;
import com.jeeba.sys.service.UserService;
import com.jeeba.sys.service.UserTokenService;

/**
 * 头filter，spring-mvc中的第一个
 * 除了web.xml中的filter外
 */
public class P1_TokenFilter implements Filter{
	Logger LOGGER = LoggerFactory.getLogger(P1_TokenFilter.class);
	
	@Override
	public void init() {
	}

	@Override
	public int setLevel() {
		return 1;
	}

	@Override
	public Pattern setMapping() {
		return Pattern.compile("^.*:\\/sys\\/.*$");
	}

	@Override
	public Pattern setNotMapping() {
		return null;
	}

	@Override
	public boolean doFilter(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler)
			throws RestException {
		do{
			String token = request.getHeader("X-1008-Session-Token");
			
			// 通过token拿到UserToken对象数据
			if(token == null || "".equals(token.trim())) break;
			// 1.redis 一级缓存
//			RedisService redisService = BeanHelper.getBean(RedisService.class);
//			UserToken userToken = redisService.getUserToken(token);
			UserToken userToken = null;
			// 2.数据库 二级存储
			if(userToken == null){
				LOGGER.debug("访问数据库获取UserToken");
				UserTokenService userTokenService = BeanHelper.getBean(UserTokenService.class);
				userToken = userTokenService.getUserTokenByToken(token);
				if(userToken != null){
//					redisService.saveUserToken(userToken);
				}
			}
			
			if(userToken == null) break;
			UserContext.setToken(userToken);
			UserService userService = BeanHelper.getBean(UserService.class);
			// 通过UserToken中关联的userId拿到User对象数据
			User user = userService.getEntity(userToken.getUserId());
			
			if(user == null){
				UserContext.cleanToken();
				break;
			}
			UserContext.setUser(user);
		}while(false);
		
		return true;
	}


	@Override
	public void doEnd(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler) {
		UserContext.cleanUser();
		UserContext.cleanToken();
	}
}
