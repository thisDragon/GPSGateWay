/*
 * 创建日期 May 20, 2006
 * 系统模块信息业务逻辑类
 * 林良益@caripower
 */
package com.cari.rbac;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.cari.sql.hibernate.BaseOperation;
import com.cari.sql.hibernate.CommQueryCondition;
import com.cari.sql.hibernate.HibernateUtil;
import com.cari.sql.hibernate.NHibernateUtil;
import com.cari.sys.biz.SystemConstant;
import com.cari.web.comm.ListPage;
import com.cari.web.exception.BeRefuseException;

public class ModuleManage extends BaseOperation{

	private static ModuleManage mm;
	
	public static ModuleManage getInstance() {
		if (mm == null) {
			mm = new ModuleManage();
		}
		return mm;
	}
	
	private ModuleManage() {
		super();
	}

    /**
     * 返回当前模块的子模块
     * @param moduleID 当前组织的ID

     * @return
     */
    public List findChildModules(String moduleID){
        List resultList = null;
        if(moduleID != null){
	        try {
	        	Session session = NHibernateUtil.openSession();
	        	Criteria criteria = session.createCriteria(Module.class);
	        	if(!Module.ROOT.getModuleID().equals(moduleID)){
	        		criteria.add(Restrictions.like("moduleID" , moduleID + "-" , MatchMode.START));
	        	}
	        	criteria.addOrder(Order.asc("moduleLevel"));
	        	criteria.addOrder(Order.asc("moduleSort"));
	        	resultList = criteria.list();
	        }catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	NHibernateUtil.closeSession();
	        }
        }
        
        return resultList;
    }
    
    /**
     * 获取带操作列表的权限对象
     * @param moduleID
     * @return
     */
    public Module loadModulesWithOperate(String moduleID){
    	Module theModule = null;
    	if(moduleID != null){
	        try {
	        	Session session = NHibernateUtil.openSession();
	        	theModule = (Module)session.load(Module.class , moduleID);
	        	if (theModule != null){
	        		theModule.getOperations().size();
	        	}
		    }catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		    	NHibernateUtil.closeSession();
		    }   		
    	}
    	return theModule;
    }

	/**
	 * 根据返回的值获取模块名称
	 */
	public static String getModuleName(String moduleAct,String subAct){
		String moduleName=null;
		String allAct=moduleAct+subAct;
		Module tempModule=null;
		if(!moduleAct.equals("") && !subAct.equals("")){
			try{
				Session session = NHibernateUtil.openSession();
				Criteria criteria = session.createCriteria(Module.class);
            	criteria.add(Restrictions.eq("moduleID",allAct));
            	tempModule = (Module)criteria.uniqueResult();
            	moduleName=tempModule.getModuleName();
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				NHibernateUtil.closeSession();
			}
		}
		return moduleName;
	}

	public Module getModuleById(String moduleID, Session session) throws Exception {
		if (moduleID == null || moduleID.equals("")) {
			return null;
		}
		Module m = (Module)session.get(Module.class , moduleID);
		if (m == null) {
			Query query = session.createQuery("delete RoleModuleRight where module_id=:moduleID");
			query.setString("moduleID", moduleID);
			query.executeUpdate();
		}
		return m;
	}
	
	public ListPage getModuleList(int pageNo, int pageCount, Integer moduleLevel, String moduleID, String moduleName) {
		StringBuffer bHql = new StringBuffer();		
		StringBuffer cHql = new StringBuffer();
		HashMap cond = new HashMap();
		bHql.append("select m from com.cari.rbac.Module m where 1=1 ");
		cHql.append("select count(m) from com.cari.rbac.Module m where 1=1 ");
		if (moduleLevel != null && moduleLevel.intValue() > 0) {
			bHql.append(" and m.moduleLevel =:moduleLevel ");
			cHql.append(" and m.moduleLevel =:moduleLevel ");
			cond.put("moduleLevel", moduleLevel);
		}
		if (moduleID != null && !moduleID.equals("")) {
			bHql.append(" and m.moduleID like:moduleID ");
			cHql.append(" and m.moduleID like:moduleID ");
			cond.put("moduleID", "%" + moduleID.toUpperCase() + "%");
		}
		if (moduleName != null && !moduleName.equals("")) {
			bHql.append(" and m.moduleName like:moduleName ");
			cHql.append(" and m.moduleName like:moduleName ");
			cond.put("moduleName", "%" + moduleName + "%");
		}
		bHql.append(" order by  m.moduleLevel, m.moduleSort");
		CommQueryCondition mqc = new CommQueryCondition(cHql.toString(), bHql.toString(), cond);
        return queryPage(pageNo, pageCount, mqc);
	}
	
	public static String MODULE_URL = "";
	
	public static String MODULE_ID = "";
		
	public void delModule(String[] ids) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			tr = session.beginTransaction();
			int level = 0;
			//一级
			List oneId = new ArrayList();
			//二级
			List twoId = new ArrayList();
			String id = "";
			int length = ids.length;
			for (int i = 0; i < length; i++) {
				id = ids[i];
				level = getLevel(id);
				if (level == 1) {
					oneId.add(id);
				} else {
					twoId.add(id);
				}
			}
			if (twoId.size() > 0) {
				session.createQuery("delete from com.cari.rbac.RoleModuleRight where module_id in(:id)")
						.setParameterList("id", twoId)
						.executeUpdate();
				session.createSQLQuery("delete from sys_rbac_mod2op where module_id in (:id)")
						.setParameterList("id", twoId)
						.executeUpdate();
			}
			for (Iterator it = oneId.iterator(); it.hasNext();) {
				id = (String) it.next();
				session.createQuery("delete from com.cari.rbac.RoleModuleRight where module_id like :id")
						.setString("id", id + "%")
						.executeUpdate();
				session.createQuery("delete from com.cari.rbac.Module where moduleID like :id")
						.setString("id", id + "%")
						.executeUpdate();
				session.createSQLQuery("delete from sys_rbac_mod2op where module_id like :id")
						.setString("id", id + "%")
						.executeUpdate();
			}
			session.createQuery("delete from com.cari.rbac.Module where moduleID in(:id)")
					.setParameterList("id", ids)
					.executeUpdate();
			oneId.clear();
			oneId = null;
			twoId.clear();
			twoId = null;
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
	
	public void saveModule(String id, String name, int moduleSort, String desc) throws BeRefuseException {		
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {
			checkModule(id, session);
			tr = session.beginTransaction();
//			int maxSort = getMaxSort(id, session);
			int level = getLevel(id);
			Module m = new Module();
			m.setModuleID(id);
			m.setModuleLevel(level);
//			m.setModuleSort(maxSort + 1);
			m.setModuleSort(moduleSort);
			m.setModuleDesc(desc);
			m.setModuleName(name);
			session.save(m);
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tr != null) {
				tr.rollback();
			}
			throw new BeRefuseException("保存失败");
		} catch (BeRefuseException e) {
			throw e;		
		} finally {
			session.close();
		}		
	}
	/**
	 * 修改模块名词，模块排序，模块的url ，模块key不修改
	 * @param id
	 * @param name
	 * @param moduleSort
	 * @param desc
	 */
	public void updateModule(String id, String name, int moduleSort, String desc) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		try {			
			Module m = (Module) session.createQuery("select m from com.cari.rbac.Module m where moduleID=:moduleID")
								.setString("moduleID", id)
								.uniqueResult();
			if (m != null) {
				tr = session.beginTransaction();
				session.createQuery("update com.cari.rbac.Module set moduleName=:moduleName,moduleSort=:moduleSort," +
						"moduleDesc=:moduleDesc where moduleID=:moduleID")
						.setString("moduleName", name)
						.setString("moduleDesc", desc)
						.setString("moduleID", id)
						.setInteger("moduleSort", moduleSort)
						.executeUpdate();						
				tr.commit();
			} else {
				throw new BeRefuseException("修改的模块已不存在！");
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
	 * 为模块添加操作
	 * @param id
	 * @param operationId
	 * @throws BeRefuseException
	 */
	public void addModuleOperation(String id, String[] operationId) throws BeRefuseException {
		Session session = HibernateUtil.getInstance().openSession();
		Transaction tr = null;
		boolean flag = false;
		if (operationId == null || operationId.length == 0) {
			flag = true;
		}
		try {
			tr = session.beginTransaction();
			session.createSQLQuery("delete from SYS_RBAC_MOD2OP where MODULE_ID=:id")
					.setString("id", id)
					.executeUpdate();
			if (!flag) {
				String oId = "";
				int index = 1;
				Operation o = null;
				for (int i = 0; i < operationId.length; i++) {
					oId = operationId[i];
					o = (Operation)session.createQuery("select o from com.cari.rbac.Operation o where o.operate_id=:operateId")
							.setString("operateId", oId)
							.uniqueResult();
					if (o == null) {
						throw new BeRefuseException("操作：" + oId + "\t已经被删除！");
					} else {
						o = null;
					}
					session.createSQLQuery("insert into SYS_RBAC_MOD2OP values(?,?,?)")
							.setString(0, id)
							.setString(1, oId)
							.setInteger(2, index++)
							.executeUpdate();
				}
				
			}
			tr.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tr != null) {
				tr.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	/**
	 * 将配置文件moduleurl.properties中的url地址插入到数据库中
	 * @param map
	 * @param session
	 */
	public void addModuleUrl(HashMap map, Session session) {
		if (map != null) {
			String key = "";
			String url = "";
			Transaction tr = null;
			try {
				tr = session.beginTransaction();
				for (Iterator it = map.keySet().iterator(); it.hasNext();) {
					key = (String) it.next();
					url = (String) map.get(key);					
					session.createQuery("update com.cari.rbac.Module set moduleDesc=:url where moduleID=:moduleID")
							.setString("url", url)
							.setString("moduleID", key)
							.executeUpdate();
				}
				tr.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 添加时检查是否有重复的模块key
	 * @param id
	 * @param session
	 * @throws BeRefuseException
	 */
	public void checkModule(String id, Session session) throws BeRefuseException {
		StringBuffer str = new StringBuffer();
		str.append("select count(m) from com.cari.rbac.Module m where m.moduleID=:id ");
		Long count = (Long) session.createQuery(str.toString())
										.setString("id", id)
										.uniqueResult();		
		if (count.longValue() > 0) {
			throw new BeRefuseException("模块key值重复！");
		}
	}
	
	public int getMaxSort(String id, Session session) {
		if (id.contains("-")) {
			//二级（以上）
			id = " like '" + id.substring(0, id.lastIndexOf("-") + 1) + "%'";
		} else {
			id = " not like '%-%'";
		}
		Integer c = (Integer)session.createQuery("select max(t.moduleSort) from com.cari.rbac.Module t where t.moduleID " + id).uniqueResult();
		return (c != null) ? c.intValue() : 0;
	}
	/**
	 * 该模块的最大排序
	 * @param id
	 * @return
	 */
	public String getMaxSort(String id) {
		if (id.contains("-")) {
			//二级（以上）
			id = " like '" + id.substring(0, id.lastIndexOf("-") + 1) + "%'";
		} else {
			id = " not like '%-%'";
		}
		return id;
	}
	/**
	 * 该模块的级别
	 * @param id
	 * @return
	 */
	public int getLevel(String id) {
		String[] array = id.split("-");	
		int length = array.length;		
		array = null;
		return length;
	}		
	/**
	 * /bus/mng/busstopform.jsp = BUSMT-STOP
	 * BUSMT-STOP = ../bus/mng/busstopform.jsp
	 * @param moduleUrl
	 * @param id
	 */
	public void setModuleUrl(String moduleUrl, String id) {
		BufferedWriter writer = null;
		try {						
			URL url = ModuleManage.class.getResource("moduleurl.properties");
			if (url != null) {						
//				moduleUrl = moduleUrl.replaceAll("=", "/=");
				MODULE_URL = moduleUrl;
				MODULE_ID = id;
				writer = new BufferedWriter(new FileWriter(url.getFile(), true));
				writer.newLine();writer.newLine();
				writer.write(moduleUrl + " = " + id);
				writer.newLine();
				writer.write(id + " = .." + id);
				writer.flush();
				BufferedReader reader = new BufferedReader(new FileReader(url.getFile()));
				String str = "";			
				while ((str = reader.readLine()) != null) {
					System.out.println(str);		
				}
				reader.close();	
				SystemConstant.URL_MOD_MAPPING.put(moduleUrl, id);
				SystemConstant.URL_MOD_MAPPING.put(id, moduleUrl);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			SystemConstant.URL_MOD_MAPPING.put(moduleUrl, id);
			SystemConstant.URL_MOD_MAPPING.put(id, moduleUrl);
		}
	}			
	
	public String getSecondModule(TreeMap tmModule1_2) {
		//遍历TreeMap和二级TreeSet生成MenuScript
		StringBuffer menuScript = new StringBuffer();
		String menuStr = "";
		Session session = HibernateUtil.getInstance().openSession();
		String url = "";
		try {
			for(Iterator it = tmModule1_2.keySet().iterator(); it.hasNext() ;) {
				//取出一级模块
				Module module1 = (Module)it.next();
				menuScript.append(module1.getModuleName()).append("///");
				//取出二级模块集合
				TreeSet lvl2ModuleSet = (TreeSet)tmModule1_2.get(module1);
				
				for(Iterator it2 = lvl2ModuleSet.iterator() ; it2.hasNext() ;){
					//取出二级模块
					Module module2 = (Module)it2.next();		
					String moduleURL = "";
					url = (String) session.createQuery("select moduleDesc from com.cari.rbac.Module where moduleID=:moduleID")
							.setString("moduleID", module2.getModuleID())
							.uniqueResult();
					if (url != null && !url.equals("")) {					
						moduleURL = url;
					} else {
						moduleURL = (String)SystemConstant.URL_MOD_MAPPING.get(module2.getModuleID());
						if (moduleURL != null && !moduleURL.equals("")) {
							moduleURL = moduleURL.replaceAll("/=", "=");
						} else {
							moduleURL = "javascript:void(0)";
						}
					}					
					menuScript.append(module2.getModuleName())
						.append('*')
						.append(moduleURL)
						.append("||");
				}
				menuScript.delete(menuScript.length() - 2 ,  menuScript.length());
				menuScript.append("&&&&");
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		if(menuScript.length() > 4){
			menuStr = menuScript.substring(0 , menuScript.length() -4);
		}else{
			menuStr = "";
		}		
		return menuStr;
	}
	
	public List getAllModule() {
		List list = null;
		Session session = HibernateUtil.getInstance().openSession();
		try {
			list = session.createQuery("from Module").list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
}
