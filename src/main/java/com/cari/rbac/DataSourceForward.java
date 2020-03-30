package com.cari.rbac;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @ClassName: DataSourceForward
* @Description: 转发订阅
* @author yangjianlong
* @date 2020年2月11日下午4:05:34
*
 */
public class DataSourceForward implements Serializable{

	private static final long serialVersionUID = 4344827922018552497L;

	private String id;					//主键
	private String subscriptionName;	//订阅名称
	private String forwardUrl;			//转发地址
	private String sourceType;			//数据源类型
	private String remark;				//备注
	private String account;				//账号
	private String password;			//密码
	private Integer isEnable;			//是否启用（0为禁用，1为启用）
	private String createUser;			//创建人
	private Timestamp createTime;		//创建时间
	private String modifyUser;			//修改人
	private Timestamp modifyTime;		//修改时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubscriptionName() {
		return subscriptionName;
	}
	public void setSubscriptionName(String subscriptionName) {
		this.subscriptionName = subscriptionName;
	}
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Timestamp getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
