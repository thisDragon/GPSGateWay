package com.cari.rbac;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.analog.data.util.DateUtils;
import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.CommQueryCondition;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.exception.SessionNotOpenException;

import jodd.util.StringUtil;

/**
* @ClassName: LogDataManage
* @Description: 数据日志
* @author yangjianlong
* @date 2020年2月12日上午11:19:20
*
 */
public class LogDataManage extends BaseOperation{

private static LogDataManage logDataManage;
	
	private LogDataManage() {
		super();
	}
	
	public static LogDataManage getInstance() {
		if (logDataManage == null) {
			logDataManage = new LogDataManage();
		}
		return logDataManage;
	}
	
	/**
	 * @Title: save   
	 * @Description: 添加日志记录
	 * @param logData
	 * @throws BeRefuseException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月17日 上午10:16:42
	 */
	public void save(LogData logData) throws BeRefuseException {		
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			session.save(logData);
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
	
	@SuppressWarnings("unchecked")
	public List<LogData> getAllList(Integer logType, String sourceType, Integer state, String startTime, String endTime) throws SessionNotOpenException{
		Session session = HibernateUtil.getInstance().openSession();
		StringBuilder bHql = new StringBuilder();
		bHql.append("select m from com.cari.rbac.LogData m ");
		List<LogData> logDatas = new ArrayList<LogData>();
		try {
			if (logType != null) {
				bHql.append(" and m.logType =:logType ");
			}
			if (state != null) {
				bHql.append(" and m.state =:state ");
			}
			if (StringUtil.isNotEmpty(sourceType)) {
				bHql.append(" and m.sourceType like:sourceType ");
			}
			if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)) {
				Date startDate = DateUtils.str2Date(startTime, DateUtils.NORMAL_FORMAT2);
				Date endDate = DateUtils.str2Date(endTime, DateUtils.NORMAL_FORMAT2);
				if (endDate.getTime()>=startDate.getTime()) {
					bHql.append(" and m.createTime >:startTime ");
					bHql.append(" and m.createTime <:endTime ");
				}
			}
			Query query = session.createQuery(bHql.toString());
			
			if (logType != null) {
				query.setInteger("logType", logType);
			}
			if (state != null) {
				bHql.append(" and m.state =:state ");
				query.setInteger("state", state);
			}
			if (StringUtil.isNotEmpty(sourceType)) {
				bHql.append(" and m.sourceType like:sourceType ");
				query.setString("sourceType", "%" + sourceType + "%");
			}
			if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)) {
				Date startDate = DateUtils.str2Date(startTime, DateUtils.NORMAL_FORMAT2);
				Date endDate = DateUtils.str2Date(endTime, DateUtils.NORMAL_FORMAT2);
				if (endDate.getTime()>=startDate.getTime()) {
					bHql.append(" and m.createTime >:startTime ");
					bHql.append(" and m.createTime <:endTime ");
					query.setDate("startTime", startDate);
					query.setDate("endTime", endDate);
				}
			}
			
			logDatas = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return logDatas;
	}
	
	public ListPage getList(int pageNo, int pageCount, Integer logType, String sourceType, Integer state, String startTime, String endTime) {
		StringBuffer bHql = new StringBuffer();		
		StringBuffer cHql = new StringBuffer();
		HashMap<String, Object> cond = new HashMap<String, Object>();
		bHql.append("select m from com.cari.rbac.LogData m where 1=1 ");
		cHql.append("select count(m) from com.cari.rbac.LogData m where 1=1 ");
		
		if (logType != null) {
			bHql.append(" and m.logType =:logType ");
			cHql.append(" and m.logType =:logType ");
			cond.put("logType", logType);
		}
		
		if (state != null) {
			bHql.append(" and m.state =:state ");
			cHql.append(" and m.state =:state ");
			cond.put("state", state);
		}
		
		if (StringUtil.isNotEmpty(sourceType)) {
			bHql.append(" and m.sourceType like:sourceType ");
			cHql.append(" and m.sourceType like:sourceType ");
			cond.put("sourceType", "%" + sourceType + "%");
		}
		
		if (StringUtil.isNotEmpty(startTime) && StringUtil.isNotEmpty(endTime)) {
			Date startDate = DateUtils.str2Date(startTime, DateUtils.NORMAL_FORMAT2);
			Date endDate = DateUtils.str2Date(endTime, DateUtils.NORMAL_FORMAT2);
			if (endDate.getTime()>=startDate.getTime()) {
				bHql.append(" and m.createTime >:startTime ");
				cHql.append(" and m.createTime >:startTime ");
				cond.put("startTime", startDate);
				
				bHql.append(" and m.createTime <:endTime ");
				cHql.append(" and m.createTime <:endTime ");
				cond.put("endTime", endDate);
			}
		}
		
		bHql.append(" order by  m.createTime desc");
		CommQueryCondition mqc = new CommQueryCondition(cHql.toString(), bHql.toString(), cond);
        return queryPage(pageNo, pageCount, mqc);
	}
}
