package com.jeeba.sys.rest;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.axe.annotation.ioc.Autowired;
import org.axe.annotation.ioc.Controller;
import org.axe.annotation.mvc.Request;
import org.axe.annotation.mvc.RequestParam;
import org.axe.constant.RequestMethod;
import org.axe.exception.RestException;
import org.axe.util.StringUtil;

import com.jeeba.sys.constant.UserContext;
import com.jeeba.sys.entity.User;
import com.jeeba.sys.entity.UserRole;
import com.jeeba.sys.entity.UserToken;
import com.jeeba.sys.service.RoleMenuService;
import com.jeeba.sys.service.UserRoleService;
import com.jeeba.sys.service.UserService;
import com.jeeba.sys.service.UserTokenService;


@Controller(basePath = "/sys/login",title="登录")
public class LoginRestController {
	@Autowired
	private UserTokenService userTokenService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleMenuService roleMenuService;
//	@Autowired
//	private RedisService redisService;
	
	@Request(value = "/code_{codeIndex}", method=RequestMethod.GET,title="获取验证码")
	public void code(@RequestParam("codeIndex")String index,HttpServletResponse response) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			String code = drawImg(output);
			//存到redis，60秒过期
//			redisService.saveCode(index,code);
			
			//写到前台
			ServletOutputStream out = response.getOutputStream();
			output.writeTo(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String drawImg(ByteArrayOutputStream output) {
		String code = StringUtil.getRandomString(4);
		int width = 70;
		int height = 39;
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman", Font.PLAIN, 20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66, 2, 82);
		g.setColor(color);
		g.setBackground(new Color(226, 226, 240));
		g.clearRect(0, 0, width, height);
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int) x, (int) baseY);
		g.dispose();
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	@Request(value="", method = RequestMethod.POST,title="登录")
	public Object login(
			@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam("code")String code,
			@RequestParam("codeIndex")String codeIndex,
			@RequestParam("saveLogin")String saveLogin,
			HttpServletRequest request) {
		//验证code验证码
		/*String oldCode = redisService.getCode(codeIndex);
		if(oldCode == null){
			throw new RestException(RestException.SC_UNAUTHORIZED,"验证码已过期，请重新获取验证码");
		}*/
		//不区分大小写
		/*if(!oldCode.equalsIgnoreCase(code)){
			throw new RestException(RestException.SC_UNAUTHORIZED,"验证码错误");
		}*/
		
		
		//验证并获取token
		UserToken userToken = userTokenService.login(username, password, saveLogin,request);
		if(userToken == null){
			throw new RestException(RestException.SC_UNAUTHORIZED,"登录名或密码错误");
		}
		
		Map<String,Object> resultInfo = new HashMap<String,Object>();
		resultInfo.put("token", userToken.getToken());
		if(userToken.getExpire() != null){
			resultInfo.put("expire", userToken.getExpire().getTime());
		}
		if(userToken.getCookieExpire() != null){
			resultInfo.put("cookieExpire", userToken.getCookieExpire().getTime());
		}
		
		//删除Redis的验证码
//		redisService.cleanCode(codeIndex);
		
		return resultInfo;
	}
	
	@Request(value = "/userInfo", method = RequestMethod.GET,title="获取用户信息")
	public Object getUserInfo(HttpServletRequest request) {
		User user = UserContext.getUser();
		if(user == null){
			throw new RestException("您的登录账号不存在！");
		}
		//拿到角色
		List<UserRole> userRoleList = userRoleService.getListByUserId(user.getId());
		String rolesStr = userRoleService.getRolesStrByUserRoleList(userRoleList);
		//拿到菜单
		StringBuffer menusStr_buf = new StringBuffer();
		for(int i=0;userRoleList != null && i<userRoleList.size();i++){
			menusStr_buf.append(roleMenuService.getMenusStrByRoleId(userRoleList.get(i).getRoleId()));
		}
		String menusStr = menusStr_buf.toString();
		
		
		Map<String,String> userInfo = new HashMap<String,String>();
		userInfo.put("id", String.valueOf(user.getId()));
		userInfo.put("username", user.getUsername());
		userInfo.put("role", rolesStr);
		userInfo.put("menu", menusStr);
		return userInfo;
	}
	

	@Request(value="/password",method = RequestMethod.PUT,title="修改密码")
	public Object editPassword(
			@RequestParam("password")String password,
			@RequestParam("oldPassword")String oldPassword,
			HttpServletRequest request) {
		User user = UserContext.getUser();
		userService.editPassword(user.getId(), password, oldPassword);
		HashMap<String, Object> res = new HashMap<String, Object>();
		return res;
	}
}
	