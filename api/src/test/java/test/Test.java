package test;

import java.util.HashMap;
import java.util.Map;

public class Test {
	private String name;
	private String mobile;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMobile_(){
		return mobile;
	}
	
	public void setMobile_(String mobile){
		this.mobile = mobile;
	}
	
	public static void main(String[] args) {
		
		/*Test test = new Test();
		test.setMobile_("123");
		test.setName("name");
		String json = JsonUtil.toJson(test);
		System.out.println(json);
		
		List<EntityFieldMethod> getMethodList = ReflectionUtil.getGetMethodList(Test.class);
		for(EntityFieldMethod efm:getMethodList){
			System.out.println(efm.getField().getName());
		}*/
		
//		System.out.println(MD5Util.getMD5Code("e10adc3949ba59abbe56e057f20f883e"+"w0qaq8dk"));
		
		try {
//			Axe.init();
//			
//			System.out.println(ConfigHelper.getAppAssetPath());
//			System.out.println(StringUtil.getRandomString(32));
			
			Map<String,String> map1 = new HashMap<>();
			map1.put("sign", "1");
			
			Map<String,String> map2 = new HashMap<>();
			map2.putAll(map1);

			System.out.println(map2.get("sign"));
			map2.remove("sign");
			
			System.out.println(map1.get("sign"));
			System.out.println(map2.get("sign"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
