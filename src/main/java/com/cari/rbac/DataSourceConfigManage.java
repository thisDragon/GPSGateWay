package com.cari.rbac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.hibernate.type.StandardBasicTypes;

import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.CommQueryCondition;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;
import com.cari.web.exception.SessionNotOpenException;

/**
* @ClassName: DataSourceManage
* @Description: 数据源模块
* @author yangjianlong
* @date 2020年2月10日下午2:42:54
*
 */
public class DataSourceConfigManage extends BaseOperation{

	private static DataSourceConfigManage dataSourceConfigManage;
	public static final String GPSDATASOURCE_TABLE_PREFIX = "T_GPSDATASOURCE_";
	public static final String GPSDATASOURCE_TABLE_DELETE_PREFIX = "DELETE_T_GPSDATASOURCE_";
	
	private DataSourceConfigManage() {
		super();
	}
	
	public static DataSourceConfigManage getInstance() {
		if (dataSourceConfigManage == null) {
			dataSourceConfigManage = new DataSourceConfigManage();
		}
		return dataSourceConfigManage;
	}
	
	/**
	 * @Title: renameTable   
	 * @Description: 重命名表
	 * @param tableName
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月6日 上午11:19:05
	 */
	public void renameTable(String tableName) throws Exception {
		Session session = HibernateUtil.getInstance().openSession();
		String oldTableName = GPSDATASOURCE_TABLE_PREFIX+tableName;
		String newTableName = GPSDATASOURCE_TABLE_DELETE_PREFIX+tableName;
		if(!isTableExist(oldTableName)) return;
		String sql = "exec sp_rename '%s','%s'";
		String newSql = String.format(sql, oldTableName, newTableName);
		try {
			session.doWork(
	          new Work() {
				public void execute(Connection connection) throws SQLException {  
	            	PreparedStatement preparedStatement = null;
	            	try {
	            		  connection.setAutoCommit(false);
						  preparedStatement = connection.prepareCall(newSql);
		                  preparedStatement.execute();
		                  connection.commit();
		                  
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						if (preparedStatement != null) {
							preparedStatement.close();
						}
					}
	              }  
	            }
	          );
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * @Title: createTable   
	 * @Description: 创建表
	 * @param tableName
	 * @throws Exception 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年3月6日 上午11:17:18
	 */
	public void createTable(String tableName) throws Exception {
		Session session = HibernateUtil.getInstance().openSession();
		tableName = GPSDATASOURCE_TABLE_PREFIX+tableName;
		if(isTableExist(tableName)) return;
		
		StringBuilder sbTable = new StringBuilder();
		sbTable.append(" CREATE TABLE %s ");
		sbTable.append(" ( ");
		sbTable.append(" 	 id varchar(50) NOT NULL , ");
		sbTable.append(" 	 deviceId varchar(50) NULL , ");
		sbTable.append(" 	 sourceType varchar(30) NULL , ");
		sbTable.append(" 	 lon float(53) NULL ,");
		sbTable.append(" 	 lat float(53) NULL , ");
		sbTable.append(" 	 position varchar(1000) NULL , ");
		sbTable.append(" 	 token varchar(50) NULL , ");
		sbTable.append(" 	 createTime datetime NULL , ");
		sbTable.append(" 	 remark varchar(1000) NULL , ");
		sbTable.append(" 	 speed varchar(20) NULL , ");
		sbTable.append(" 	 gpsTime datetime NULL  ");
		sbTable.append(" ) ");
		
		StringBuilder sbIndex = new StringBuilder();
		sbIndex.append(" CREATE INDEX %s ON %s ");
		sbIndex.append(" ( ");
		sbIndex.append(" 	sourceType ASC, gpsTime ASC, deviceId ASC ");
		sbIndex.append(" ) ");
		
		String sqlTable = String.format(sbTable.toString(),tableName);
		String sqlIndex = String.format(sbIndex.toString(),tableName+ "_INDEX",tableName);
		try {
			session.doWork(
	          new Work() {
	              @SuppressWarnings("resource")
				public void execute(Connection connection) throws SQLException {  
	            	  PreparedStatement preparedStatement = null;
	            	  try {
						  connection.setAutoCommit(false);
						  preparedStatement = connection.prepareCall(sqlTable);
		                  preparedStatement.execute();
		                  preparedStatement = connection.prepareCall(sqlIndex);
		                  preparedStatement.execute();
		                  connection.commit();
		                  
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						if (preparedStatement != null) {
							preparedStatement.close();
						}
					}
	              }  
	            }
	          );
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private boolean isTableExist(String tableName) throws SQLException {
		return (getTableNameNumber(tableName) > 0);
	}
	
	private long getTableNameNumber(String tableName) {
		int size = 1;
		String sql = "SELECT name FROM sysobjects WHERE name= '"+tableName+"'";
		Session session = HibernateUtil.getInstance().openSession();
		try {
			size = session.createSQLQuery(sql).addScalar("name",StandardBasicTypes.STRING).list().size();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (session != null) {
				session.close();
			}
		}
		
		return size;
	}
	
	/**
	 * @Title: getDataSourceConfigByKey   
	 * @Description: 获取配置信息
	 * @param id
	 * @return 
	 * DataSourceConfig      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月18日 上午10:12:07
	 */
	public DataSourceConfig getDataSourceConfigByKey(String configId){
		Session session = HibernateUtil.getInstance().openSession();
		DataSourceConfig dataSourceConfig = null;
		try {
			dataSourceConfig = (DataSourceConfig) session.createQuery("select m from com.cari.rbac.DataSourceConfig m where id=:id ")
					.setString("id", configId)
					.uniqueResult();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (session != null) {
				session.close();
			}
		}
		return dataSourceConfig;
    }
	
	public boolean isExist(String id,String sourceName, String sourceType){
		Session session = HibernateUtil.getInstance().openSession();
		DataSourceConfig m = null;
		try {
			if (id == null) {
				m = (DataSourceConfig) session.createQuery("select m from com.cari.rbac.DataSourceConfig m where sourceName=:sourceName or sourceType=:sourceType ")
						.setString("sourceName", sourceName)
						.setString("sourceType", sourceType)
						.uniqueResult();
			}else{
				m = (DataSourceConfig) session.createQuery("select m from com.cari.rbac.DataSourceConfig m where (sourceName=:sourceName or sourceType=:sourceType) and id!=:id ")
						.setString("sourceName", sourceName)
						.setString("sourceType", sourceType)
						.setString("id", id)
						.uniqueResult();
			}
		}catch (Exception e) {
		
		}finally{
			if (session != null) {
				session.close();
			}
		}
		
		if (m != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Title: saveDataSourceConfig   
	 * @Description: 保存数据源配置
	 * @param dataSourceConfig
	 * @throws BeRefuseException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月10日 下午3:01:40
	 */
	public void save(DataSourceConfig dataSourceConfig) throws BeRefuseException {		
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			session.save(dataSourceConfig);
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
	
	/**
	 * @Title: getAllList   
	 * @Description: 获取全部的数据源配置
	 * @return
	 * @throws SessionNotOpenException 
	 * List<DataSourceConfig>      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月13日 上午11:46:31
	 */
	@SuppressWarnings("unchecked")
	public List<DataSourceConfig> getAllList() throws SessionNotOpenException{
		Session session = HibernateUtil.getInstance().openSession();
		String hql = "select m from com.cari.rbac.DataSourceConfig m ";
		List<DataSourceConfig> configs = new ArrayList<DataSourceConfig>();
		try {
			Query query = session.createQuery(hql);
			configs = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return configs;
	}
	
	/**
	 * @Title: getList   
	 * @Description: 获取资源配置列表
	 * @param pageNo
	 * @param pageCount
	 * @param sourceName
	 * @param token
	 * @return 
	 * ListPage      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月11日 上午9:57:51
	 */
	public ListPage getList(int pageNo, int pageCount, String sourceName, String token) {
		StringBuffer bHql = new StringBuffer();		
		StringBuffer cHql = new StringBuffer();
		HashMap<String, Object> cond = new HashMap<String, Object>();
		bHql.append("select m from com.cari.rbac.DataSourceConfig m where 1=1 ");
		cHql.append("select count(m) from com.cari.rbac.DataSourceConfig m where 1=1 ");
		if (sourceName != null && !sourceName.equals("")) {
			bHql.append(" and m.sourceName like:sourceName ");
			cHql.append(" and m.sourceName like:sourceName ");
			cond.put("sourceName", "%" + sourceName + "%");
		}
		if (token != null && !token.equals("")) {
			bHql.append(" and m.token like:token ");
			cHql.append(" and m.token like:token ");
			cond.put("token", "%" + token + "%");
		}
		bHql.append(" order by  m.createTime desc");
		CommQueryCondition mqc = new CommQueryCondition(cHql.toString(), bHql.toString(), cond);
        return queryPage(pageNo, pageCount, mqc);
	}
	
	/**
	 * @Title: update   
	 * @Description: 修改  
	 * @param id
	 * @param name
	 * @param moduleSort
	 * @param desc
	 * @throws BeRefuseException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月11日 上午10:00:20
	 */
	public void update(DataSourceConfig dataSourceConfig) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {			
			DataSourceConfig m = (DataSourceConfig) session.createQuery("select m from com.cari.rbac.DataSourceConfig m where id=:id")
								.setString("id", dataSourceConfig.getId())
								.uniqueResult();
			if (m != null) {
				tr = session.beginTransaction();
				session.createQuery("update com.cari.rbac.DataSourceConfig set sourceName=:sourceName,sourceType=:sourceType," +
						"token=:token,timeSpan=:timeSpan,remark=:remark,modifyUser=:modifyUser,modifyTime=:modifyTime where id=:id")
						.setString("sourceName", dataSourceConfig.getSourceName())
						.setString("sourceType", dataSourceConfig.getSourceType())
						.setString("token", dataSourceConfig.getToken())
						.setInteger("timeSpan", dataSourceConfig.getTimeSpan())
						.setString("remark", dataSourceConfig.getRemark())
						.setString("modifyUser", dataSourceConfig.getModifyUser())
						.setTimestamp("modifyTime", dataSourceConfig.getModifyTime())
						.setString("id", dataSourceConfig.getId())
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
	
	public void updateUserFlag(String id) throws BeRefuseException{
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {			
			DataSourceConfig m = (DataSourceConfig) session.createQuery("select m from com.cari.rbac.DataSourceConfig m where id=:id")
								.setString("id", id)
								.uniqueResult();
			if (m != null) {
				tr = session.beginTransaction();
				Integer userFlag = m.getUserFlag() == 0 ? 1 : 0;
				session.createQuery("update com.cari.rbac.DataSourceConfig set userFlag=:userFlag where id=:id")
						.setInteger("userFlag", userFlag)
						.setString("id", id)
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
	
	/**
	 * @Title: delete   
	 * @Description: 批量删除配置
	 * @param ids
	 * @throws BeRefuseException 
	 * void      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月11日 上午10:42:11
	 */
	public void delete(String[] ids) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();

			session.createQuery("delete from com.cari.rbac.DataSourceConfig where id in(:id)")
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
	
}
