/*
 * 创建日期 2006-1-12
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.cari.sys.biz;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;


import com.cari.sql.hibernate.AbstQueryCondition;
import com.cari.web.util.HttpParamCaster;

/**
 * @author fuquanming
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class EventLogQueryCondition extends AbstQueryCondition {
    
    public EventLogQueryCondition() {
        super();
        this.baseHql = "select log from SysEventLog as log where log.dwKey is not null";
        this.countHql = "select count(log) from SysEventLog as log where log.dwKey is not null";
    }
    
    /* （非 Javadoc）
     * @see com.cp.sql.hibernate.AbstQueryCondition#setParameters(java.util.Map)
     */
    public void setParameters(Map parameters) {
  	  //查询条件参数记录列表
    	ArrayList sqlCondition = new ArrayList(4);
    	sqlConditionValue = new ArrayList(4);
    	dataType = new ArrayList(4);
    	
    	//获取日志公司编号
    	String userName = HttpParamCaster.getUTF8Parameter(parameters, "userName");
    	if(userName != null) {
    		sqlCondition.add("log.userName = ?");
    		sqlConditionValue.add(userName);
    		dataType.add("STRING");
  	  	}
    	
    	String model = HttpParamCaster.getUTF8Parameter(parameters, "model");
    	if(model != null) {
    		sqlCondition.add("log.model = ?");
    		sqlConditionValue.add(model);
    		dataType.add("STRING");
  	  	}
    	//获取日志用户帐号
    	String userID = HttpParamCaster.getUTF8Parameter(parameters, "userID");
    	if(userID != null) {
    		sqlCondition.add("log.userID = ?");
    		sqlConditionValue.add(userID);
    		dataType.add("STRING");
  	  	}
    	//获取日志事件
    	String event = HttpParamCaster.getUTF8Parameter(parameters, "event");
    	if(event != null) {
    		sqlCondition.add("log.event = ?");
    		sqlConditionValue.add(event);
    		dataType.add("STRING");
  	  	}
    	 //获取事件时间范围
  		String submitDateStart = HttpParamCaster.getUTF8Parameter(parameters, "submitDateStart");
  		String submitDateEnd = HttpParamCaster.getUTF8Parameter(parameters, "submitDateEnd");
  		if(submitDateStart != null || submitDateEnd != null){

  		    if (submitDateStart == null || submitDateStart.trim().equals("")) {
  		        submitDateStart = "1970-01-01";
  		  }
  		    if (submitDateEnd == null || submitDateEnd.trim().equals("")) {
  		        submitDateEnd = "2070-01-01";
  		  }
  		  
  		  sqlCondition.add("log.eventTime between ? and ? ");
  		  sqlConditionValue.add(Date.valueOf(submitDateStart));
  		  sqlConditionValue.add(Date.valueOf(submitDateEnd));
  		  dataType.add("DATE");
  		  dataType.add("DATE");
  	  }	
    	
    	
    	//组装HQL
    	  
    	StringBuffer sb_sql =  new StringBuffer(baseHql);
    	StringBuffer sb_count = new StringBuffer(countHql);
    	for (int i = 0; i < sqlCondition.size(); i++) {
    		sb_sql.append(" and ").append( (String) sqlCondition.get(i));
    		sb_count.append(" and ").append( (String) sqlCondition.get(i));
    	}
    	sb_sql.append(" order by log.eventTime desc");
    	countHql = sb_count.toString();
    	prepareHql = sb_sql.toString();
    }
}
