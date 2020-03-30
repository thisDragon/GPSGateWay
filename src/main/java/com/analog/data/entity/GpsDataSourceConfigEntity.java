package com.analog.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

/**
* @ClassName: GpsDataSourceConfigEntity
* @Description: 数据源配置
* @author yangjianlong
* @date 2019年11月14日下午2:18:20
*
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_GPSDATASOURCECONFIG")
public class GpsDataSourceConfigEntity implements Serializable{

	private String id;			//主键
	private String sourceName;	//数据源名称
	private String sourceType;	//数据源类型
	private String token;		//授权标识
	private Integer userFlag;	//使用标识（0为禁用，1为启用）
	private Integer timeSpan;	//时间跨度(单位:天)
	private String remark;		//备注
	private String createUser;	//创建人
	private Date createTime;	//创建时间
	private String modifyUser;	//修改人
	private Date modifyTime;	//修改时间
	
	@Id
	@Length(max = 32)
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}