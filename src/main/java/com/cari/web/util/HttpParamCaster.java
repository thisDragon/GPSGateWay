/*
 * 创建日期 2005-12-13
 *
 */
package com.cari.web.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.*;

/**
 * @author linliangyi@team of miracle
 * @version 1.0
 * modify by zhuoshiyao 2007.8.22
 */

public class HttpParamCaster {

	private static String NULLSTRING = "";
	
	/**
	 * 对HTTP接收的参数进行编码转换 
	 * @param request
	 * @param name
	 * @param encoding
	 * @param defautlValue
	 * @return
	 */
	public static String getEncodedParameter(HttpServletRequest request,
			String name, String encoding, String defautlValue) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defautlValue;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			temp = new String(temp.getBytes("UTF-8"), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return defautlValue;
		}
		return temp;
	}

	public static String getEncodedParameter(HttpServletRequest request,
			String name, String encoding) {
		return getEncodedParameter(request, name, encoding, null);
	}
	
	/**
	 * 取得HTTP参数，值为空字符串时返加null
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getEncodedParameter(request, name, null, null);
	}

	/**
	 * 取得HTTP参数，值为空字符串或null时返回默认值
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String name,
			String defaultValue) {
		return getEncodedParameter(request, name, null, defaultValue);
	}

	/**
	 * 把接收的字符串转化为GBK编码
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getGBParameter(HttpServletRequest request, String name) {
		return getEncodedParameter(request, name, "UTF-8", null);
	}
	
	public static String getGBParameter(HttpServletRequest request, String name, 
			String defaultValue) {
		return getEncodedParameter(request, name, "UTF-8", defaultValue);
	}

	/**
	 * 把接收的字符串转化为UTF-8编码
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getUTF8Parameter(HttpServletRequest request, String name) {
		return getEncodedParameter(request, name, "UTF-8", null);
	}
	
	public static String getUTF8Parameter(HttpServletRequest request,
			String name, String defaultValue) {
		return getEncodedParameter(request, name, "UTF-8", defaultValue);
	}

	/**
	 * 对HTTP接收的参数数组进行编码转换
	 * @param request
	 * @param name
	 * @param encoding
	 * @return
	 */
	public static String[] getEncodedParameters(HttpServletRequest request,
			String name, String encoding) {

		String[] temp = request.getParameterValues(name);
		if (temp == null) {
			return null;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = new String(temp[i].getBytes("UTF-8"), encoding);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 从Map参数集中取得给定名称的参数值
	 * @param params
	 * @param name
	 * @param encoding
	 * @param defaultValue
	 * @return
	 */
	public static String getEncodedParameter(Map params, String name,
			String encoding, String defaultValue) {
		String[] arraytemp = (String[]) params.get(name);
		if (arraytemp == null) {
			return null;
		}
		String temp = arraytemp[0];
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultValue;
		}
		if (encoding == null) {
			return temp;
		}
		try {
			temp = new String(temp.getBytes("UTF-8"), encoding);
		} catch (Exception ex) {
			System.err.println(ex.toString());
			return defaultValue;
		}
		return temp;
	}

	public static String getGBParameter(Map params, String name) {
		return getEncodedParameter(params, name, "UTF-8", null);
	}

	public static String getUTF8Parameter(Map params, String name) {
		return getEncodedParameter(params, name, "UTF-8", null);
	}

	public static String getParameter(Map params, String name) {
		return getEncodedParameter(params, name, null, null);
	}

	/**
	 * 值为"trur"或'y"时返回true，否则返回false
	 * @param request
	 * @param name
	 * @param defaultVal
	 * @return
	 */
	public static boolean getBooleanParameter(HttpServletRequest request,
			String name, boolean defaultVal) {
		String temp = request.getParameter(name);
		if ("true".equalsIgnoreCase(temp) || "y".equalsIgnoreCase(temp)) {
			return true;
		} else if ("false".equalsIgnoreCase(temp) || "n".equalsIgnoreCase(temp)) {
			return false;
		} else {
			return defaultVal;
		}
	}
	
	public static boolean getBooleanParameter(HttpServletRequest request,
			String name) {
		return getBooleanParameter(request, name, false);
	}

	/**
	 * 把取得的参数传化为int类型
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static int getIntParameter(HttpServletRequest request, String name,
			int defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Integer.parseInt(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为double类型
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static double getDoubleParameter(HttpServletRequest request,
			String name, double defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Double.parseDouble(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return defaultNum;
	}

	/**
	 * 把取得的参数传化为long类型
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static long getLongParameter(HttpServletRequest request,
			String name, long defaultNum) {
		String temp = request.getParameter(name);
		if (temp == null || temp.trim().equals(NULLSTRING)) {
			return defaultNum;
		}
		try {
			defaultNum = Long.parseLong(temp);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
			return defaultNum;
	}

	/**
	 * 从request中取得绑定的String值
	 * @param request
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getStringAttribute(HttpServletRequest request,
			String name, String defaultValue) {
		String temp = (String) request.getAttribute(name);
		if (temp != null) {
			return temp;
		} else {
			return defaultValue;
		}
	}

	/**
	 * 从request中取得绑定的int值
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static int getIntAttribute(HttpServletRequest request, String name,
			int defaultNum) {
		Integer temp = (Integer)request.getAttribute(name);
		if (temp != null) {
			return temp.intValue();
		} else {
			return defaultNum;
		}
	}

	/**
	 * 从request中取得绑定的long值
	 * @param request
	 * @param name
	 * @param defaultNum
	 * @return
	 */
	public static long getLongAttribute(HttpServletRequest request,
			String name, long defaultNum) {
		Long temp = (Long) request.getAttribute(name);
		if (temp != null) {
			return temp.longValue();
		} else {
			return defaultNum;
		}
	}
}