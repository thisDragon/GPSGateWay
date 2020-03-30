package com.cari.sys.biz;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sys.bean.Region;

public class RegionManage extends BaseOperation {

	private static RegionManage rm;
	
	private RegionManage() {
		super();
	}

	public static RegionManage getInstance() {
		if (rm == null) {
			rm = new RegionManage();
		}
		return rm;
	}
	public boolean save(Region region) {
		boolean saveOK = false;
		if (region != null) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				session.save(region);
				tx.commit();
				saveOK = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null) {
					tx.rollback();
				}
			} finally {
				session.close();
			}
		}
		return saveOK;
	}

	public boolean update(Region region) {
		boolean updateOK = false;
		if (region != null) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tx = null;
			try {				
				tx = session.beginTransaction();
				session.update(region);
				tx.commit();
				updateOK = true;
			} catch (Exception e) {
				e.printStackTrace();
				if (tx != null) {
					tx.rollback();
				}
			} finally {
				session.close();
			}
		}
		return updateOK;
	}

	public boolean delete(Region region){
		boolean delOK = false;
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tx = null;
        try {
        	tx = session.beginTransaction();
        	session.delete(region);
            tx.commit();
            delOK = true;
        } catch (Exception e) {
        	if (tx != null) {
        		tx.rollback();
        	}
        } finally {
            session.close();
        }
		return delOK;
	}
	
	public Region getRegionById(String dwkey) {
		Region region = null;
		Session session = HibernateUtil.getInstance().openSession();
		
		if(dwkey.equals(Region.REGION_ROOT.getDwKey())){
    		region = Region.REGION_ROOT;
    	}else{
			try {
				region = (Region)session.get(Region.class,dwkey);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
    	}
		return region;
	}
	
	public Region getRegionByCityCode(String cityCode){
		Region region = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			Query query = session.createQuery("from Region where cityCode=:cityCode");
			query.setString("cityCode", cityCode);
			region = (Region)query.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}
		return region;
	}
	
	public Region getRegionByFullName(String fullName) {
		Region region = new Region();
        if(fullName != null){
        	if(fullName.equals(Region.REGION_ROOT.getFullName())){
        		return Region.REGION_ROOT;
        	}
        	region.setFullName(fullName);
 	        Session session = HibernateUtil.getInstance().openSession();
		    try {
		          Query query = null;
		          if(region.is_Province()){
		        	  query = session.createQuery("from Region where city is null and county is null and province =:province");
		        	  query.setString("province" , region.getProvince());
		          }else if(region.is_City()){
		        	  query = session.createQuery("from Region where county is null and city =:city");
		        	  query.setString("city" , region.getCity());
		          }else if(region.is_County()){
		        	  query = session.createQuery("from Region where county =:county");
		        	  query.setString("county" , region.getCounty());
		          }
		          region = (Region)query.uniqueResult();
	        }catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	          // No matter what, close the session
	          session.close();
	        }
        }
        return region;
	}
	
	public List getAllRegions() {
		List resList = new ArrayList();
		Session session = HibernateUtil.getInstance().openSession();
        try {
            Query query = session.createQuery("from Region as region order by region.orderNo");
            resList = query.list();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
		return resList;
	}
	
	public List getAllActiveRegions() {
		List resList = new ArrayList();
		Session session = HibernateUtil.getInstance().openSession();
        try {
            Query query = session.createQuery("from Region as region where region.active='Y' order by region.orderNo");
            resList = query.list();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
		return resList;
	}
	
	/**
     * 获取子节点数目

     * @return count
     */
    public long getChildCount(Region region){
    	long count = 0;
    	Session	session = HibernateUtil.getInstance().openSession();
        try {
          Query query = null;
          if(region == Region.REGION_ROOT){
        	  query = session.createQuery("select count(*) from Region where city is null and county is null");
          }else if(region.is_Province()){
        	  query = session.createQuery("select count(*) from Region where city is not null and county is null and province =:province");
        	  query.setString("province" , region.getProvince());
          }else if(region.is_City()){
        	  query = session.createQuery("select count(*) from Region where city =:city and county is not null");
        	  query.setString("city" , region.getCity());
          }else if(region.is_County()){
        	  return -1;
          }
          count = ((Long)query.uniqueResult()).longValue();
        }catch (Exception e) {
        	 e.printStackTrace();
        } finally {
          // No matter what, close the session
    		session.close();
        }
        return count;
    }
	
}