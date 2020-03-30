package com.analog.data.enums;

/**
* @ClassName: LogStateEnum
* @Description: 日志状态
* @author yangjianlong
* @date 2020年1月7日下午2:55:33
*
 */
public enum DataSourceConfigUserFlagEnum {
	ENABLED(1, "启用"), 
	DISABLED(0, "禁用");

	private int state;
	private String stateInfo;

	private DataSourceConfigUserFlagEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static DataSourceConfigUserFlagEnum stateOf(int index) {
		for (DataSourceConfigUserFlagEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
