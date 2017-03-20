package test;

import org.axe.util.MD5Util;
import org.axe.util.StringUtil;

public class AdminGenerator {
	public static void main(String[] args) {
		//admin
//		String password = "ybsl1234";
		//caidongyu
		String password = "1234";
		String salt = StringUtil.getRandomString(8);
		
		password = MD5Util.getMD5Code(password);
		System.out.println(password);
		password = MD5Util.getMD5Code(password+salt);
		System.out.println(salt);
		System.out.println(password);
		
		
	}
}

