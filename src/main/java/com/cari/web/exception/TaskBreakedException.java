package com.cari.web.exception;

/**
 * 任务中断异常，用于分图过程控制
 * @author richfans
 *
 */
public class TaskBreakedException extends Exception {

	private static final long serialVersionUID = 1L;

	public TaskBreakedException() {
		super("任务被中断。");
	}
	
	public TaskBreakedException(String msg) {
		super(msg);
	}
}
