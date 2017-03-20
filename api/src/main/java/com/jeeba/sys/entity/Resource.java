package com.jeeba.sys.entity;

import org.axe.annotation.persistence.ColumnDefine;
import org.axe.annotation.persistence.Table;
import org.axe.annotation.persistence.Unique;

import com.jeeba.common.entity.IdEntity;

/**  
 * Filename:    Resource.java  
 * 资源
 * ------------------------------------------------------------------  
 * 2015年12月30日	蔡东余		1.0			1.0 Version  
 */  
@Table(value="sys_resource")
public class Resource extends IdEntity{
	//标题
	private String title;
	
	@Unique
	private String url;
	
	//GET POST ... 这些
	@Unique
	private String type;
	
	//public java.lang.Object 这样的，一个到方法的签名
	private String classMethod;
	
	//关键字用于搜索，title+url+classMethod
	@ColumnDefine(" varchar(100) NOT NULL DEFAULT '' ")
	private String keywords;
	
	
	//*** 下面都是没用的，不用看
	@Override
	public int hashCode() {
		return (this.url+this.type).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Resource){
			Resource other = (Resource)obj;
			if(other.url.equals(this.url) && other.type.equals(this.type)){
				return true;
			}
		}
		return false;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassMethod() {
		return classMethod;
	}
	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}
