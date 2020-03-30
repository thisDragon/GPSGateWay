/*
 * 创建日期 2005-12-12
 * 林良益 @ caripower
 */
package com.cari.sql.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 * @author linliangyi@team of miracle
 *
 *	Hibernate 工具类  
 *
 */
public class HibernateUtil {
    
    public static final String FLAG_UNSAVE = "unsave";
    private static HibernateUtil singleton;

    private HibernateUtil(){

    }

	public static void main(String[] args) {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tx = null;
        try {
        	tx = session.beginTransaction();
        	Query query = session.createQuery("delete table");
        	query.executeUpdate();
        	tx.commit();
        }catch (Exception e) {
        	if (tx != null) {
        		tx.rollback();
        	}
            e.printStackTrace();
        } finally {
          session.close();
          session = null;
        }
	} 

   
    public static HibernateUtil getInstance(){
    	if(singleton == null){
    		singleton = new HibernateUtil();
    	}
    	return singleton;
    }
   
    public Session openSession(){
        return NHibernateUtil.sessionFactory.openSession();
    }
}