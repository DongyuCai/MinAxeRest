package com.jeeba.sys.rest;


import java.util.HashMap;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Controller;
import org.axe.annotation.mvc.Request;
import org.axe.annotation.mvc.RequestParam;
import org.axe.constant.RequestMethod;
import org.axe.exception.RestException;

import com.jeeba.sys.entity.Resource;
import com.jeeba.sys.service.ResourceService;
import com.jeeba.sys.service.RoleResourceService;

@Controller(basePath = "/sys/resource", title="资源")
public class ResourceRestController {
	
	@Autowired
	private ResourceService  resourceService;
	@Autowired
	private RoleResourceService roleResourceService;
	
	
	@Request(value="", method = RequestMethod.POST,title="发布资源")
	public void add(
			@RequestParam("title")String title,
			@RequestParam("url")String url,
			@RequestParam("type")String type,
			@RequestParam("classMethod")String classMethod) {
		
		resourceService.add(title,url,type,classMethod);
	}
	
	@Request(value = "/{id}",method = RequestMethod.DELETE,title="删除资源")
	public void delete(@RequestParam("id")Long id) {
		resourceService.delete(id);
	}
	
	@Request(value = "/{id}",method = RequestMethod.PUT,title="编辑资源")
	public void edit(@RequestParam("id")Long id,@RequestParam("title")String title) {
		resourceService.edit(id,title);
	}
	
	@Request(value = "/{id}/role",method = RequestMethod.PUT,title="调整资源关联的角色")
	public void editRole(@RequestParam("id")Long id,@RequestParam("roleIds")String roleIds){
		roleResourceService.setRolesOfResource(id, roleIds);
	}

	@Request(value = "/all", method = RequestMethod.GET,title="所有已发布资源")
	public Object allFromDb(){
		return resourceService.getResourceAllFromDb();
	}
	
	@Request(value = "/all/all", method = RequestMethod.GET,title="所有系统内资源")
	public Object allAll(){
		return resourceService.getResourceAll();
	}
	
	@Request(value = "/unpublished", method = RequestMethod.GET,title="未发布的资源")
	public Object unpublished(){
		return resourceService.getUnpublishedList();
	}
	
	@Request(value = "/outofdate", method = RequestMethod.GET,title="过期的资源")
	public Object outOfDate(){
		return resourceService.getOutOfDateList();
	}

	@Request(value = "/page", method = RequestMethod.GET,title="已发布资源-分页查询")
	public HashMap<String,Object> page(
			@RequestParam(value = "page") Integer pageNum,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "keywords") String keywords,
			@RequestParam(value = "type") String type
			) {
		pageNum = pageNum == null?1:pageNum;
		pageSize = pageSize == null?15:pageSize;
		keywords = keywords == null?"":keywords;
		type = type == null?"":type;
		
		return resourceService.page(pageNum,pageSize,keywords,type);
	}

	@Request(value = "/{id}",method = RequestMethod.GET,title="单个已发布资源")
	public Object get(@RequestParam("id")Long id) {
		Resource resource = resourceService.getEntity(id);
		if(resource == null){
			throw new RestException("资源不存在");
		}
		//拿到角色
		String  roleIds = roleResourceService.getRolesStrByResourceId(id);
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("resource", resource);
		res.put("roleIds", roleIds);
		return res;
	}
}
