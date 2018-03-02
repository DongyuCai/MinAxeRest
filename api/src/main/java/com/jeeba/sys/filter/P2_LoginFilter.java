package com.jeeba.sys.filter;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.axe.bean.mvc.Handler;
import org.axe.bean.mvc.Param;
import org.axe.exception.RestException;
import org.axe.helper.ioc.BeanHelper;
import org.axe.interface_.mvc.Filter;
import org.axe.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeba.sys.constant.UserContext;
import com.jeeba.sys.entity.UserToken;
import com.jeeba.sys.service.UserTokenService;

/**
 * 用户登录拦截 caidongyu
 */
public class P2_LoginFilter implements Filter{
	Logger LOGGER = LoggerFactory.getLogger(P2_LoginFilter.class);

	@Override
	public void init() {
		
	}

	@Override
	public int setLevel() {
		return 2;
	}

	@Override
	public Pattern setMapping() {
		return Pattern.compile("^.*:\\/sys\\/.*$");
	}

	@Override
	public Pattern setNotMapping() {
		return Pattern.compile("^.*login.*$");
	}

	@Override
	public boolean doFilter(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler)
			throws RestException {
		do {
			try {
				UserTokenService userTokenService = BeanHelper.getBean(UserTokenService.class);
				UserToken userToken = UserContext.getToken();
				// 判断token是否过期
				boolean isExpire = userTokenService.isExpire(userToken);
				if (isExpire) {
					throw new RestException(RestException.SC_UNAUTHORIZED, "Token过期了，请重新登录！");
				}
				// 换机器必须重新登录
				if (!IpUtil.getIpAddress(request).equals(userToken.getIp())) {
//					userTokenService.userTokenCache.remove(userToken.getToken());
					throw new RestException(RestException.SC_UNAUTHORIZED, "更换机器了，请重新登录！");
				}
			} catch (RestException e) {
				throw e;
			} catch (Exception e){
				LOGGER.error(e.getMessage());
				throw new RestException(RestException.SC_INTERNAL_SERVER_ERROR,"登录状态过滤异常，请监控日志并再次操作！");
			}
		} while (false);
		return true;
	}

	@Override
	public void doEnd(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler) {}

}
