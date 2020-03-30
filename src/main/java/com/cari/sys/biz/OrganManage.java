/*
 * 创建日期 May 5, 2006
 * 对部门机构的逻辑层操作

 * 林良益@caripower
 */
package com.cari.sys.biz;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sys.bean.Organ;



public class OrganManage {

	private static OrganManage om;
	
	private OrganManage() {
		super();
	}
	
	public static OrganManage getInstance() {
		if (om == null) {
			om = new OrganManage();
		}
		return om;
	}
	
	
	/**
	 * 保存机构对像
	 * @param organ
	 * @throws Exception
	 */
    public void save(Organ organ) throws Exception{
        
    	if(organ != null && organ!= Organ.ROOT_ORGAN){
	        Session session = HibernateUtil.getInstance().openSession();
	        Transaction tx = null;
	        try {
				tx = session.beginTransaction();
				session.save(organ);
				tx.commit();
	        }catch (Exception e) {
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
    }
    

    /**
     * 修改机构对象
	 * @param organ
	 * @throws Exception
     */
    public void update(Organ organ) throws Exception{
        
    	if(organ != null && organ != Organ.ROOT_ORGAN){
	        Session session = HibernateUtil.getInstance().openSession();
	        Transaction tx = null;
	        try {
	            tx = session.beginTransaction();
	            session.update(organ);
	            tx.commit();
	        }catch (Exception e) {
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
    } 
    

    /**
     * 根据ID获得机构实体
     * @param organID 机构ID
     * @return 机构实体
     */
    public Organ getOrganByID(String organID){
    	Organ organ = null;
        
        if(organID != null){
        	if(Organ.ROOT_ORGAN.getOrganID().equals(organID)){
        		organ = Organ.ROOT_ORGAN;
        	}else{
		        Session session = HibernateUtil.getInstance().openSession();
		        try {
		          Criteria criteria = session.createCriteria(Organ.class);
		          criteria.add(Restrictions.eq("organID",organID));
		          organ = (Organ)criteria.uniqueResult();
		        }catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		          // No matter what, close the session
		          session.close();
		        }
        	}
        }
        
        return organ;
    }   
 
    /**
     * 根据organ对象获得父机构实体

     * @param organ organ对象
     * @return 父机构实体

     */
    public Organ getParentOrgan(Organ organ){
    	Organ parentOrgan = null;
        
        if(organ != null){
        	parentOrgan = getParentOrgan(organ.getOrganID());
        }
        
        return parentOrgan;
    }
    
    /**
     * 根据organID获得父机构实体

     * @param organID 
     * @return 父机构实体

     */
    public Organ getParentOrgan(String organID){
    	Organ parentOrgan = null;
        
        if(organID != null){
        	String parentOrganID = Organ.getParentOrganID(organID);
        	parentOrgan = getOrganByID(parentOrganID);
         }
        
        return parentOrgan;
    }
    
    /**
     * 返回当前组织的子机构
     * @param parentOrganID 当前组织的ID
     * @param allChildren 当allChildren = false ,仅返回当前机构的直接子机构 ; 当allChildren = true ,则返回当前机构的所有子孙机构

     * @return
     */
    public List findChildOrgans(String parentOrganID , boolean allChildren){
        List resultList = null;
        if(parentOrganID != null){
	        Session session = HibernateUtil.getInstance().openSession();
	        try {
	          Criteria criteria = session.createCriteria(Organ.class);
	          if(Organ.ROOT_ORGAN.getOrganID().equals(parentOrganID)){
	        	  if(allChildren){
	        		  // Do Nothing 
	        	  }else{
	        		  criteria.add(Restrictions.not(Restrictions.like("organID" , "-" , MatchMode.ANYWHERE)));
	        	  }
	          }else{
	        	  if(allChildren){
	        		  criteria.add(Restrictions.like("organID" , parentOrganID + "-" , MatchMode.START));
	        	  }else{
	        		  criteria.add(Restrictions.like("organID" , parentOrganID + "-____"));
	        	  }
	          }
	          criteria.addOrder(Order.asc("organLevel"));
	          criteria.addOrder(Order.asc("organSort"));
	          resultList = criteria.list();
	        }catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	          // No matter what, close the session
	          session.close();
	        }
        }
        
        return resultList;
    }
    
    /**
     * 返回直接子机构的数量
     * @param parentOrganID 父机构ID
     * @return 子机构数
     */
    public int getChildrenCount(String parentOrganID){
    	int childrenCount = 0;
    	if(parentOrganID != null){
	        Session session = HibernateUtil.getInstance().openSession();
	        try {
	        	if(parentOrganID.equals(Organ.ROOT_ORGAN.getOrganID())){
		        	Query query = session.createQuery("select max(organSort) from Organ where organLevel=1");
		        	Integer max = (Integer)query.uniqueResult();
		        	if(max == null ){
		        		childrenCount = 0;
		        	}else{
		        		childrenCount = max.intValue();
		        	}
	        	}else{
		        	Query query = session.createQuery("select max(organSort) from Organ where organID like :organID");
		        	query.setString("organID" , parentOrganID + "-____");
		        	Integer max = (Integer)query.uniqueResult();
		        	if(max == null ){
		        		childrenCount = 0;
		        	}else{
		        		childrenCount = max.intValue();
		        	}
	        	}
	        }catch (Exception e) {
	        	e.printStackTrace();
	        } finally {
	          // No matter what, close the session
	          session.close();
	        }
    	}
    	return childrenCount;
    }
    /**
     * 删除组织信息
     * @param organ
     * @throws Exception
     */
    public void deleteOrgan(Organ organ) throws Exception {
    	if(organ != null && organ != Organ.ROOT_ORGAN){
	        Session session = HibernateUtil.getInstance().openSession();
	        Transaction tx = null; 
	        
	        try {
	            tx = session.beginTransaction();
	            session.delete(organ);
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
    }
    

}
