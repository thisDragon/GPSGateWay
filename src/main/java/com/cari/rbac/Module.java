/*
 * 创建日期 May 20, 2006
 * 系统模块对象
 * 林良益 @caripower
 */
package com.cari.rbac;

import java.util.Set;
import java.util.TreeSet;

public class Module implements Comparable{

	public static final Module ROOT = new Module();//数据库中没有对映记录
	static{
		ROOT.setModuleID("MODULE_ROOT");
		ROOT.setModuleName("系统模块");
		ROOT.setModuleLevel(0);
		ROOT.setModuleSort(0);
	}
	//  MODULE_ID    VARCHAR2(50) primary key, 
	private String moduleID;
	//  MODULE_NAME  VARCHAR2(100) not null,
	private String moduleName;
	//  MODULE_LEVEL INTEGER not null,
	private int moduleLevel;
	//  MODULE_SORT  INTEGER not null,
	private int moduleSort;
	//  MODULE_DESC  VARCHAR2(100)
	private String moduleDesc;
	
	private Set operations = new TreeSet();
	
	public Module() {
	}

	/**
	 * @return 返回 moduleDesc。

	 */
	public String getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * @param moduleDesc 要设置的 module_desc。

	 */
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	/**
	 * @return 返回 moduleSort。

	 */
	public int getModuleSort() {
		return moduleSort;
	}

	/**
	 * @param moduleSort 要设置的 moduleSort。

	 */
	public void setModuleSort(int moduleSort) {
		this.moduleSort = moduleSort;
	}

	/**
	 * @return 返回 moduleID。

	 */
	public String getModuleID() {
		return moduleID;
	}

	/**
	 * @param moduleID 要设置的 moduleID。

	 */
	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	/**
	 * @return 返回 moduleLevel。

	 */
	public int getModuleLevel() {
		return moduleLevel;
	}

	/**
	 * @param moduleLevel 要设置的 moduleLevel。

	 */
	public void setModuleLevel(int moduleLevel) {
		this.moduleLevel = moduleLevel;
	}

	/**
	 * @return 返回 moduleName。

	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName 要设置的 moduleName。

	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Set getOperations() {
		return operations;
	}

	public void setOperations(Set operations) {
		this.operations = operations;
	}

	public boolean equals(Object obj){
		if(obj instanceof Module){
			Module module = (Module)obj; 
			if(this.moduleID.equals(module.getModuleID())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		if(this.moduleID != null){
			return this.moduleID.hashCode();
		}else{
			return 0;
		}
	}

	public int compareTo(Object o) {
		if(equals(o)){
			return 0;
		}
		
		if(o instanceof Module){
			Module mod = (Module)o;
			if(this.moduleSort > mod.getModuleSort()){
				return 1;
			}else if(this.moduleSort < mod.getModuleSort()){
				return -1;
			}
		}
		return 0;
	}		
}
