package com.cari.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cari.rbac.Operation;
import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.CommQueryCondition;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;

/**
 * 
 * @author fuqm
 *
 */
public class OperationManage extends BaseOperation {
	
	public static List factorialPush;
	
	public static List factorialPop;
	
	private static OperationManage om;
	
	static {
		setFactorialMap();
	}
	
	private OperationManage() {
		super();
	}
	
	public static OperationManage getInstance() {
		if (om == null) {
			om = new OperationManage();
		}
		return om;
	}
	
	public List getOperateList() {
		List list = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			list = getOperateList(session);
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public List getOperateList(Session session) {
		return session.createQuery("select o from com.cari.rbac.Operation o order by operate_value").list();		
	}
	
	public List getOperationByModuleId(String moduleID) {
		List list = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			list = session.createSQLQuery("select * from SYS_RBAC_MOD2OP where MODULE_ID=:id order by ORDERNO")
					.setString("id", moduleID)
					.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public ListPage getOperation(int pageNo, int pageCount, String moduleName) {
		StringBuffer bHql = new StringBuffer();		
		StringBuffer cHql = new StringBuffer();
		HashMap cond = new HashMap();
		bHql.append("select o from com.cari.rbac.Operation o where 1=1 ");
		cHql.append("select count(o) from com.cari.rbac.Operation o where 1=1 ");		
		if (moduleName != null && !moduleName.equals("")) {
			bHql.append(" and o.operate_name like:operate_name ");
			cHql.append(" and o.operate_name like:operate_name ");
			cond.put("operate_name", "%" + moduleName + "%");
		}
		bHql.append(" order by o.operate_value ");
		CommQueryCondition mqc = new CommQueryCondition(cHql.toString(), bHql.toString(), cond);
        return queryPage(pageNo, pageCount, mqc);		
	}
	
	public void saveOperation(Operation o) throws BeRefuseException{
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			if (checkOperationPK(o.getOperate_id(), session)) {
				throw new BeRefuseException("主键重复！");
			} else if (checkOperationValue(o.getOperate_value(), session)) {
				throw new BeRefuseException("操作值重复！");
			}
			Object[] obj = getMaxValueAndCount(session);
			long value = Long.valueOf((((Integer)obj[0]).intValue())).longValue() * 2;
			int count = ((Long)obj[1]).intValue();
			if (count >= 31) {
				throw new BeRefuseException("操作值超过最大值！");
			} else if ((value > Integer.MAX_VALUE && count < 31)) {
				List list = getOperationValue(session);
				Integer o_value = null;
				setFactorialMap();				
//				System.out.println("list.size = " + list.size() + "\t" 
//						+ "pop.size = " + factorialPop.size() + "\t" 
//						+ "pup.size = " + factorialPush.size());
				for (Iterator it = list.iterator(); it.hasNext();) {
					o_value = (Integer) it.next();								
					if (factorialPop.contains(o_value)) {
						factorialPop.remove(o_value);												
					}
				}
				for (Iterator it = factorialPop.iterator(); it.hasNext();) {
					o_value = (Integer) it.next();
					value = o_value.intValue();
					break;
				}
				factorialPop.clear();
			}			
			tr = session.beginTransaction();	
			o.setOperate_value(Long.valueOf(value).intValue());
			session.save(o);
			tr.commit();
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
	
	public void delOperation(String[] id) throws BeRefuseException {		
		if (id != null && id.length > 0) {
			Session session = HibernateUtil.getInstance().openSession();
			Transaction tr = null;
			try {
				tr = session.beginTransaction();
				session.createQuery("delete com.cari.rbac.Operation where operate_id in(:operate_id)")
						.setParameterList("operate_id", id)
						.executeUpdate();
				session.createSQLQuery("delete sys_rbac_mod2op where operator_id in (:operate_id)")
						.setParameterList("operate_id", id)
						.executeUpdate();
				tr.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
				throw new BeRefuseException("删除失败！");
			} finally {
				session.close();
			}
		}
	}
	
	public void updateOperation(String name, String id) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			session.createQuery("update com.cari.rbac.Operation set operate_name=:name where operate_id=:id")
					.setString("name", name)
					.setString("id", id)
					.executeUpdate();
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new BeRefuseException("更新失败！");
		} finally {
			session.close();
		}
	}
	
	public boolean checkOperationPK(String dwKey, Session session) {
		return (((Long)session.createQuery("select count(o) from com.cari.rbac.Operation o where o.operate_id=:operate_id")
				.setString("operate_id", dwKey)
				.uniqueResult()).intValue() > 0) ? true : false;
	}
	
	public boolean checkOperationValue(int value, Session session) {
		return (((Long)session.createQuery("select count(o) from com.cari.rbac.Operation o where o.operate_value=:value")
				.setInteger("value", value)
				.uniqueResult()).intValue() > 0) ? true : false;
	}
	
	public Object[] getMaxValueAndCount(Session session) {
		return (Object[])session.createQuery("select max(o.operate_value),count(o) from com.cari.rbac.Operation o")				
				.uniqueResult();
	}
	
	public List getOperationValue(Session session) {
		return session.createQuery("select o.operate_value from com.cari.rbac.Operation o ").list();
	}
	
	public static List setFactorialMap() {
		if (factorialPush == null) {			
			factorialPush = new ArrayList(31);
			factorialPop = new ArrayList(31);
			for (int i = 0; i < 31; i++) {	
				factorialPush.add(Integer.valueOf(getFactorial(i)));
			}
			factorialPop.addAll(factorialPush);
		} else {			
			factorialPop.addAll(factorialPush);
		}
		return factorialPush;	
	}
	
	public static int getFactorial(int index) {
		if (index - 1 >= 0) {
			//乘以2
			return getFactorial(index - 1) << 1;
		} else {
			return 1;
		}
	}
	
	public static void main(String[] args) {		
		System.out.println(OperationManage.getFactorial(3));
	}
}
