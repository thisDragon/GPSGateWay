package com.cari.rbac;

import java.io.Serializable;

public class Operation implements Serializable{

	private static final long serialVersionUID = 3547657453359660222L;
	private String operate_id;
	private String operate_name;
	private int operate_value;
	
	public Operation() {
		super();
	}

	public String getOperate_id() {
		return operate_id;
	}

	public void setOperate_id(String operate_id) {
		this.operate_id = operate_id;
	}

	public String getOperate_name() {
		return operate_name;
	}

	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}

	public int getOperate_value() {
		return operate_value;
	}

	public void setOperate_value(int operate_value) {
		this.operate_value = operate_value;
	}

	public boolean equals(Object obj){
		if(obj instanceof Operation){
			Operation op = (Operation)obj; 
			if(this.operate_id.equals(op.getOperate_id())){
				return true;
			}
		}
		return false;
	}
	
	public int hashCode(){
		if(this.operate_id != null){
			return this.operate_id.hashCode();
		}else{
			return 0;
		}
	}
}
