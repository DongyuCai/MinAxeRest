package com.jeeba.sys.rest;

import java.util.HashMap;
import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Controller;
import org.axe.annotation.mvc.Request;
import org.axe.annotation.mvc.RequestParam;
import org.axe.constant.RequestMethod;
import org.axe.exception.RestException;

import com.jeeba.sys.entity.Role;
import com.jeeba.sys.service.RoleMenuService;
import com.jeeba.sys.service.RoleResourceService;
import com.jeeba.sys.service.RoleService;

@Controller(basePath = "/sys/role",title="角色")
public class RoleRestController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private RoleResourceService roleResourceService;


	@Request(value = "", method = RequestMethod.POST,title="新增角色")
	public Object add(@RequestParam("name")String name,@RequestParam("remark")String remark) {
		roleService.add(name,remark);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value="/{id}",method = RequestMethod.DELETE,title="删除角色")
	public Object delete(@RequestParam("id")Long id) {
		//需要删除一些级联信息
		roleService.delete(id);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value="/{id}",method = RequestMethod.PUT,title="编辑角色")
	public Object edit(@RequestParam("id")Long id,@RequestParam("name")String name,@RequestParam("remark")String remark) {
		roleService.edit(id,name,remark);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value="/{id}/menu",method = RequestMethod.PUT,title="调整角色关联的菜单")
	public Object editMenu(@RequestParam("id")Long id,@RequestParam("menuIds")String menuIds) {
		roleMenuService.setMenusOfRole(id,menuIds);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}

	@Request(value="/{id}/resource",method = RequestMethod.PUT,title="调整角色关联的资源")
	public Object editResource(@RequestParam("id")Long id,@RequestParam("resourceIds")String resourceIds) {
		roleResourceService.setResourcesOfRole(id,resourceIds);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}


	@Request(value = "/all", method = RequestMethod.GET,title="所有角色不分页")
	public List<Role> all(){
		return roleService.getAll();
	}

	@Request(value = "/page", method = RequestMethod.GET,title="分页角色查询")
	public HashMap<String, Object> page(
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize) {
		page = page==null?1:page;
		pageSize = pageSize==null?15:pageSize;

		HashMap<String, Object> res;
		res = roleService.getRolesPage(page, pageSize);
		return res;
	}

	@Request(value = "/{id}", method = RequestMethod.GET,title="单个角色")
	public Object get(@RequestParam("id") Long id) {
		Role role = roleService.getEntity(id);
		if (role == null) {
			throw new RestException("角色不存在！");
		}
		//拿到角色
		String  menuIds = roleMenuService.getMenusStrByRoleId(role.getId());

		//拿到资源
		String  resourceIds = roleResourceService.getResourcesStrByRoleId(role.getId());

		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("role", role);
		res.put("menuIds", menuIds);
		res.put("resourceIds", resourceIds);
		return res;
	}

}
