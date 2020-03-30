/*
 * @(#)Tools.java May 17 2008.
 * 
 * Copyright(2004-2008) of Cari Power Corp.,Inc. All rights reserved.
 * Cari Power Proprietary/Confidential. Use is subject to license terms.
 */
package com.cari.web.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 系统工具类
 * 
 * 目前主要用于进行各种数值的转换 
 * 
 * @author leidg.cn
 * @version 1.0, 2007-03-30
 * @see com.cari.wing.map.MapElementFactory
 * @see com.cari.wing4.sxw.LayerBuffer
 */

public class Tools {
	private static DecimalFormat m_Formater = null;	
	/**
	 * 取得当前的系统时间
	 * @return 字符串
	 */
	public static String getCurrentTime(){
		return (new Timestamp(System.currentTimeMillis())).toString();
	}
	/**
	 * 格式化double数
	 * @param num double值
	 * @param formatString 格式字符串
	 * @return 字符串
	 */
	public static String formatNumber(double num, String formatString){
		if (m_Formater == null) m_Formater = (DecimalFormat)NumberFormat.getInstance();
		m_Formater.applyPattern(formatString);
		String sReturn = m_Formater.format(num);
		return sReturn;
	}
	
	/**
	 * 格式化int数
	 * @param num int值
	 * @param formatString 格式字符串
	 * @return 字符串
	 */
	public static String formatNumber(int num, String formatString){
		if (m_Formater == null) m_Formater = (DecimalFormat)NumberFormat.getInstance();
		m_Formater.applyPattern(formatString);
		String sReturn = m_Formater.format(num);
		return sReturn;
	}
	
	/**
	 * 以三十六进制压缩字符串,主要用于地图图片名称的命名
	 * @param sText
	 * @return
	 */
	public static String zipNumber(String sText){
		long nNum = Long.parseLong(sText);
		String sResult = "";	//定义返回字符
		while(nNum > 35){
			long nMod = nNum % 36;
			if(nMod < 10){		//前10个用数字
				sResult = (char)(nMod + 48) + sResult;
			}else{//后26用大写字符代替
				sResult = (char)(nMod + 65 - 10) + sResult;
			}
			nNum = (nNum / 36);
		}
		if(nNum < 10){
			sResult = (char)(nNum + 48) + sResult;
		}else{
			sResult = (char)(nNum + 65 - 10) + sResult;
		}
		return sResult;
	}
	
	/**
	 * 解压缩三十六进制的字符中,还原文件名
	 * @param sText
	 * @return
	 */
	public static String rezipNumber(String sText){
		long  nReturn = 0;
		for(int i=0; i<sText.length(); i++){
			long tempNum = (sText.charAt(sText.length() - i - 1));
			if(tempNum >= 65){
				tempNum = (long)((tempNum - 65 + 10) * Math.pow(36,i));
			}else{
				tempNum = (long)((tempNum - 48) * Math.pow(36,i));
			}
			nReturn += tempNum;
		}
		
		return String.valueOf(nReturn);

	}
	
	public static void main(String[] args){
		System.out.println(Tools.zipNumber("140350402525"));
		System.out.println(Tools.rezipNumber("ZZZZZZZZ"));
	}
}
