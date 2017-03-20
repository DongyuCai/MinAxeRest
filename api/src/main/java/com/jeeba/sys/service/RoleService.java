package com.jeeba.sys.service;

import java.util.HashMap;
import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.exception.RestException;

import com.jeeba.sys.entity.Role;
import com.jeeba.sys.repository.RoleDao;

@Service
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private RoleResourceService roleResourceService;
	
	
	
	//需要删除角色表、用户角色表、角色菜单表、角色资源表
	@Tns
	public void delete(long id) {
		this.deleteEntity(id);
		userRoleService.getBaseRepository().deleteByRoleId(id);
		roleMenuService.getBaseRepository().deleteByRoleId(id);
		roleResourceService.getBaseRepository().deleteByRoleId(id);
	}

	private void deleteEntity(long id) {
		Role role = new Role();
		role.setId(id);
		roleDao.deleteEntity(role);
	}

	public void edit(long id, String name, String remark) {
		name = name == null?"":name;
		remark = remark == null?"":remark;
		Role role = this.getEntity(id);
		if(role == null) throw new RestException("角色不存在");

		if("".equals(name)) throw new RestException("角色名不能空");
		
		Role roleOther = roleDao.findRoleByNameNotId(name,id);
		if(roleOther != null) throw new RestException("角色名已被使用："+name);
		
		//保存角色
		role.setName(name);
		role.setRemark(remark);
		roleDao.saveEntity(role);
	}

	public Role getEntity(long id) {
		Role role = new Role();
		role.setId(id);
		return roleDao.getEntity(role);
	}

	public void add(String name, String remark) {
		name = name == null?"":name;
		remark = remark == null?"":remark;
		
		if("".equals(name)) throw new RestException("角色名不能空");
		
		Role role = roleDao.findRoleByName(name);
		if(role != null) throw new RestException("角色名已被使用："+name);
		
		//存角色
		role = new Role();
		role.setName(name);
		role.setRemark(remark);
		role = roleDao.saveEntity(role);
	}

	public List<Role> getAll() {
		return roleDao.getAll();
	}

	public HashMap<String, Object> getRolesPage(Integer page, Integer pageSize) {
		PageConfig pageConfig = new PageConfig(page, pageSize);
		Page<Role> result = roleDao.getPage(pageConfig);
		HashMap<String, Object> pageResult = new HashMap<>();
		pageResult.put("totalpage", result.getPages());
		pageResult.put("totalElements", result.getCount());
		pageResult.put("records", result.getRecords());
		return pageResult;
	}

}
