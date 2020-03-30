package com.cari.rbac;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @ClassName: DataSourceConfig
* @Description: 数据源配置
* @author yangjianlong
* @date 2020年2月11日下午4:05:07
*
 */
public class DataSourceConfig implements Serializable{

	private static final long serialVersionUID = 5680845531960217092L;
	
	private String id;			//主键
	private String sourceName;	//数据源名称
	private String sourceType;	//数据源类型
	private String token;		//授权标识
	private Integer userFlag;	//使用标识（0为禁用，1为启用）
	private Integer timeSpan;	//时间跨度(单位:天)
	private String remark;		//备注
	private String createUser;	//创建人
	private Timestamp createTime;	//创建时间
	private String modifyUser;	//修改人
	private Timestamp modifyTime;	//修改时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getUserFlag() {
		return userFlag;
	}
	public void setUserFlag(Integer userFlag) {
		this.userFlag = userFlag;
	}
	public Integer getTimeSpan() {
		return timeSpan;
	}
	public void setTimeSpan(Integer timeSpan) {
		this.timeSpan = timeSpan;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
