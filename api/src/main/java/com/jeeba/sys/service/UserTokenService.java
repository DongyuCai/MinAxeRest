package com.jeeba.sys.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.util.IpUtil;
import org.axe.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserToken;
import com.jeeba.sys.repository.UserTokenDao;


@Service
public class UserTokenService{
	private final Logger log = LoggerFactory.getLogger(UserTokenService.class);  
	@Autowired
	private UserService userService;
	@Autowired
	private UserTokenDao userTokenDao;
//	@Autowired
//	private RedisService redisService;

//	public Map<String,UserToken> userTokenCache = new HashMap<String,UserToken>();
	
	public int EXPIRE_DATE = 1;//日票的过期日期，如果1天不用，就过期
	public int COOKIE_EXPIRE_DATE = 7;//月票，一周过期，不管是否使用，都是一周过期，过期后需要靠日票
	
	public UserToken login(String username, String password, String save_login,
			HttpServletRequest request){
		try {
			do{
				User user = userService.checkPassword(username, password);
				//密码不对
				if(user == null) break;
				
				//密码对的，返回UserToken
				UserToken userToken = userTokenDao.getUserTokenByUserId(user.getId());
				if (userToken == null) {
					userToken = new UserToken();
					userToken.setCreatTime(new Date());
					userToken.setUserId(user.getId());
					userToken.setUsername(username);
				}else{
					//如果有老的token，更新掉cache里老的
//					userTokenCache.remove(userToken.getToken());
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date current = new Date();
				String todayMorning_str = sdf.format(current);//今天的早上0点
				Date todayMorning = sdf.parse(todayMorning_str);
				Calendar cd = Calendar.getInstance();
				cd.setTime(todayMorning);   
				cd.add(Calendar.DATE, EXPIRE_DATE);//增加一天  ，就是今天晚上0点，如果超过今天晚上0点，除了这次登录没有别的请求操作，那么就过期了
				userToken.setExpire(cd.getTime());//日票
				
				if("true".equals(save_login)){
					//自动登录一周，则需要月票
					cd.add(Calendar.DATE, COOKIE_EXPIRE_DATE);
					userToken.setCookieExpire(cd.getTime());
				}else{
					userToken.setCookieExpire(null);
				}
				
				
				//更新token
				StringBuffer token_str = new StringBuffer();
				
				//拼装token串
				token_str.append(user.getId()).append("|");
				//生成过期日期
				token_str.append(userToken.getExpire().getTime()).append("|");
				if(userToken.getCookieExpire() != null){
					token_str.append(userToken.getCookieExpire().getTime()).append("|");
				}
				//一次加密token
				String token = MD5Util.getMD5Code(token_str.toString()+System.currentTimeMillis());
				if(token.length()>32){
					token = token.substring(0,32);
				}
//				String oldToken = userToken.getToken();
//				redisService.deleteUserToken(oldToken);
				userToken.setToken(token);
				
				
				//保存当前登录机器的ip
				//TODO:如果移动端登录，要保证不能踢掉pc端
				userToken.setIp(IpUtil.getIpAddress(request));
				
				//保存token
				userToken.setUpdateTime(new Date());
				userToken = userTokenDao.saveEntity(userToken);
				
				
				return userToken;
			}while(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("UserTokenService.login:"+e.getMessage());
		}
		return null;
	}
	
	
	public UserToken getUserTokenByToken(String token){
		UserToken userToken= null;
//		if(userTokenCache.containsKey(token)){
//			userToken = userTokenCache.get(token);
//		}else{
			userToken = userTokenDao.getUserTokenByToken(token);
//			if(userToken != null){
//				userTokenCache.put(token, userToken);
//			}
//		}
		return userToken;
	}
	

	public boolean isExpire(UserToken userToken){
		long currentTimeMillis = System.currentTimeMillis();
		BigDecimal now = new BigDecimal(currentTimeMillis);

		//判断是否过期
		do{
			if(userToken == null) break;
			
			//========= 日票  =============//
			Date expire = userToken.getExpire();
			//过期
			if(expire == null || currentTimeMillis > expire.getTime()){
				break;
			}
			
			//没过期，更新expire
			BigDecimal expireDayMillis = new BigDecimal(86400).multiply(new BigDecimal(1000)).multiply(new BigDecimal(EXPIRE_DATE));
			BigDecimal leftMillis = new BigDecimal(expire.getTime()).subtract(now);
			if(leftMillis.compareTo(expireDayMillis) < 0){
				//离过期只剩1天不到了，更新下过期时间，往后延长1天
				Calendar cd = Calendar.getInstance();   
	            cd.setTime(expire);   
	            cd.add(Calendar.DATE, EXPIRE_DATE);//增加一天   
	            userToken.setExpire(cd.getTime());
	            //更新缓存和db,每天只会有一次
				userToken.setUpdateTime(new Date());
//	            userTokenCache.put(userToken.getToken(), userToken);
//				redisService.saveUserToken(userToken);
	            userTokenDao.saveEntity(userToken);
			}
			
			return false;//没过期
		}while(false);
		
		//日票过期了，来看看cookie月票吧
		do{
			if(userToken == null) break;
			//过期了
			Date cookieExpire = userToken.getCookieExpire();
			if(cookieExpire == null || currentTimeMillis > cookieExpire.getTime()){
				break;
			}
			return false;//没过期
		}while(false);
		
		
//		if(userToken != null){//清理掉过期的token
//			userTokenCache.remove(userToken.getToken());
//		}
		return true;//真过期了，或者就没这个token
	}
	
	/*public void refreshCache(){
		userTokenCache.clear();
		//第一步：查出userToken列表，这里的userToken，都是在user表里有userId的，删掉的user，对应的userToken就不会查出来了
		List<UserToken> userTokenAll = this.userTokenDao.getUserToken4Cache();
		if(userTokenAll != null  && userTokenAll.size() > 0){
			for(UserToken userToken:userTokenAll){
				//第二部:已经过期的，不要了
				if(this.isExpire(userToken)) continue;
				userTokenCache.put(userToken.getToken(), userToken);
			}
		}
	}*/
	
}
