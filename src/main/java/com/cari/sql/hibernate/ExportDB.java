package com.cari.sql.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * 创建数据库表，利用hibernate的工具类，将实体类映射导出到数据库，
 * @author fuquanming
 *
 */
public class ExportDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//读取配置文件
		Configuration cfg = new Configuration().configure();
		
		//创建SchemaExport对象
		SchemaExport export = new SchemaExport(cfg);
		
		//创建数据库表
		export.create(true,true);		
		
		System.out.println("-----------");

	}
	
	

}
