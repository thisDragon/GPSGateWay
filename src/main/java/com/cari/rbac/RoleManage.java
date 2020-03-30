package com.cari.rbac;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.NHibernateUtil;
import com.cari.web.exception.BeRefuseException;


/**
 * @author Administrator linly@caripower
 * 2006.6.21
 */
public class RoleManage extends BaseOperation{

	private static Logger logger =LoggerFactory.getLogger(RoleManage.class);
	
	private static RoleManage rm;
	private RoleManage() {
		super();
	}
	
	public static RoleManage getInstance() {
		if (rm == null) {
			rm = new RoleManage();
		}
		return rm;
	}
	/**
	 * 获取所有的角色对象
	 * @return 角色对象列表
	 */
	public List findAllRoles(){
		List allRoles = null;
        Query query = null;
        try {
        	Session session = NHibernateUtil.openSession();
        	query = session.createQuery("from Role");
        	allRoles = query.list();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	NHibernateUtil.closeSession();
        }
        return allRoles;
	}
	
	/**
	 * 获取下一个角色顺序编号
	 * @return　下一个角色顺序编号
	 */
	public long getNextRoleOrder(){
		long nextOrder = 0;
        Query query = null;
        try {
        	Session session = NHibernateUtil.openSession();
        	query = session.createQuery("select count(*) from Role");
        	nextOrder = ((Long)query.uniqueResult()).longValue();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	NHibernateUtil.closeSession();
        }
		return nextOrder;
	}
	
	public static String getRoleNameById(String role_id){
		
		if(Role.ROOT.getRole_id().equals(role_id)){
			return Role.ROOT.getRole_name();
		}
		String inst = null;
		try {
        	Session session = NHibernateUtil.openSession();
        	Query query = session.createQuery("select r.role_name from Role as r " +
        			"where r.role_id=:role_id");
        	query.setString("role_id", role_id);
        	inst = (String)query.uniqueResult();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
        	NHibernateUtil.closeSession();
        }
		return inst;
	}
	
	/**
	 * 根据id获取role
	 * @param role_id
	 * @return role实例
	 * @throws Exception 
	 */
	public Role getRoleById(String role_id){
		Role inst = null;
		if(Role.ROOT.getRole_id().equals(role_id)){
			inst = Role.ROOT;
		}else{
			inst = (Role)getByID(Role.class , role_id);
		}
		return inst;
	}
	
	public String getRoleNameById(String role_id, Session session) throws Exception {
		if (Role.ROOT.getRole_id().equals(role_id)) {
			return Role.ROOT.getRole_name();
		}
		Query query = session.createQuery("select r.role_name from Role as r where r.role_id=:roleID");
		query.setString("roleID", role_id);
		return (String)query.uniqueResult();
	}

	public void checkRoleName(String role_name, Session session, String old_role_name) throws Exception {
		Query query = session.createQuery("select count(r) from Role as r where r.role_name=:name");
		query.setString("name", role_name);
		long c = ((Long)query.uniqueResult()).longValue();
		if (c > 0 && !role_name.equals(old_role_name)) {
			throw new BeRefuseException("已存在名为“" + role_name + "”的角色！");
		}
	}
	/**
	 * 删除角色对象，同时删除对应的角色模块权限对象
	 * @param role
	 * @throws Exception
	 */
	public void delete(Role role) throws Exception{
		if (role != null) {
			try {
				Session session = NHibernateUtil.openSession();
				NHibernateUtil.beginTransaction();
				Query query = session.createQuery("delete from RoleModuleRight where role_id=:role_id");
				query.setString("role_id" , role.getRole_id());
				query.executeUpdate();
				session.delete(role);
				NHibernateUtil.commitTransaction();
			} catch (Exception e) {
				NHibernateUtil.rollbackTransaction();
				throw e;
			} finally {
				NHibernateUtil.closeSession();
			}
		}
	}
	
	/**
	 * 删除用户——角色对应表中，与本角色相关的记录
	 * @param role_id
	 * @throws Exception
	 */
	public void deleteUserRoleById(String role_id, Session session) throws Exception{
		if (role_id != null) {
			session.doWork(
	          new Work() {
	              public void execute(Connection connection) throws SQLException {  
	            	PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement("delete from sys_rbac_usertorole where role_id=?");
						ps.setString(1,role_id);
		      			ps.execute();
					} catch (SQLException e) {
						logger.error("数据库异常:"+e);
						e.printStackTrace();
					}finally{
						if (ps!=null) {
							ps.close();
						}
					}
	              }  
	            }
	          );
		}
	}
}
