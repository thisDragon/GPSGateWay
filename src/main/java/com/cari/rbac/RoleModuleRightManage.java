package com.cari.rbac;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.NHibernateUtil;
import com.cari.sys.bean.SysUser;

public class RoleModuleRightManage extends BaseOperation{

	private static RoleModuleRightManage mrm;
	
	private RoleModuleRightManage() {
		super();
	}
	
	public static RoleModuleRightManage getInstance() {
		if (mrm == null) {
			mrm = new RoleModuleRightManage();
		}
		return mrm;
	}

	/**
	 * 根据角色ID，获取模块的权值列表
	 * @param role_id 角色ID
	 * @return  角色用户的模块权值列表
	 */
	public List getModuleRightByRole(String role_id, Session session) throws Exception {
		Query query = session.createQuery("from RoleModuleRight as rmr where rmr.role_id=:role_id");
		query.setString("role_id", role_id);
		return query.list();
	}
	
	public List getValidModuleIds(String role_id, Session session) throws Exception{
		if (role_id == null) {
			return null;
		}
		Query query = session.createQuery("select rm.module_id from RoleModuleRight as rm " +
				"where rm.role_id=:roleId and rm.right_value>0");
		query.setString("roleId", role_id);
		return query.list();

	}
	/**
	 * 根据角色ID和模块ID获取相应的操作权值
	 * @param role_id CPK
	 * @param module_id CPK
	 * @return
	 */
	public RoleModuleRight getModuleRightByKey(String role_id , String module_id){
		RoleModuleRight mr = null;
		if(role_id != null && module_id != null){
			mr = new RoleModuleRight();
			mr.setRole_id(role_id);
			mr.setModule_id(module_id);
			try{
				Session session = NHibernateUtil.openSession();
				mr = (RoleModuleRight)session.get(RoleModuleRight.class , mr);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				NHibernateUtil.closeSession();
			}
		}
		return mr;
	}
	
	/**
	 * 根据角色ID和模块ID获取相应的操作权值
	 * @param role_id CPK
	 * @param module_id CPK
	 * @return
	 */
	public Integer getModuleRightValue(String role_id , String module_id, Session session){
		Query query = session.createQuery("select rmr.right_value from RoleModuleRight as rmr " +
				"where rmr.module_id=:module_id and rmr.role_id=:role_id");
		query.setString("module_id", module_id);
		query.setString("role_id", role_id);
		if (query.iterate().hasNext()) {
			return (Integer)query.iterate().next();
		}
		return new Integer(0);
	}
	
	/**
	 * 重载超类方法
	 * @param c
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Object getByID(Class c, String id) {
		throw new UnsupportedOperationException("This method unsupport composite primery key!");
	}

	/**
	 * 清除角色相关的所有操作权值
	 * @param role_id
	 * @return
	 */
	public void clearModuleRightByRole(String role_id, Session session) throws Exception {
		Query query = session.createQuery("delete from RoleModuleRight where role_id=:role_id");
		query.setString("role_id" , role_id);
		query.executeUpdate();		
	}	

	/**
	 * 获取系统超级权限
	 * @return
	 */
	public void createSuperRight(String role_id, Session session,SysUser user) throws Exception {
		//清除旧模块权限
		this.clearModuleRightByRole(role_id, session);
		
		//新增所有模块
		Query query = session.createQuery("select m.moduleID from Module as m where " +
				"m.moduleLevel <> 1 order by m.moduleLevel,m.moduleSort");
        List moduleList = query.list();
        String userId = user.getUserId();
		for(Iterator it = moduleList.iterator() ; it.hasNext() ;){
			String moudleId = (String)it.next();
			if(!userId.equals("admin") && (moudleId.equals("SYS-CFG") || moudleId.equals("SYS-DICT")) || moudleId.equals("SYS-AREA")){
	        	continue;
	        }
			RoleModuleRight superRight = new RoleModuleRight();
			superRight.setRole_id(role_id);
			superRight.setModule_id(moudleId);
			superRight.setRight_value(Integer.MAX_VALUE);
			session.save(superRight);
		}
	}
	
	public void updateModuleRight(String role_id, String module_id, int right_value, 
			Session session) throws Exception {
		Query query = null;
		if (right_value <= 0) {
			query = session.createQuery("delete RoleModuleRight where " +
				"module_id=:module_id and role_id=:role_id");
			query.setString("module_id", module_id);
			query.setString("role_id", role_id);
			query.executeUpdate();
			return;
		}
		query = session.createQuery("from RoleModuleRight as rmr where " +
			"rmr.module_id=:module_id and rmr.role_id=:role_id");
		query.setString("module_id", module_id);
		query.setString("role_id", role_id);
		Iterator it = query.iterate();
		if (it.hasNext()) {
			RoleModuleRight rmr = (RoleModuleRight)it.next();
			rmr.setRight_value(right_value);
		} else {
			RoleModuleRight rmr = new RoleModuleRight();
			rmr.setModule_id(module_id);
			rmr.setRole_id(role_id);
			rmr.setRight_value(right_value);
			session.save(rmr);
		}
	}
}
