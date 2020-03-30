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
* @ClassName: LogDataEntity
* @Description: 日志
* @author yangjianlong
* @date 2020年1月6日上午11:17:32
*
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_LOGDATA")
public class LogDataEntity implements Serializable{

	private String id;			//主键
	private Integer logType;	//日志类型(0上报日志，1订阅日志，2、查询日志)
	private String deviceId;	//设备id
	private String sourceType;	//数据源类型
	private String content;		//日志内容（成功记录id，失败记录全部字段，不参与转发则不记录）
	private Integer state;		//日志状态（-1未参与转发，0失败，1成功）
	private Date createTime;	//创建时间
	
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
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
