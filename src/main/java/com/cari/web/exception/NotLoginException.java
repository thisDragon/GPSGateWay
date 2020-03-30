/**
 * 
 */
package com.cari.web.exception;

/**
 * @author zsy
 *
 */
public class NotLoginException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotLoginException() {
		super("你还未登录");
	}
	
	public NotLoginException(String msg) {
		super(msg);
	}
}
