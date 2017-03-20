package com.jeeba.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axe.annotation.ioc.Controller;
import org.axe.annotation.ioc.Service;
import org.axe.annotation.mvc.Request;
import org.axe.bean.mvc.Handler;
import org.axe.helper.mvc.ControllerHelper;
import org.axe.util.StringUtil;

import com.jeeba.sys.entity.Resource;

@Service
public class ResourceLoader{

	public List<Resource> getResourceAll() {
		List<Resource> resourceAll = new ArrayList<>();
		List<Handler> handlerList = ControllerHelper.getActionList();
		
		Map<String, List<Handler>> handlerMap = new HashMap<>();
		for (Handler handler : handlerList) {
			String mappingPath = handler.getMappingPath();
			List<Handler> action = new ArrayList<>();
			if (handlerMap.containsKey(mappingPath)) {
				action = handlerMap.get(mappingPath);
			} else {
				handlerMap.put(mappingPath, action);
			}
			action.add(handler);
		}
		List<String> mappingPathList = StringUtil.sortStringSet(handlerMap.keySet());
		for (String mappingPath : mappingPathList) {
			if(mappingPath.startsWith("/axe")){
				continue;
			}
			List<Handler> action = handlerMap.get(mappingPath);
			StringBuilder titleBuf = new StringBuilder();
			for (Handler handler : action) {
				Resource resource = new Resource();
				titleBuf.delete(0, titleBuf.length());
				titleBuf.append(handler.getControllerClass().getAnnotation(Controller.class).title());
				if(titleBuf.length() > 0){
					titleBuf.append(".");
				}
				titleBuf.append(handler.getActionMethod().getAnnotation(Request.class).title());
				resource.setTitle(titleBuf.toString());
				resource.setUrl(mappingPath);
				resource.setType(handler.getRequestMethod());
				resource.setClassMethod(handler.getActionMethod().getName());
				resourceAll.add(resource);
			}
		}
				
		return resourceAll;
	}

}
