package com.jeeba.sys.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.axe.bean.mvc.Handler;
import org.axe.bean.mvc.Param;
import org.axe.exception.RestException;
import org.axe.helper.ioc.BeanHelper;
import org.axe.interface_.mvc.Filter;

import com.jeeba.sys.constant.UserContext;
import com.jeeba.sys.entity.Resource;
import com.jeeba.sys.entity.RoleResource;
import com.jeeba.sys.entity.UserRole;
import com.jeeba.sys.entity.UserToken;
import com.jeeba.sys.service.ResourceService;
import com.jeeba.sys.service.RoleResourceService;
import com.jeeba.sys.service.UserRoleService;

/**
 * 过滤资源权限 caidongyu
 */
public class P3_ResourceFilter implements Filter {

	private boolean onlyAcceptRegistered = false;// 是否只接受注册入库的资源

	@Override
	public void init() {
		
	}

	@Override
	public int setLevel() {
		return 3;
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
		do{
			ResourceService resourceService = BeanHelper.getBean(ResourceService.class);
			
			Resource resource = resourceService.getResourceByUrlAndType(handler.getMappingPath(), handler.getRequestMethod());
			if (resource == null) {
				// 如果资源入库，那就接着校验
				// 如果没有入库，就要看是否接受不入库的资源
				if (onlyAcceptRegistered) {
					// 只能接受入库资源，没有入库的，就算能请求成功，也不可以
					throw new RestException("URL未注册开放！[" + handler.getRequestMethod() + ":" + handler.getMappingPath() + "]");
				} else {
					break;
				}
			}
			
			// 拿到role与url对应的集合
			UserToken userToken = UserContext.getToken();
			if(userToken == null){
				throw new RestException("资源权限需要登录获取，请重新登录！");
			}
			
			UserRoleService userRoleService = BeanHelper.getBean(UserRoleService.class);
			List<UserRole> userRoleList = userRoleService.getListByUserId(userToken.getUserId());
			if (userRoleList == null || userRoleList.size() == 0) {
				// 角色都还没赋，什么操作都不会给
				throw new RestException("太可怜了，一个角色都没有！No角色，No资源！");
			}
			List<Long> roleIds = new ArrayList<>();
			for (UserRole userRole : userRoleList) {
				roleIds.add(userRole.getRoleId());
			}
			if(roleIds.size() <= 0) roleIds.add(-1L);
			RoleResourceService roleResourceService = BeanHelper.getBean(RoleResourceService.class);
			List<RoleResource> roleResourceList = roleResourceService
					.getRoleResourceListByRoleIdsAndResourceId(roleIds,
							resource.getId());
			if (roleResourceList == null || roleResourceList.size() == 0) {
				// Role下没这个资源，肯定不行
				throw new RestException("没有该URL("+resource.getTitle()+")的访问权限！[" + handler.getRequestMethod()+":"+ handler.getMappingPath()
						+ "]");
			}
		}while(false);
			/*StringBuffer methodBuf = new StringBuffer(",");
			for (RoleResource roleResource : roleResourceList) {
				methodBuf.append(roleResource.getType()).append(",");
			}
			// 看集合中的操作是否包含当前的method类型
			String methodContent = methodBuf.toString().toUpperCase();
			
			if (!methodContent.contains("," + method + ",")) {
				return this.writerErrorToFrontend(response, "没有该[" + url + "]URL的"
						+ method + "访问权限！");
			}*/
		return true;
	}

	@Override
	public void doEnd(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler) {}

}
