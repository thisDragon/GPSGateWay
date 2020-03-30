package com.cari.sys.biz;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sql.hibernate.QueryCondition;
import com.cari.sys.bean.Organ;
import com.cari.sys.bean.SysUser;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.exception.UserNotExistException;

/**
 * @author richfans
 *
 */
public class UserManage extends BaseOperation {
	
	private static UserManage um;
	
	private UserManage() {		
	}
	
	public static UserManage getInstance() {
		if (um == null) {
			um = new UserManage();
		}
		return um;
	}
	
	/**
	 * 保存
	 * @param user
	 * @return
	 */
	public boolean save(SysUser user) {
		boolean saveOK = false;
		if (user != null) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.save(user);
				tx.commit();
				saveOK = true;
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
			} finally {
				// No matter what, close the session
				session.close();
				session = null;
			}
		}
		return saveOK;
	}
	
	/**
	 * 更新
	 * @param user
	 * @return
	 */
	public boolean update(SysUser user) {
		boolean updateOK = false;
		if (user != null) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.update(user);
				tx.commit();
				updateOK = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null) {
					tx.rollback();
				}
			} finally {
				// No matter what, close the session
				session.close();
				session = null;
			}
		}
		return updateOK;
	}
	
	/**
	 * 删除
	 * @param user
	 * @return
	 */
	public boolean delete(SysUser user) {
		boolean delOK = false;
		if (user != null) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.delete(user);
				tx.commit();
				delOK = true;
			} catch (Exception e) {
				System.err.println(e.toString());
				if (tx != null) {
					tx.rollback();
				}
			} finally {
				// No matter what, close the session
				session.close();
				session = null;
			}
		}
		return delOK;
	}
	
	/**
	 * 删除一系列用户
	 * @param userIds
	 * @return
	 */
	public boolean delete(String[] userIds) {
		boolean delOK = false;
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tx = null;
        try {
        	tx = session.beginTransaction();
            for(int i = 0; i < userIds.length; i++) {
            	SysUser user = (SysUser)session.get(SysUser.class , userIds[i]);
            	session.delete(user);
            }
            tx.commit();
            delOK = true;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
            session = null;
        }
		return delOK;
	}

	/**
	 * 删除一个组织下的所有用户
	 * @param userIds
	 * @return
	 */
	public boolean deleteByOrgan(Organ organ) {
		boolean delOK = false;
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tx = null;
        try {
        	tx = session.beginTransaction();
            Query query = session.createQuery("delete from SysUser where dept like :dept");
            query.setString("dept" , organ.getOrganID() + "%");
            query.executeUpdate();
        tx.commit();
            delOK = true;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
		return delOK;
	}
	
	/**
	 * 根据userId获取SysUser
	 * @param userId
	 * @return
	 */
	public SysUser getUserById(String userId) {
		SysUser user = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			user = (SysUser)session.get(SysUser.class,userId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
		}
		return user;
	}
	
	/**
	 * 登陆验证
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean authenticate(String  userId  , String password) {
		boolean flag = false;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			Query query = session.createQuery("from SysUser where userId=:userId and password=:password");
			query.setString("userId" , userId);
			query.setString("password" , password);
			SysUser user = (SysUser)query.uniqueResult();
			flag = user != null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			session = null;
		}
		return flag;
	}
	
	public SysUser getLoninUser(String  userId  , String password) throws Exception {
		SysUser user = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			Query query = session.createQuery("from SysUser where userId=:userId");
			query.setString("userId" , userId);
			user = (SysUser)query.uniqueResult();
			if (user == null) {
				throw new UserNotExistException();
			} else if (!user.getPassword().equals(password)) {
				throw new BeRefuseException("用户密码错误");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
			session = null;
		}
		return user;
	}
    /**
     * 检查用户ID是否已近登记过
     * @param userID
     * @return 是否已登记
     */
    public boolean checkRegistedUserID(String userID){
        boolean result = false;
        if(userID != null && !userID.trim().equals("")){
            
	        Session session = HibernateUtil.getInstance().openSession();
	        try {
	          Query query = session.createQuery("select count(sysuser.userId) from SysUser as sysuser where sysuser.userId=:userId");
	          query.setString("userId",userID);
	          Long countUserID = (Long)query.uniqueResult();
	          if(countUserID == null || countUserID.longValue() == 0){
	              result = true;
	          }
	        }catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	          // No matter what, close the session
	          session.close();
	          session = null;
	        }
        }
        
        return result;
    }
	
    /**
     * 设置用户密码
     * @param userID 用户ID
     */
    public void modifyPassword(String userId, String password) throws Exception {
        Session session = HibernateUtil.getInstance().openSession();
        Transaction tx = null; 
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("update SysUser set password = :password where userId=:userId");
            query.setString("password", password);
            query.setString("userId",userId);
            query.executeUpdate();
            tx.commit();
        } catch(Exception e) {
            throw e;
        } finally {
            session.close();
            session = null;
        }
    }
    
    /**
     * 获取全部用户列表
     */
    public static List getAllUser(){
    	List userList=null;
    	Session session = HibernateUtil.getInstance().openSession();
    	Transaction tx=null;
    	try{
    		tx=session.beginTransaction();
    		Query query=session.createQuery("from SysUser order by userName");
    		userList=query.list();
    		tx.commit();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		session.close();
    		session = null;
    	}
    	return userList;
    }
    
	/**
	 * 带翻页查询
	 * @param pageNo
	 * @param qc
	 * @return
	 */
	public ListPage queryPage(int pageNo, QueryCondition qc) {
		return queryPage(pageNo, 20 , qc);
	}
	
	/**
	 * 以时间生成流水账号
	 * @return
	 */
	public String getNumberUserID() {
		Calendar now =  Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DATE);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		int no = (Math.abs(year - 2007) * 365 * 24 + month * 30 * 24 + day * 24 + hour)*1000;
		no += (int)(Math.random() * minute*60 + second);
		return Integer.toString(no);
	}
	
	public static void main(String[] args){
		
	}
}
