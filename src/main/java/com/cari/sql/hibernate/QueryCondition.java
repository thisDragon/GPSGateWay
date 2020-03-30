/*
 * 创建日期 2005-12-31
 * 林良益 @ caripower
 */
package com.cari.sql.hibernate;

import java.util.Map;

import org.hibernate.Query;

/**
 * @author linliangyi@team of miracle
 *
 * 创建日期 2005-12-31
 * 
 */

public interface QueryCondition {
    /**
     * @return 返回 baseSql。
     */
    public String getBaseHql();

    /**
     * @param baseSql 要设置的 baseSql。
     */
    public void setBaseHql(String baseSql);

    /**
     * @return 返回 countSql。
     */
    public String getCountHql();

    /**
     * @param baseSql 要设置的 baseSql。
     */
    public void setCountHql(String CountHql);

    
    /**
     * @return 返回 prepareSql。
     */
    public String getPrepareHql();

    /**
     * @param prepareSql 要设置的 prepareSql。
     */
    public void setPrepareHql(String prepareSql);
    
    /**
     * 
     * @param parameters 要设置的查询参数表
     */
    public void setParameters(Map parameters);
    
    /**
     * 给prepareHQL载入参数
     * @param query Hibernate Query
     */
    public void launchParamValues(Query query);
}