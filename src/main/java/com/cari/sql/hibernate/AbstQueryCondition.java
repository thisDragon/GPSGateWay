/*
 * 创建日期 2005-12-30
 * 林良益 @ caripower
 * 
 */
package com.cari.sql.hibernate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Query;

/**
 * @author linliangyi@team of miracle
 *
 * 创建日期 2005-12-30
 * 
 * 抽象的查询条件
 */

public abstract class AbstQueryCondition implements QueryCondition {

	protected ArrayList sqlConditionValue = null;
	protected ArrayList dataType = null;


	protected String baseHql = null;
	protected String countHql = null;
	protected String prepareHql = null;

    /**
     * 
     */
    public AbstQueryCondition() {
        super();
        // TODO 自动生成构造函数存根
    }

    abstract public void setParameters(Map parameters);
    
    /**
     * @return 返回 baseSql。
     */
    public String getBaseHql() {
        return baseHql;
    }
    /**
     * @param baseSql 要设置的 baseSql。
     */
    public void setBaseHql(String baseSql) {
        this.baseHql = baseSql;
    }
    /**
     * @return 返回 prepareSql。
     */
    public String getPrepareHql() {
        return prepareHql;
    }
    /**
     * @param prepareSql 要设置的 prepareSql。
     */
    public void setPrepareHql(String prepareSql) {
        this.prepareHql = prepareSql;
    }
    
    /**
     * 参数载入
     */
    public void launchParamValues(Query query) {
        for(int i = 0; i < sqlConditionValue.size(); i++){
            if("STRING".equals( dataType.get(i) )){
                query.setString(i , (String) sqlConditionValue.get(i));	
            }
            
            if("INT".equals( dataType.get(i) )){
                query.setInteger(i , Integer.parseInt( (String) sqlConditionValue.get(i)));	
            }

            if("DATE".equals( dataType.get(i) )){
                query.setDate(i , (Date) sqlConditionValue.get(i));	
            }
        }
    }
    
    /**
     * @return 返回 countHql。
     */
    public String getCountHql() {
        return countHql;
    }
    /**
     * @param countHql 要设置的 countHql。
     */
    public void setCountHql(String countHql) {
        this.countHql = countHql;
    }
}
