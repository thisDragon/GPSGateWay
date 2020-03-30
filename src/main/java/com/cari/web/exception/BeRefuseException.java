/**
 * 
 */
package com.cari.web.exception;

/**
 * @author zsy
 * 用户非法操作
 */
public class BeRefuseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BeRefuseException() {
		super("拒绝操作");
	}
	
	public BeRefuseException(String message) {
		super(message);
	}
}
