/*
 * 创建日期 2006-1-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.cari.sys.biz;


import java.sql.Timestamp;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import com.cari.sql.DBKeyCreator;
import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sql.hibernate.QueryCondition;
import com.cari.sys.bean.SysEventLog;
import com.cari.web.comm.ListPage;


/**
 * @author fuquanming
 *
 */
public class EventLogManage extends BaseOperation {
    /**
     * 保存事件日志对象
     * @param eventLog 事件日志对象
     * @throws Exception
     */
    public static void save(SysEventLog eventLog) throws Exception {  
    	Session session = HibernateUtil.getInstance().openSession();
        Transaction tx = null;
        try {
			tx = session.beginTransaction();
			session.save(eventLog);
			tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new Exception(e);
        } finally {
          //No matter what, close the session
          session.close();
        }
    }
    
    public static void saveLog(String userID, String userName, String event, 
    		String model, String ip, String eventObject, String eventResult, Session session) {
        SysEventLog log = new SysEventLog();
        log.setDwKey(DBKeyCreator.getRandomKey(12));
        log.setUserID(userID);
        log.setUserName(userName);
        log.setEventTime(new Timestamp(System.currentTimeMillis()));
        log.setEvent(event);
        log.setModel(model);
        log.setIp(ip);
        log.setEventObject(eventObject);
        log.setEventResult(eventResult);
        try {
            session.save(log);
        } catch(Exception e) {
        }
    }
    
    public static void saveLog(String userID, String userName, String event, 
    		String model, String ip, String eventObject, String eventResult) {
        SysEventLog log = new SysEventLog();
        log.setDwKey(DBKeyCreator.getRandomKey(12));
        log.setUserID(userID);
        log.setUserName(userName);
        log.setEventTime(new Timestamp(System.currentTimeMillis()));
        log.setEvent(event);
        log.setModel(model);
        log.setIp(ip);
        log.setEventObject(eventObject);
        log.setEventResult(eventResult);
        try {
            save(log);
        } catch(Exception e) {
        }
    }
    
    /**
     * 根据id查询事件日志
     * @param id 事件日志的id
     * @return
     */
    public SysEventLog getEventLogByID(String id) {
        SysEventLog eventLog = null;
        
        if(id != null) {
        	Session session = HibernateUtil.getInstance().openSession();
	        try {
	          Criteria criteria = session.createCriteria(SysEventLog.class);
	          criteria.add(Expression.eq("dwKey",id));
	          eventLog = (SysEventLog)criteria.uniqueResult();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	          // No matter what, close the session
	          session.close();
	        }
        }   
        return eventLog;
    }   
    
    /**
     * 删除事件日志组
     * @param ids 事件日志组
     * @throws Exception
     */
    public void deleteEventLogs(String[] ids) throws Exception {
    	Session session = HibernateUtil.getInstance().openSession();
        Transaction tx = null; 
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("delete from SysEventLog where dwKey=:dwKey");
            for(int i = 0; i < ids.length ; i++) {
                query.setString("dwKey" , ids[i]);
                query.executeUpdate();
            }
            tx.commit();
        }catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
          // No matter what, close the session
            session.close();
        }
    }
    
    /**
     * 带翻页的事件日志查询
     * @param pageNo 页码
     * @param qc 事件日志检索条件
     * @return
     */
    public ListPage queryPage(int pageNo, QueryCondition qc) {
		int pageCount = 10;// 临时系统参数
		return queryPage(pageNo, pageCount, qc);
	}
}
