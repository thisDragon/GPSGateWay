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
* @ClassName: GpsDateSource
* @Description: 数据源
* @author yangjianlong
* @date 2019年11月6日上午9:37:29
*
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_GPSDATASOURCE")
public class GpsDataSourceEntity implements Serializable{

	private String id;			//主键
	private String sourceType;	//数据源类型
	private String deviceId;	//设备id
	private Double lon;			//经度
	private Double lat;			//纬度
	private String position;	//位置描述
	private String token;		//授权标识
	private String remark;		//备注
	private String speed;		//速度
	private Date gpsTime;		//gps上报时间
	private Date createTime;	//插入数据时间
	
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
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public Date getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
