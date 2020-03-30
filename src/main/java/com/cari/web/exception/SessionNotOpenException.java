/**
 * 
 */
package com.cari.web.exception;

/**
 * @author zsy
 *
 */
public class SessionNotOpenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SessionNotOpenException() {
		super("Hibernate's Session 未打开");
	}
	
	public SessionNotOpenException(String message) {
		super(message);
	}
}
