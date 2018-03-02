package test;

import org.axe.util.MD5Util;
import org.axe.util.StringUtil;

public class AdminGenerator {
	public static void main(String[] args) {
		axe登录密码();
	}
	
	public static void 后台管理员密码(){
		String password = "1234";
		String salt = StringUtil.getRandomString(8);
		
		password = MD5Util.getMD5Code(password);
		System.out.println(password);
		password = MD5Util.getMD5Code(password+salt);
		System.out.println(salt);
		System.out.println(password);
	}
	
	public static void axe登录密码(){
		String signin_id = "axe";
		String password = "axe";
		String axe_signin_token = MD5Util.getMD5Code(signin_id+":"+password);
		System.out.println(axe_signin_token);
	}
}

