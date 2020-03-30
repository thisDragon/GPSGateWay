package com.cari.rbac;

import java.util.HashMap;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.CommQueryCondition;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.exception.SessionNotOpenException;
import jodd.util.StringUtil;

/**
* @ClassName: DataSourceForwardManage
* @Description: 转发订阅
* @author yangjianlong
* @date 2020年2月11日下午4:06:48
*
 */
public class DataSourceForwardManage extends BaseOperation{

private static DataSourceForwardManage dataSourceForwardManage;
	
	private DataSourceForwardManage() {
		super();
	}
	
	public static DataSourceForwardManage getInstance() {
		if (dataSourceForwardManage == null) {
			dataSourceForwardManage = new DataSourceForwardManage();
		}
		return dataSourceForwardManage;
	}
	
	/**
	 * @Title: save   
	 * @Description: 保存
	 * @param dataSourceForward
	 * @throws BeRefuseException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月13日 下午2:59:25
	 */
	public void save(DataSourceForward dataSourceForward) throws BeRefuseException {		
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			session.save(dataSourceForward);
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tr != null) {
				tr.rollback();
			}
			throw new BeRefuseException("保存失败");
		} finally {
			session.close();
		}
	}
	
	public void update(DataSourceForward dataSourceForward) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {			
			DataSourceForward m = (DataSourceForward) session.createQuery("select m from com.cari.rbac.DataSourceForward m where id=:id")
								.setString("id", dataSourceForward.getId())
								.uniqueResult();
			if (m != null) {
				tr = session.beginTransaction();
				session.createQuery("update com.cari.rbac.DataSourceForward set subscriptionName=:subscriptionName,forwardUrl=:forwardUrl," +
						"sourceType=:sourceType,remark=:remark,account=:account,password=:password,isEnable=:isEnable,modifyUser=:modifyUser,modifyTime=:modifyTime where id=:id")
						.setString("subscriptionName", dataSourceForward.getSubscriptionName())
						.setString("forwardUrl", dataSourceForward.getForwardUrl())
						.setString("sourceType", dataSourceForward.getSourceType())
						.setString("remark", dataSourceForward.getRemark())
						.setString("account", dataSourceForward.getAccount())
						.setString("password", dataSourceForward.getPassword())
						.setInteger("isEnable", dataSourceForward.getIsEnable())
						.setString("modifyUser", dataSourceForward.getModifyUser())
						.setTimestamp("modifyTime", dataSourceForward.getModifyTime())
						.setString("id", dataSourceForward.getId())
						.executeUpdate();						
				tr.commit();
			} else {
				throw new BeRefuseException("修改的配置已不存在！");
			}			
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tr != null) {
				tr.rollback();
			}
		} catch (BeRefuseException e) {
			throw e;
		} finally {
			session.close();
		}
	}
	
	public void delete(String[] ids) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();

			session.createQuery("delete from com.cari.rbac.DataSourceForward where id in(:id)")
			.setParameterList("id", ids)
			.executeUpdate();
			
			tr.commit();
		} catch (HibernateException e) {
			if (tr != null) {
				tr.rollback();
			}
			e.printStackTrace();
			throw new BeRefuseException("删除失败！");
		} finally {
			session.close();
			session = null;
		}
	}
	
	public boolean isExist(String id,String subscriptionName){
		Session session = HibernateUtil.getInstance().openSession();
		DataSourceForward m;
		if (id == null) {
			m = (DataSourceForward) session.createQuery("select m from com.cari.rbac.DataSourceForward m where subscriptionName=:subscriptionName  ")
					.setString("subscriptionName", subscriptionName)
					.uniqueResult();
		}else{
			m = (DataSourceForward) session.createQuery("select m from com.cari.rbac.DataSourceForward m where subscriptionName=:subscriptionName and id!=:id ")
					.setString("subscriptionName", subscriptionName)
					.setString("id", id)
					.uniqueResult();
		}
		
		if (m != null) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public ListPage getList(int pageNo, int pageCount, String sourceName, String subscriptionName) {
		StringBuffer bHql = new StringBuffer();
		StringBuffer cHql = new StringBuffer();
		HashMap<String, Object> cond = new HashMap<String, Object>();
		bHql.append("select m from com.cari.rbac.DataSourceForward m where 1=1 ");
		cHql.append("select count(m) from com.cari.rbac.DataSourceForward m where 1=1 ");
		
		if (StringUtil.isNotEmpty(sourceName)) {
			Session session = null;
			try {
				session = HibernateUtil.getInstance().openSession();
				String hql = "select m from com.cari.rbac.DataSourceConfig m where sourceName=:sourceName ";
				HashMap<String, Object> conditions = new HashMap<String, Object>();
				conditions.put("sourceName", sourceName);
				List<DataSourceConfig> configs = executeQuery(hql, conditions, session);
				
				if (configs.size() == 1) {
					bHql.append(" and m.sourceType like:sourceType ");
					cHql.append(" and m.sourceType like:sourceType ");
					cond.put("sourceType", "%$" + configs.get(0).getSourceType() + "$%");
				}else if(configs.size() > 1){
					for (int i = 0; i < configs.size(); i++) {
						if (i == 0) {
							bHql.append(" and ( m.sourceType like:sourceType"+i+" ");
							cHql.append(" and ( m.sourceType like:sourceType"+i+" ");
							cond.put("sourceType"+i, "%$" + configs.get(0).getSourceType() + "$%");
						}else if(i == configs.size()-1){
							bHql.append(" or m.sourceType like:sourceType"+i+" ) ");
							cHql.append(" or m.sourceType like:sourceType"+i+" ) ");
							cond.put("sourceType"+i, "%$" + configs.get(i).getSourceType() + "$%");
						}else{
							bHql.append(" or m.sourceType like:sourceType"+i+" ");
							cHql.append(" or m.sourceType like:sourceType"+i+" ");
							cond.put("sourceType"+i, "%$" + configs.get(i).getSourceType() + "$%");
						}
					}
				}
				
			} catch (SessionNotOpenException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null) {
					session.close();
				}
			}
		}
		
		if (StringUtil.isNotEmpty(subscriptionName)) {
			bHql.append(" and m.subscriptionName like:subscriptionName ");
			cHql.append(" and m.subscriptionName like:subscriptionName ");
			cond.put("subscriptionName", "%" + subscriptionName + "%");
		}
		
		bHql.append(" order by  m.createTime desc");
		CommQueryCondition mqc = new CommQueryCondition(cHql.toString(), bHql.toString(), cond);
        return queryPage(pageNo, pageCount, mqc);
	}
}
