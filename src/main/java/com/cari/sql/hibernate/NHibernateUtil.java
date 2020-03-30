package com.cari.sql.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.cari.wing.cache.SystemConfig;

/**
 * @author richfans
 *
 *	Hibernate 工具类  
 *
 */
public class NHibernateUtil {
	
    private static Log log = LogFactory.getLog(NHibernateUtil.class);
    public static final SessionFactory sessionFactory;
    static {
        try {
        	SystemConfig sysConfig = SystemConfig.loadSystemConfig();
        	Configuration configure = new Configuration().configure();
        	//数据库帐号
        	configure.setProperty("hibernate.connection.url", sysConfig.getJDBCURL());
        	configure.setProperty("hibernate.connection.driver_class", sysConfig.getJDBCDriverClass());
        	configure.setProperty("hibernate.connection.username", sysConfig.getJDBCUserName());
        	configure.setProperty("hibernate.connection.password", sysConfig.getJDBCPassword());
        	//C3P0连接池属性
        	configure.setProperty("hibernate.c3p0.max_size", sysConfig.getC3P0MaxPoolSize());
        	configure.setProperty("hibernate.c3p0.min_size", sysConfig.getC3P0MinPoolSize());
        	configure.setProperty("hibernate.c3p0.timeout", sysConfig.getC3P0MaxIdleTime());
        	configure.setProperty("hibernate.c3p0.max_statements", sysConfig.getC3P0MaxStatements());
        	configure.setProperty("hibernate.c3p0.acquire_increment", sysConfig.getC3P0AcquireIncrement());
        	configure.setProperty("hibernate.c3p0.idle_test_period", sysConfig.getC3P0IdleConnectionTestPeriod());
		
        	//创建连接池
        	sessionFactory = configure.buildSessionFactory();
        } catch (Throwable ex) {
        	ex.printStackTrace();
            log.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        } // end of the try - catch block
    } // end of static block

    public static final ThreadLocal localSession = new ThreadLocal();
    public static final ThreadLocal localTransaction = new ThreadLocal();
    
    public static Session openSession() throws HibernateException {
    	Session session = (Session)localSession.get();
    	if(session == null){
    		session = sessionFactory.openSession();
    		localSession.set(session);
    	}
    	
    	if(!session.isOpen()){
    		localSession.set(null);
    		session = sessionFactory.openSession();
    		localSession.set(session);
    	}
        return session;
    }
    
    public static void closeSession() throws HibernateException {
    	Session session = (Session)localSession.get();
    	localSession.set(null);
    	if(session != null){
	        session.close();
	        session = null;
    	}
    }
    
    public static Transaction beginTransaction() throws HibernateException {
    	Transaction tx = (Transaction)localTransaction.get();
    	if(tx == null){
    		tx = openSession().beginTransaction();
    		localTransaction.set(tx);
    	}
    	if(tx.wasCommitted()) {
    		tx = null;
    		tx = openSession().beginTransaction();
    		localTransaction.set(tx);
    	}
    	return tx;
    }

    public static void commitTransaction() throws HibernateException {
    	Transaction tx = (Transaction)localTransaction.get();
    	localTransaction.set(null);
    	if(tx != null){
    		tx.commit();
    		tx = null;
    	}
    }
    
    public static void rollbackTransaction() throws HibernateException {
    	Transaction tx = (Transaction)localTransaction.get();
    	localTransaction.set(null);
    	if(tx != null){
    		tx.rollback();
    		tx = null;
    	}
    }

    public static void main(String[] args){
    	try {
			Session session = NHibernateUtil.openSession();
			NHibernateUtil.beginTransaction();
			Query q = session.createSQLQuery("create table (id varchar2(10))");
			q.executeUpdate();
			NHibernateUtil.commitTransaction();
		} catch (Exception e) {
			NHibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			// No matter what, close the session
			NHibernateUtil.closeSession();
		}
    }
}