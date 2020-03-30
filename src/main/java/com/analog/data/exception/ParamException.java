package com.analog.data.exception;

/**
 * 自定义参数异常
 * @author yjl
 */
public class ParamException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private Object exceptionObject = null;
	private String detailInfo ="";
	public ParamException(){
		super();
	}
	public ParamException(String msg){
		super(msg);
	}
	public ParamException(String msg,Object obj,String msgDetailInfo){
		super(msg);
		this.exceptionObject = obj;
		this.detailInfo = msgDetailInfo;
	}
	public String getDetailMes(){
		String logMsg = exceptionObject+":"+detailInfo;
		return logMsg;
	}

}
