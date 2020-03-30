package com.cari.rbac;

/**
 * 创建日期：2006.6.20
 * @author 
 * linly@caripower
 */
public class Role {

	public static final Role ROOT = new Role();//数据库中没有对映记录
	static{
		ROOT.setRole_id("RBACRole");
		ROOT.setRole_name("系统角色配置");
		ROOT.setRole_orderNo(-1);
		ROOT.setRole_desc("");
	}
	
	//用户基础角色
	public static final Role SYS_BASE_ROLE = new Role();//数据库中有对映记录
	static{
		SYS_BASE_ROLE.setRole_id("BASE_ROLE");
		SYS_BASE_ROLE.setRole_name("基础用户");
		SYS_BASE_ROLE.setRole_orderNo(2);
	}
	
	//ROLE_ID		VARCHAR2(20) primary key not null, --角色ID
	private String role_id;
	//ROLE_NAME	VARCHAR2(50)             not null, --角色命名
	private String role_name;
	//ROLE_ORDERNO    NUMBER             not null, --角色顺序  
	private int role_orderNo;
	//ROLE_DESC	VARCHAR2(200)			 --角色信息描述
	private String role_desc;
	
	/**
	 * 
	 */
	public Role() {
		super();
	}


	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public int getRole_orderNo() {
		return role_orderNo;
	}

	public void setRole_orderNo(int role_orderNo) {
		this.role_orderNo = role_orderNo;
	}

	public boolean equals(Object obj){
		if(obj instanceof Role){
			Role role = (Role)obj; 
			if(this.role_id.equals(role.getRole_id())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		if(this.role_id != null){
			return this.role_id.hashCode();
		}else{
			return 0;
		}
	}
	
}
