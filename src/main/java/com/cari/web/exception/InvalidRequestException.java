/*
 * @(#)InvalidRequestException.java 1.0 Jul 5, 2006
 *
 * Copyright(2004-2006) of Cari Power Corp.,Inc.. All rights reserved.
 * Cari Power PROPRIETARY/CONFIDENTIAL, Use is subject to license terms.
 */
package com.cari.web.exception;

/**
 * 类描述：该类用于处理Servlet中传入了非法参数的情况
 * 用法： throw new InvalidRequestException("错误内容");
 * @author Dreamer He
 * @version 1.0, Jul 5, 2006
 * @see
 */
public class InvalidRequestException extends Exception {
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID = 7595048177340914517L;
	private Throwable m_ex;
	
	/**
	 * Constructs a <code>SVGCreateException</code> with no detail message
	 */
	public InvalidRequestException(){
		super((Throwable)null);		//Disallow initCause
	}
	
	/**
	 * Constructs a <code>SVGCreateException</code> with the specified detail message
	 * @param sMsg
	 */
	public InvalidRequestException(String sMsg){
		super(sMsg,null);		//Disallow initCause
	}
	
	/**
	 * Constructs a <code>SVGCreateException</code> with the specified detail
	 * message and optional exception that was raised while loading the class
	 * @param sMsg
	 * @param ex
	 */
	public InvalidRequestException(String sMsg, Throwable ex){
		super(sMsg,null);	//Disallow initCause
		this.m_ex = ex;
	}
	
	/**
	 * @return the <code>Exception</code> that was raised while loading a class
	 */
	public Throwable getException(){
		return m_ex;
	}
	
	/**
	 * Return the cause of this exception (the exception that was raised
	 * if an error occured while attempting to load the class; otherwise
	 * <tt>null</tt>).
	 * @return the cause of this exception
	 */
	public Throwable getCause(){
		return m_ex;
	}
}
