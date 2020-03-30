package com.cari.rbac;

import java.io.Serializable;

/**
 * 模块权限
 * @author linliangyi@caripwoer
 *
 */
public class RoleModuleRight implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4270319941088652544L;
	//MODULE_ID	VARCHAR2(20)		not null, --模块ID
	private String module_id;
	//ROLE_ID		VARCHAR2(20) 		not null, --角色ID
	private String role_id;
	//RIGHT_VALUE	NUMBER                  not null, --角色在该模块的总权值
	private int right_value;
	
	public RoleModuleRight() {
		super();
	}

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public int getRight_value() {
		return right_value;
	}

	public void setRight_value(int right_value) {
		this.right_value = right_value;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public boolean equals(Object obj){
		if(obj instanceof RoleModuleRight){
			RoleModuleRight another = (RoleModuleRight)obj;
			if(another.getModule_id().equals(this.module_id)
					&& another.getRole_id().equals(this.role_id)){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		int hash = 0;
		if(this.module_id != null){
			hash = this.module_id.hashCode();
		}
		
		if(this.role_id != null){
			hash = hash * 17 + this.role_id.hashCode();
		}
		return hash;
	}
}
