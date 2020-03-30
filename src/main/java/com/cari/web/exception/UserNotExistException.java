/**
 * 
 */
package com.cari.web.exception;

/**
 * @author zsy
 * 用户不存在异常
 */
public class UserNotExistException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotExistException() {
		super("该用户不存在");
	}
	
	public UserNotExistException(String message) {
		super(message);
	}

}
