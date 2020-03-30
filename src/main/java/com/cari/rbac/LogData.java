package com.cari.rbac;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @ClassName: LogData
* @Description: 数据日志
* @author yangjianlong
* @date 2020年2月12日上午11:20:22
*
 */
public class LogData implements Serializable{

	private static final long serialVersionUID = 7351943191665752100L;
	
	private String id;			//主键
	private Integer logType;	//日志类型(0上报日志，1订阅日志，2、查询日志)
	private String deviceId;	//设备id
	private String sourceType;	//数据源类型
	private String content;		//日志内容（成功记录id，失败记录全部字段，不参与转发则不记录）
	private Integer state;		//日志状态（-1未参与转发，0转发 失败，1转发成功）
	private Timestamp createTime;	//创建时间
	
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
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
