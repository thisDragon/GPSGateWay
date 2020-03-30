package com.analog.data.enums;

/**
* @ClassName: LogStateEnum
* @Description: 日志状态
* @author yangjianlong
* @date 2020年1月7日下午2:55:33
*
 */
public enum LogStateEnum {
	NOT_JOIN(-1, "未参与转发"),
	SUCCESS(1, "成功"), 
	FAIL(0, "失败");

	private int state;
	private String stateInfo;

	private LogStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LogStateEnum stateOf(int index) {
		for (LogStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
