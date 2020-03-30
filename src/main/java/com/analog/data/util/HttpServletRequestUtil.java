package com.analog.data.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jodd.util.StringUtil;

public class HttpServletRequestUtil {
	public static int getInt(HttpServletRequest request, String name) {

		try {
			return Integer.decode(request.getParameter(name));
		} catch (Exception e) {
			return -1;
		}
	}

	public static long getLong(HttpServletRequest request, String name) {

		try {
			return Long.valueOf(request.getParameter(name));
		} catch (Exception e) {
			return -1;
		}
	}

	public static Double getDouble(HttpServletRequest request, String name) {

		try {
			return Double.valueOf(request.getParameter(name));
		} catch (Exception e) {
			return -1d;
		}
	}

	public static Boolean getBoolean(HttpServletRequest request, String name) {

		try {
			return Boolean.valueOf(request.getParameter(name));
		} catch (Exception e) {
			return false;
		}
	}

	public static String getString(HttpServletRequest request, String name) {
		try {
			String result = request.getParameter(name);
			if (result != null) {
				result = result.trim();
			}
			if ("".equals(result))
				result = null;
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @Title: getRequestBody   
	 * @Description: 读取request中body的json串并转换成jsonobject
	 * @param request
	 * @param logger
	 * @return
	 * @throws Exception 
	 * JSONObject      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年1月3日 下午5:22:09
	 */
	public static JSONObject getRequestBody(HttpServletRequest request, Logger logger) throws Exception{
		String inputLine;
	    String jsondata = "";
	    BufferedReader bReader = null;
	     try {
	    	 request.setCharacterEncoding("utf-8");
	    	 bReader = request.getReader();
		     while ((inputLine = bReader.readLine()) != null) {
		        jsondata += inputLine;
		       }
	     } catch (IOException e) {
	    	 logger.error("数据接收失败",  e);
	    	 throw e;
	     }finally{
	     }
	     if(StringUtil.isNotEmpty(jsondata)){
	    	 JSONObject jObject = JSON.parseObject(jsondata);
	    	 return jObject;
	     }
	     
		return null;
	}
	
	/**
	 * @Title: getRequestBodyOfJSONArray   
	 * @Description: 读取request中body的json串并转换成jsonoarray
	 * @param request
	 * @param logger
	 * @return
	 * @throws Exception 
	 * JSONArray      
	 * @throws
	 * @author: yangjianlong
	 * @date: 2020年2月10日 上午9:41:17
	 */
	public static JSONArray getRequestBodyOfJSONArray(HttpServletRequest request, Logger logger) throws Exception{
		String inputLine;
	    String jsondata = "";
	    BufferedReader bReader = null;
	     try {
	    	 request.setCharacterEncoding("utf-8");
	    	 bReader = request.getReader();
		     while ((inputLine = bReader.readLine()) != null) {
		        jsondata += inputLine;
		       }
	     } catch (IOException e) {
	    	 logger.error("数据接收失败",  e);
	    	 throw e;
	     }finally{
	     }
	     if(StringUtil.isNotEmpty(jsondata)){
	    	 JSONArray jObject = JSON.parseArray(jsondata);
	    	 return jObject;
	     }
	     
		return null;
	}
}