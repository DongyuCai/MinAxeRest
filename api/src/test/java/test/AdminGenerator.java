package test;

import org.axe.util.MD5Util;
import org.axe.util.StringUtil;

public class AdminGenerator {
	public static void main(String[] args) {
		axe��¼����();
	}
	
	public static void ��̨����Ա����(){
		String password = "1234";
		String salt = StringUtil.getRandomString(8);
		
		password = MD5Util.getMD5Code(password);
		System.out.println(password);
		password = MD5Util.getMD5Code(password+salt);
		System.out.println(salt);
		System.out.println(password);
	}
	
	public static void axe��¼����(){
		String signin_id = "axe";
		String password = "axe";
		String axe_signin_token = MD5Util.getMD5Code(signin_id+":"+password);
		System.out.println(axe_signin_token);
	}
}

