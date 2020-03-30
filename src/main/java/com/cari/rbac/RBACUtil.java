package com.cari.rbac;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.cari.rbac.Module;
import com.cari.rbac.ModuleManage;
import com.cari.rbac.Role;
import com.cari.rbac.RoleModuleRightManage;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sys.biz.SystemConstant;

/**
 * RBAC工具类
 * @author linliangyi@caripower
 *
 */
public class RBACUtil {

	public RBACUtil() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	/*
	 * 获得授权的菜单脚本
	 * @param SysUser user 当前登录的用户
	 */
	public static String getMenuScript(Set roles, HttpSession hsession){
		String menuStr = (String)hsession.getAttribute("MENU_FOR_MYROLE");				
		if (menuStr != null) {
			return menuStr;
		}
		RoleModuleRightManage rmrm = RoleModuleRightManage.getInstance();
		ModuleManage mm = ModuleManage.getInstance();
		HashSet moduleIdSet = new HashSet();
		TreeMap tmModule1_2 = new TreeMap();
		
		Session session = HibernateUtil.getInstance().openSession();
		try{
			
			for(Iterator it = roles.iterator() ; it.hasNext() ;){
				Role role = (Role)it.next();
				List rmrList = rmrm.getValidModuleIds(role.getRole_id(), session);
				for(Iterator itRmr = rmrList.iterator(); itRmr.hasNext(); ){
					String module_id = (String)itRmr.next();
					//判断HashSet的缓存中是否已有相关模块
					if(moduleIdSet.contains(module_id)) {
						continue;
					}
					
					moduleIdSet.add(module_id);
					//取二级模块实例
					Module lvl2Module = mm.getModuleById(module_id, session);
					if (lvl2Module == null) {
						continue;
					} 

					//取一级模块实例
					String lvl1ModuleId = module_id.substring(0 , module_id.indexOf('-'));
					Module lvl1Module = mm.getModuleById(lvl1ModuleId, session);
					if (lvl1Module == null) {
						continue;
					} 
		
					//从TreeMap取二级的TreeSet
					TreeSet lvl2ModuleSet = (TreeSet)tmModule1_2.get(lvl1Module);
					if(lvl2ModuleSet == null){
						lvl2ModuleSet = new TreeSet();
						tmModule1_2.put(lvl1Module , lvl2ModuleSet);
					}
					//向TreeSet中加入二级模块
					lvl2ModuleSet.add(lvl2Module);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			session.close();
		}

		
		//遍历TreeMap和二级TreeSet生成MenuScript
		StringBuffer menuScript = new StringBuffer();
		for(Iterator it = tmModule1_2.keySet().iterator(); it.hasNext() ;){
			//取出一级模块
			Module module1 = (Module)it.next();
			menuScript.append(module1.getModuleName()).append("///");
			//取出二级模块集合
			TreeSet lvl2ModuleSet = (TreeSet)tmModule1_2.get(module1);
			for(Iterator it2 = lvl2ModuleSet.iterator() ; it2.hasNext() ;){
				//取出二级模块
				Module module2 = (Module)it2.next();
				String moduleURL = (String)SystemConstant.URL_MOD_MAPPING.get(module2.getModuleID());
				if(moduleURL == null){
					moduleURL = "javascript:void(0)";
				}
				menuScript.append(module2.getModuleName())
					.append('*')
					.append(moduleURL)
					.append("||");
			}
			menuScript.delete(menuScript.length() - 2 ,  menuScript.length());
			menuScript.append("&&&&");
		}
		//System.out.println(menuScript.substring(0 , menuScript.length()-4));
		if(menuScript.length() > 4){
			menuStr = menuScript.substring(0 , menuScript.length() -4);
		}else{
			menuStr = "";
		}
		hsession.setAttribute("MENU_FOR_MYROLE", menuStr);		
		return menuStr;
	}
	

}
