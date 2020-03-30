package com.cari.sql.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.SessionNotOpenException;


/**
 * @author 卓诗垚
 * Hibernate数据库基础操作
 */
public class BaseOperation {
	/**
     * 带翻页查询
     * @param pageNo 页码
     * @param qc 检索条件
     * @return
     */
    public ListPage queryPage(int pageNo, int pageCount, QueryCondition qc){
        ListPage listPage = null;
        try {
        	Session session = NHibernateUtil.openSession();
        	listPage = queryPage(pageNo, pageCount, qc, session);
        } catch (Exception e) {
        	e.printStackTrace();
        	listPage = ListPage.EMPTY_PAGE;
	        listPage.setCurrentPageSize(pageCount);
        } finally {
          // No matter what, close the session
        	NHibernateUtil.closeSession();
        }
        return listPage;
    }
    
    /**
     * 带翻页查询
     * @param pageNo
     * @param pageCount
     * @param qc
     * @param session
     * @return
     */
    public ListPage queryPage(int pageNo, int pageCount, QueryCondition qc, Session session) {
    	ListPage listPage = null;
        String hql = qc.getPrepareHql();
        if(hql != null){
        	//如果pageCount为0,则认为无限制,返回所有记录
        	int firstResultIndex = 0;
        	if (pageCount == 0){
        		firstResultIndex = 0;
        		pageCount = Integer.MAX_VALUE;
        	}else{
        		firstResultIndex = (pageNo - 1) * pageCount;
        	}
            Query query = session.createQuery(qc.getCountHql());
            qc.launchParamValues(query);
            int totalCount = ( (Long)query.iterate().next() ).intValue();            
            if (totalCount == 0 || firstResultIndex > totalCount) {
		        listPage = ListPage.EMPTY_PAGE;
            } else {
	          	query = session.createQuery(hql);
	          	qc.launchParamValues(query);
	          	query.setFirstResult(firstResultIndex);
	          	query.setMaxResults(pageCount);
	          	
	          	List result = query.list();
	          	
	          	listPage = new ListPage();
	          	listPage.setTotalSize(totalCount);
	          	listPage.setDataList(result);
            }
            listPage.setCurrentPageNo(pageNo);
          	listPage.setCurrentPageSize(pageCount);
        }
        return listPage;
    }
 
    /**
     * 根据条件执行HQL查询语句
     * @param hql
     * @param conditions
     * @param session
     * @return
     * @throws Exception
     * @throws SessionNotOpenException
     */
    public List executeQuery(String hql, Map conditions, int count, Session session) 
			throws Exception, SessionNotOpenException {
		if (session == null) {
			throw new SessionNotOpenException();
		}
		CommQueryCondition cqc = new CommQueryCondition();
		Query query = session.createQuery(hql);
		cqc.setConditions(conditions);
		cqc.launchParamValues(query);
		if (count > 0) {
			query.setMaxResults(count);
		}
		return query.list();
	}
    
    public List executeQuery(String hql, Map conditions, Session session) 
			throws Exception, SessionNotOpenException {
		return executeQuery(hql, conditions, 0, session);
	}

    /**
     * 根据条件执行HQL更新语句
     * @param hql
     * @param conditions
     * @param session
     * @return
     * @throws Exception
     * @throws SessionNotOpenException
     */
	public int executeUpdate(String hql, Map conditions, Session session) 
			throws Exception, SessionNotOpenException {
		if (session == null) {
			throw new SessionNotOpenException();
		}
		CommQueryCondition cqc = new CommQueryCondition();
		Query query = session.createQuery(hql);
		cqc.setConditions(conditions);
		cqc.launchParamValues(query);
		return query.executeUpdate();
	}

    /**
     * 保存Hibernate持久化对象
     * @param object
     * @return 
     */
	public boolean save(Object object) {
		boolean result = false;
		if (object == null) {
			return result;
		}
		try {
			Session session = NHibernateUtil.openSession();
			NHibernateUtil.beginTransaction();
			session.save(object);
			NHibernateUtil.commitTransaction();
			result = true;
		} catch (Exception e) {
			NHibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			NHibernateUtil.closeSession();
		}
		return result;
	}
	
	/**
	 * 更新Hibernate持久化对象
	 * @param object
	 * @return
	 */
	public boolean update(Object object) {
		boolean result = false;
		if (object == null) {
			return result;
		}
		try {
			Session session = NHibernateUtil.openSession();
			NHibernateUtil.beginTransaction();
			session.update(object);
			NHibernateUtil.commitTransaction();
			result = true;
		} catch (Exception e) {
			NHibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			NHibernateUtil.closeSession();
		}
		return result;
	}

	/**
	 * 删除Hibernate持久化对象
	 * @param object
	 * @return
	 */
	public boolean delete(Object object) {
		boolean result = false;
		if (object == null) {
			return result;
		}
		try {
			Session session = NHibernateUtil.openSession();
			NHibernateUtil.beginTransaction();
			session.delete(object);
			NHibernateUtil.commitTransaction();
			result = true;
		} catch (Exception e) {
			NHibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			NHibernateUtil.closeSession();
		}
		return result;
	}
	
	/**
	 * 根据ID获取实例
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public Object getByID(Class c, String id) {
		Object object = null;
		try {
			Session session = NHibernateUtil.openSession();
			object = session.get(c, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			NHibernateUtil.closeSession();
		}
		return object;
	}    
}
