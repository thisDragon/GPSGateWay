package com.analog.data.enums;

/**
* @ClassName: LogTypeEnum
* @Description: 日志类型
* @author yangjianlong
* @date 2020年1月7日下午2:56:01
*
 */
public enum LogTypeEnum {
	REPORT_LOG(0, "上报"),
	SUBSCIBE_LOG(1, "订阅"),
	SELECT_LOG(2, "查询");

	private int state;
	private String stateInfo;

	private LogTypeEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LogTypeEnum stateOf(int index) {
		for (LogTypeEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
