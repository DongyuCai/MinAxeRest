package com.jeeba.sys.rest;

import java.util.HashMap;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Controller;
import org.axe.annotation.mvc.Request;
import org.axe.annotation.mvc.RequestParam;
import org.axe.constant.RequestMethod;
import org.axe.exception.RestException;

import com.jeeba.sys.entity.User;
import com.jeeba.sys.service.UserRoleService;
import com.jeeba.sys.service.UserService;

@Controller(basePath = "/sys/user",title="运营用户")
public class UserRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	
	@Request(value = "", method = RequestMethod.POST,title="新增运营用户")
	public HashMap<String, Object> add(
			@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("roleIds")String roleIds
			) {
		HashMap<String, Object> res = new HashMap<String, Object>();
		userService.add(username,password,roleIds);
		return res;
	}

	@Request(value="/{id}",method = RequestMethod.DELETE,title="删除运营用户")
	public Object delete(@RequestParam("id")Long id) {
		userService.deleteEntity(id);//只要删除用户表，限制用户的登录即可
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value="/{id}/role",method = RequestMethod.PUT,title="编辑运营用户")
	public Object editRole(@RequestParam("id")Long id,@RequestParam("roleIds")String roleIds) {
		userRoleService.editRole(id, roleIds);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}
	
	@Request(value="/{id}/password",method = RequestMethod.PUT,title="修改密码")
	public Object editPassword(
			@RequestParam("id")Long id,
			@RequestParam("password")String password,
			@RequestParam("oldPassword")String oldPassword) {
		userService.editPassword(id, password, oldPassword);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value = "/page", method = RequestMethod.GET,title="分页查询")
	public Object page(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "roleId") Long roleId) {
		page = page==null?1:page;
		pageSize = pageSize==null?15:pageSize;
		roleId = roleId==null?-1:roleId;
		
		return userService.page(page,pageSize,username,roleId);
	}
	
	@Request(value = "/{id}", method = RequestMethod.GET,title="单个运营用户")
	public Object get(@RequestParam("id") Long id) {
		User user = userService.getEntity(id);
		if(user == null){
			throw new RestException("用户不存在");
		}
		//拿到角色
		String  roleIds = userRoleService.getRolesStrByUserId(user.getId());
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("user", user.returnPublicUserInfo());
		res.put("roleIds", roleIds);
		return res;
	}
}
