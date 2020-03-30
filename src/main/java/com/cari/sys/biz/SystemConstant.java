/*
 * 创建日期 May 8, 2006
 *
 */
package com.cari.sys.biz;

import java.util.HashMap;
import java.util.List;

import com.cari.rbac.Module;
import com.cari.rbac.ModuleManage;

public class SystemConstant {

	public static final String SESSION_ORGANENTITY = "SESSION_ORGANENTITY";
	public static final String SESSION_CATEGORY = "SESSION_CATEGORY";
	public static final String SESSION_REGIONENTITY = "SESSION_REGIONENTITY";
	public static final String SESSION_FILEUPLOAD = "SESSION_FILEUPLOAD";
	public static final String SESSION_USERQUERYCONDITION = "SESSION_USERQUERYCONDITION";
	public static final String SESSION_USER = "SESSION_USER";
	public static final String SESSION_ROLE = "SESSION_ROLE";
	public static final String SESSION_POIENTITY = "SESSION_POIENTITY";
	public static final String SESSION_DATACOPY_POIENTITY = "SESSION_DATACOPY_POIENTITY";
	public static final String SESSION_POIQUERYCONDITION = "SESSION_POIQUERYCONDITION";
	public static final String SESSION_POIAPPROVEQUERYCONDITION = "SESSION_POIAPPROVEQUERYCONDITION";
	public static final String SESSION_CHECKQUERYCONDITION = "SESSION_CHECKQUERYCONDITION";
	public static final String SESSION_MAPLAYER = "SESSION_MAPLAYER";
	//拥有全部权限标志
	public static final String ALLRIGHTS = "ALLRIGHTS";
	//没有任何权限标志
	public static final String NULLRIGHTS = "NULLRIGHTS";
	
	//定义列表的默认条目数
	public static final int LIST_DEFAULT_COUNT = 20;
	
	//注册用户时的默认密码
	public static final String USERMANAGE_DEFAULT_PASSWORD = "888888";
	
	//URL-模块ID映射表
	public static final HashMap URL_MOD_MAPPING = new HashMap();
	static {
		/*
		Properties prop = new Properties();
		try {
			prop.load(SystemConstant.class.getResourceAsStream("moduleurl.properties"));
			Enumeration em = prop.propertyNames();
			while (em.hasMoreElements()) {
				String name = (String)em.nextElement();
				URL_MOD_MAPPING.put(name, prop.getProperty(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		//初始化URL_MOD_MAPPING
		//1.群集配置
//		URL_MOD_MAPPING.put("/manage/module?act=cluster-cfg" , "CLUSTER-CFG");
//		URL_MOD_MAPPING.put("CLUSTER-CFG" , "../manage/module?act=cluster-cfg");
//		//2.服务器状态
//		URL_MOD_MAPPING.put("/manage/module?act=cluster-status" , "CLUSTER-STATUS");
//		URL_MOD_MAPPING.put("CLUSTER-STATUS" , "../manage/module?act=cluster-status");
//		//3.群集日志
//		URL_MOD_MAPPING.put("/manage/module?act=cluster-log" , "CLUSTER-LOG");
//		URL_MOD_MAPPING.put("CLUSTER-LOG" , "../manage/module?act=cluster-log");
//		//4.数据分类管理
//		URL_MOD_MAPPING.put("/manage/module?act=data-category" , "DATA-CATEGORY");
//		URL_MOD_MAPPING.put("DATA-CATEGORY" , "../manage/module?act=data-category");
//		//5.地图图层管理
//		URL_MOD_MAPPING.put("/manage/module?act=data-layer" , "DATA-LAYER");
//		URL_MOD_MAPPING.put("DATA-LAYER" , "../manage/module?act=data-layer");
//		//6.地名注记管理
//		URL_MOD_MAPPING.put("/manage/module?act=data-poi" , "DATA-POI");
//		URL_MOD_MAPPING.put("DATA-POI" , "../manage/module?act=data-poi");
//		//7.地图分块管理
//		URL_MOD_MAPPING.put("/manage/module?act=map-block" , "MAP-BLOCK");
//		URL_MOD_MAPPING.put("MAP-BLOCK" , "../manage/module?act=map-block");
//		//8.更新方案管理
//		URL_MOD_MAPPING.put("/manage/module?act=map-scheme" , "MAP-SCHEME");
//		URL_MOD_MAPPING.put("MAP-SCHEME" , "../manage/module?act=map-scheme");
//		//9.更新任务管理
//		URL_MOD_MAPPING.put("/manage/module?act=map-task" , "MAP-TASK");
//		URL_MOD_MAPPING.put("MAP-TASK" , "../manage/module?act=map-task");
//		//10.强制性更新任务
//		URL_MOD_MAPPING.put("/manage/module?act=map-forcetask" , "MAP-FORCETASK");
//		URL_MOD_MAPPING.put("MAP-FORCETASK" , "../manage/module?act=map-forcetask");
//		//11.系统参数管理
//		URL_MOD_MAPPING.put("/manage/module?act=sys-cfg" , "SYS-CFG");
//		URL_MOD_MAPPING.put("SYS-CFG" , "../manage/module?act=sys-cfg");
//		//12.区域管理
//		URL_MOD_MAPPING.put("/manage/module?act=sys-area" , "SYS-AREA");
//		URL_MOD_MAPPING.put("SYS-AREA" , "../manage/module?act=sys-area");
//		//13.组织机构管理
//		URL_MOD_MAPPING.put("/manage/module?act=sys-organ" , "SYS-ORGAN");
//		URL_MOD_MAPPING.put("SYS-ORGAN" , "../manage/module?act=sys-organ");
//		//14.角色管理
//		URL_MOD_MAPPING.put("/manage/module?act=sys-role" , "SYS-ROLE");
//		URL_MOD_MAPPING.put("SYS-ROLE" , "../manage/module?act=sys-role");
//		//15.权限配置
//		URL_MOD_MAPPING.put("/manage/module?act=sys-right" , "SYS-RIGHT");
//		URL_MOD_MAPPING.put("SYS-RIGHT" , "../manage/module?act=sys-right");
//		//16.用户管理
//		URL_MOD_MAPPING.put("/manage/module?act=sys-user" , "SYS-USER");
//		URL_MOD_MAPPING.put("SYS-USER" , "../manage/module?act=sys-user");
//		//17.个人资料维护
//		URL_MOD_MAPPING.put("/manage/module?act=userinfo-info" , "USERINFO-INFO");
//		URL_MOD_MAPPING.put("USERINFO-INFO" , "../manage/module?act=userinfo-info");
//		//18.修改密码
//		URL_MOD_MAPPING.put("/manage/module?act=userinfo-pwd" , "USERINFO-PWD");
//		URL_MOD_MAPPING.put("USERINFO-PWD" , "../manage/module?act=userinfo-pwd");
//		//19.分图总控制室
//		URL_MOD_MAPPING.put("/manage/module?act=map-control" , "MAP-CONTROL");
//		URL_MOD_MAPPING.put("MAP-CONTROL" , "../manage/module?act=map-control");
//		//20.数据字典
//		URL_MOD_MAPPING.put("/manage/module?act=sys-dict" , "SYS-DICT");
//		URL_MOD_MAPPING.put("SYS-DICT" , "../manage/module?act=sys-dict");
			
				
	}
	
	/**
	 * URL-模块ID映射表
	 */
	public static void setUrlModMapping() {
		List list = ModuleManage.getInstance().getAllModule();
		int size = list.size();
		Module m = null;
		String url = null;
		String key = null;
		for (int  i = 0; i < size; i++) {
			m = (Module) list.get(i);
			key = m.getModuleID();
			url = m.getModuleDesc();			
			if (m.getModuleLevel() > 1) {
				URL_MOD_MAPPING.put(key, url);
				URL_MOD_MAPPING.put(url.replace("..", ""), key);
			}
			m = null;
		}
		list.clear();
		list = null;
	}
	
	public static void main(String[] args) {
//		for (java.util.Iterator it = URL_MOD_MAPPING.keySet().iterator(); it.hasNext(); ) {
//			String key = (String)it.next();
//			String value = (String)URL_MOD_MAPPING.get(key);
//			System.out.println(key + "\n" + value + "\n\n");
//		}		
	}
}
