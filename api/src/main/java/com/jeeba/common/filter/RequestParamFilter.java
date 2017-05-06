package com.jeeba.common.filter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.axe.annotation.mvc.RequestParam;
import org.axe.bean.mvc.FileParam;
import org.axe.bean.mvc.FormParam;
import org.axe.bean.mvc.Handler;
import org.axe.bean.mvc.Handler.ActionParam;
import org.axe.bean.mvc.Param;
import org.axe.exception.RestException;
import org.axe.interface_.mvc.Filter;
import org.axe.util.CollectionUtil;
import org.axe.util.StringUtil;

import com.jeeba.common.annotation.Default;
import com.jeeba.common.annotation.Required;

public class RequestParamFilter implements Filter {

	@Override
	public void init() {
		
	}

	@Override
	public int setLevel() {
		return 1;
	}

	@Override
	public Pattern setMapping() {
		return Pattern.compile("^.*$");
	}

	@Override
	public Pattern setNotMapping() {
		return null;
	}

	@Override
	public boolean doFilter(HttpServletRequest request, HttpServletResponse response, Param param, Handler handler)
			throws RestException {

		List<ActionParam> actionParamList = handler.getActionParamList();
		Map<String, List<FormParam>> fieldMap = param.getFieldMap();
		Map<String, List<FileParam>> fileMap = param.getFileMap();
		List<String> error = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(actionParamList)){
			for(int i=0;i<actionParamList.size();i++){
				ActionParam ap = actionParamList.get(i);
				Annotation[] ats = ap.getAnnotations();
				if(ats != null){
					boolean require = false;
					String fieldName = null;
					String fieldValue = null;
					for(Annotation at:ats){
						if(at instanceof RequestParam){
							fieldName = ((RequestParam)at).value();
						}
						if(at instanceof  Required){
							require = true;
						}
						if(at instanceof Default){
							fieldValue = ((Default) at).value();
						}
					}
					//必填
					if(require && StringUtil.isNotEmpty(fieldName)){
						if(CollectionUtil.isEmpty(fieldMap.get(fieldName)) && CollectionUtil.isEmpty(fileMap.get(fieldName))){
							error.add(fieldName);
						}
					}
					//默认值
					if(fieldValue != null && StringUtil.isNotEmpty(fieldName)){
						List<FormParam> formParamList = param.getFormParamList();
						boolean findFieldName = false;
						for(FormParam fp:formParamList){
							if(fp.getFieldName().equals(fieldName)){
								findFieldName = true;
								if(fp.getFieldName() == null){
									fp.setFieldValue(fieldValue);
								}
								break;
							}
						}
						if(!findFieldName){
							formParamList.add(new FormParam(fieldName, fieldValue));
						}
					}
				}
			}
		}
		if(CollectionUtil.isNotEmpty(error)){
			throw new RestException("参数异常，"+error.toString()+"空");
		}
		return true;
	}

	@Override
	public void doEnd() {
		
	}

}
