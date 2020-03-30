package com.cari.rbac;

import java.util.Iterator;
import java.util.Set;


/**
 * RBAC权限许可类
 * 功能：验证用户操作权限
 * @author Administrator
 *
 */
public class RBACPermission {

	public RBACPermission() {
		super();
	}
	
	/**
	 * 鉴权
	 * @param userRoleSet 用户的角色列表
	 * @param module_id 模块ID
	 * @param operate_id 操作ID
	 * @return
	 */
	public static boolean checkPermission(Set userRoleSet , String module_id , String operate_id){
		//对非法参数说不
		if(userRoleSet == null || userRoleSet.size() == 0 
				|| module_id == null || operate_id == null 
				|| module_id.equals("") || operate_id.equals("")){
			return false;
		}
		
		//确认对应的模块是否有相关操作
		ModuleManage mm = ModuleManage.getInstance();
		Module mod = mm.loadModulesWithOperate(module_id);
		//获取系统操作的对象
		Operation op = null;
		for(Iterator it = mod.getOperations().iterator() ; it.hasNext() ; ){
			Operation tmp = (Operation)it.next();
			if(tmp.getOperate_id().equals(operate_id)){
				op = tmp;
				break;
			}
		}
		
		if(op != null){ //该模块的确拥有该操作
			RoleModuleRightManage rmrm = RoleModuleRightManage.getInstance();
			for(Iterator it = userRoleSet.iterator() ; it.hasNext() ; ){
				Role role = (Role)it.next();
				//获取角色-模块的权值
				//System.out.println("role.getRole_id()=" + role.getRole_id() + "    module_id=" + module_id );
				RoleModuleRight rmr = rmrm.getModuleRightByKey(role.getRole_id() , module_id);
				//进行权值&比较
				if(rmr != null){
					if((rmr.getRight_value()&op.getOperate_value()) > 0){
						
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean checkPermission(Set userRoleSet , String module_id){
		//对非法参数说不
		if(userRoleSet == null || userRoleSet.size() == 0 
				|| module_id == null || module_id.equals("")){
			return false;
		}
		
		RoleModuleRightManage rmrm = RoleModuleRightManage.getInstance();
		for(Iterator it = userRoleSet.iterator() ; it.hasNext() ; ){
			Role role = (Role)it.next();
			//获取角色-模块的权值
			RoleModuleRight rmr = rmrm.getModuleRightByKey(role.getRole_id() , module_id);
			//进行权值&比较
			if(rmr != null && rmr.getRight_value()> 0){
				return true;
			}
		}
		
		return false;
	}
}
