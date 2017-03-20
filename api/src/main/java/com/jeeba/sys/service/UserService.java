package com.jeeba.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.exception.RestException;
import org.axe.util.MD5Util;
import org.axe.util.StringUtil;

import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserRole;
import com.jeeba.sys.repository.UserDao;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;
	
	public User getEntity(long id) {
		User user = new User();
		user.setId(id);
		user = userDao.getEntity(user);
		return user;
	}

	public User checkPassword(String username,String password){
		do{
			if(username == null) break;
			if(password == null) break;
			
			User user = userDao.getUserByUsername(username);
			if(user == null) break;
			
			String salt = user.getSalt();
			String md5password = MD5Util.getMD5Code(password+salt);
			//密码不对
			if (!md5password.equals(user.getPassword())) break;
			
			return user;
		}while(false);
		return null;
	}

	public void editPassword(long id,String newPassword,String oldPassword){
		User user = this.getEntity(id);
		if(user == null) throw new RestException("用户不存在");
		user = this.checkPassword(user.getUsername(), oldPassword);
		if(user == null){
			//旧密码输入错误
			throw new RestException("旧密码错误！修改密码失败！");
		}
		
		user = this.setPassword(user, newPassword);
		userDao.saveEntity(user);
	}

	private User setPassword(User user,String password){
		String salt = StringUtil.getRandomString(8);
		String login_password = MD5Util.getMD5Code(password+salt);
		user.setSalt(salt);
		user.setPassword(login_password);
		return user;
	}
	
	@Tns
	public User add(String username,String password, String roleIds){
		//=========== 保存用户  ==============//
		if(username == null || "".equals(username.trim()))
			throw new RestException("登录名不可以是空的");
		
		if(password == null || "".equals(password.trim()))
			throw new RestException("密码不可以是空的");
		
		
		username = username.trim();
		password = password.trim();
		
		int userCount = userDao.countUserByUsername(username);
		if(userCount > 0){
			throw new RestException("登录名已被使用："+username+"");
		}
		
		User user = new User();
		user.setUsername(username);
		user = this.setPassword(user, password);
		user = userDao.saveEntity(user);
		
		
		
		//=========== 保存角色 ==============//
		do{
			if(roleIds == null) break;
			String[] roleId_ary = roleIds.split(",");
			
			//保存用户角色关系
			for(String roleId:roleId_ary){
				if(StringUtil.isNotEmpty(roleId)){
					UserRole userRole = new UserRole();
					userRole.setUserId(user.getId());
					userRole.setRoleId(Long.parseLong(roleId));
					userRoleService.saveEntity(userRole);
				}
			}
			
		}while(false);
		return user;
	}
	
	public HashMap<String,Object> page(int page, int pageSize, String username, long roleId) {
		StringBuilder append = new StringBuilder();
		if(StringUtil.isNotEmpty(username)){
			append.append(" and username like '%?1%'");
		}
		List<Long> userId_in = new ArrayList<Long>();
		do{
			if(roleId < 0) break;
			List<UserRole> userRoleList =  userRoleService.getListByRoleId(roleId);
			
			if(userRoleList != null){
				for(UserRole userRole : userRoleList){
					userId_in.add(userRole.getUserId());
				}
			}

			if(userId_in.size() <= 0){
				userId_in.add(-1L);//直接让他找不着，否则会忽略in条件
			}
			
			append.append(" and id in(?2)");
		}while(false);
		PageConfig pageConfig = new PageConfig(page, pageSize);
		Page<User> result = userDao.page(username, userId_in, append.toString(), pageConfig);
		
		HashMap<String,Object> pageResult = new HashMap<>();
		List<User> records = result.getRecords();
		List<Map<String,String>> newRecords = new ArrayList<Map<String,String>>();
		if(records != null){
			for(User user:records){
				Map<String,String> userInfo = new HashMap<String,String>();
				userInfo.put("id", String.valueOf(user.getId()));
				userInfo.put("username", user.getUsername());
				String rolesStr = userRoleService.getRolesStrByUserId(user.getId());
				userInfo.put("roleId", rolesStr);
				newRecords.add(userInfo);
			}
		}
		pageResult.put("totalpage", result.getPages());
		pageResult.put("totalElements", result.getCount());
		pageResult.put("records", newRecords);
		return pageResult;
	}

	public void deleteEntity(Long id) {
		User user = new User();
		user.setId(id);
		userDao.deleteEntity(user);
	}

}
