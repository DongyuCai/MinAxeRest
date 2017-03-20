package com.jeeba.sys.service;

import java.util.HashMap;
import java.util.List;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.persistence.Tns;
import org.axe.bean.persistence.Page;
import org.axe.bean.persistence.PageConfig;
import org.axe.exception.RestException;
import org.axe.util.CollectionUtil;
import org.eclipse.jetty.util.StringUtil;

import com.jeeba.sys.entity.Resource;
import com.jeeba.sys.repository.ResourceDao;
import com.jeeba.sys.repository.RoleResourceDao;


@Service
public class ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private RoleResourceDao roleResourceDao;
	@Autowired
	private ResourceLoader resourceLoader;
	
	public List<Resource> getResourceAll() {
		return resourceLoader.getResourceAll();
	}

	public List<Resource> getResourceAllFromDb() {
		return resourceDao.getResourceAll();
	}
	/**
	 * 给资源分组
	 * 返回两个List
	 * unpublishedList	代码里有，但是还么入库的资源
	 * outOfDateList		代码里没哟，但是库里有的，就是这个资源过时了
	*/
	public List<Resource> getUnpublishedList(){
		//所有已经发布就是入库的资源
		List<Resource> resourceFromDb = resourceDao.getResourceAll();
		
		//遍历一下，分成两拨
		//一波是，没有入库的资源
		//另一波是，库里有，但是实际上代码里没有的资源，可能是后来改掉，或者删掉了。我们认为，就是过时的资源。
		List<Resource> unpublishedList = CollectionUtil.diff(this.getResourceAll(), resourceFromDb);
		return unpublishedList;
	}
	
	/**
	 * 给资源分组
	 * 返回两个List
	 * unpublishedList	代码里有，但是还么入库的资源
	 * outOfDateList		代码里没哟，但是库里有的，就是这个资源过时了
	*/
	public List<Resource> getOutOfDateList(){
		//所有已经发布就是入库的资源
		List<Resource> resourceFromDb = resourceDao.getResourceAll();
		
		//遍历一下，分成两拨
		//一波是，没有入库的资源
		//另一波是，库里有，但是实际上代码里没有的资源，可能是后来改掉，或者删掉了。我们认为，就是过时的资源。
		List<Resource> outOfDateList = CollectionUtil.diff(resourceFromDb, this.getResourceAll());
		return outOfDateList;
	}
	
	public Resource getResourceByUrlAndType(String url,String accessType){
		return resourceDao.getResourceByUrlAndType(url,accessType);
	}
	

	public HashMap<String,Object> page(int pageNum, int pageSize, String keywords, String type) {
		StringBuilder append = new StringBuilder();
		if(StringUtil.isNotBlank(keywords)){
			append.append(" and keywords like '%"+keywords+"%'");
		}
		if(StringUtil.isNotBlank(type)){
			append.append(" and type = '"+type+"'");
		}
		
		PageConfig pageConfig = new PageConfig(pageNum, pageSize);
		
		Page<Resource> result = resourceDao.page(append.toString(), pageConfig);
		
		HashMap<String,Object> pageResult = new HashMap<>();
		pageResult.put("totalpage", result.getPages());
		pageResult.put("totalElements", result.getCount());
		pageResult.put("records", result.getRecords());
		return pageResult;
	}

	@Tns
	public void add(String title,String url,String type,String classMethod) {
		Resource resource = resourceDao.getResourceByUrlAndType(url,type);
		if(resource != null){
			throw new RestException("URL不能重复添加,请更换");
		}
		
		
		if(title == null || "".equals(title.trim())) 
			throw new RestException("标题不能空");
		if(url == null || "".equals(url.trim())) 
			throw new RestException("URL不能空");
		if(type == null || "".equals(type.trim())) 
			throw new RestException("TYPE不能空");
		if(classMethod == null || "".equals(classMethod.trim()))
			throw new RestException("CLASS_METHOD不能空");
		title = title.trim();
		url = url.trim();
		type = type.trim().toUpperCase();
		classMethod = classMethod.trim();
		resource = new Resource();
		resource.setTitle(title);
		resource.setUrl(url);
		resource.setType(type);
		resource.setClassMethod(classMethod);
		resource.setKeywords(title+" "+url+" "+classMethod);
		resourceDao.saveEntity(resource);
	}

	@Tns
	public void delete(long id) {
		this.deleteEntity(id);
		roleResourceDao.deleteByResourceId(id);
	}

	private void deleteEntity(long id) {
		Resource resource = new Resource();
		resource.setId(id);
		roleResourceDao.deleteEntity(resource);
	}

	public void edit(long id,String title) {
		if(title == null || "".equals(title.trim())) 
			throw new RestException("标题不能空");
		title = title.trim();
		
		Resource resource = this.getEntity(id);
		if(resource == null)
			throw new RestException("资源不存在");
		resource.setTitle(title);
		resource.setKeywords(title+" "+resource.getUrl()+" "+resource.getClassMethod());
		roleResourceDao.saveEntity(resource);
	}

	public Resource getEntity(long id) {
		Resource resource = new Resource();
		resource.setId(id);
		return roleResourceDao.getEntity(resource);
	}

}
