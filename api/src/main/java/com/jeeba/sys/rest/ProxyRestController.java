package com.jeeba.sys.rest;

import org.axe.annotation.ioc.Controller;
import org.axe.annotation.mvc.Request;
import org.axe.annotation.mvc.RequestParam;
import org.axe.constant.RequestMethod;
import org.axe.util.HttpUtil;

@Controller(basePath="/sys/cors-proxy",title="访问跨域网站")
public class ProxyRestController {

	@Request(value="",method=RequestMethod.GET,title="GET请求")
	public Object doProxy(
			@RequestParam("address")String address,
			@RequestParam("baseUrl")String baseUrl,
			@RequestParam("charset")String charset){
		try {
			String result = HttpUtil.sendGet(baseUrl+address,charset);
			return result;
		} catch (Exception e) {}
		return "";
	}
}
