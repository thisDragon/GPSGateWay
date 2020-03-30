package com.analog.data;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.analog.data.util.DateUtils;
import com.analog.data.util.Utils;

public class Test {

	public static void main(String[] args) {
		//System.out.println(new Date().getTime());
		/*String strURL = "http://192.168.2.68:9086/upLoadLocation";
		String params = "{\"sourceType\":\"数据源类型\",\"deviceId\":\"设备id\",\"lon\":123.45,\"lat\":22.33,\"position\":\"当前位置\",\"token\":\"1578043856270\",\"gpsTime\":1578043856270,\"remark\":\"这是备注\",\"speed\":\"12m/s\"}";
		post(strURL, params);*/
		/*String numStr = "123121112312111231211";
		System.out.println(isNumber(numStr));*/
		
		
		Date date1 = DateUtils.str2Date("2020-02-11 12:12:27", "yyyy-MM-dd HH:mm:ss");
		System.out.println(date1.getTime());
		Date date2 = DateUtils.str2Date("2020-02-12 12:12:49", "yyyy-MM-dd HH:mm:ss");
		System.out.println(date2.getTime());
		/*int minites =1;
		
		int timeInRange = DateUtils.getBetweenSeconds(date2, date1);
		System.out.println(timeInRange);*/
		
		
		/*String a = Utils.randomLengthUUID(16);
		long b = new Date().getTime();
		System.out.println(a);
		System.out.println(b);
		System.out.println(a+b);*/
		
		
		/*Date date1 = DateUtils.str2Date("2020-01-01 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date2 = DateUtils.str2Date("2020-01-02 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date3 = DateUtils.str2Date("2020-01-03 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date4 = DateUtils.str2Date("2020-01-04 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date5 = DateUtils.str2Date("2020-01-05 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date6 = DateUtils.str2Date("2020-01-06 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date7 = DateUtils.str2Date("2020-01-07 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date8 = DateUtils.str2Date("2020-01-08 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date9 = DateUtils.str2Date("2020-01-09 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date10 = DateUtils.str2Date("2020-01-10 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date11 = DateUtils.str2Date("2020-01-11 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date12 = DateUtils.str2Date("2020-01-12 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date13 = DateUtils.str2Date("2020-01-13 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date14 = DateUtils.str2Date("2020-01-14 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date15 = DateUtils.str2Date("2020-01-15 14:25:39", DateUtils.SIMPLE_FORMAT);
		Date date16 = DateUtils.str2Date("2020-01-16 14:25:39", DateUtils.SIMPLE_FORMAT);
		System.out.println("-------------");
		System.out.println(date1.getTime());
		System.out.println(date2.getTime());
		System.out.println(date3.getTime());
		System.out.println(date4.getTime());
		System.out.println(date5.getTime());
		System.out.println(date6.getTime());
		System.out.println(date7.getTime());
		System.out.println(date8.getTime());
		System.out.println(date9.getTime());
		System.out.println(date10.getTime());
		System.out.println("-----");
		System.out.println(date11.getTime());
		System.out.println(date12.getTime());
		System.out.println(date13.getTime());
		System.out.println(date14.getTime());
		System.out.println(date15.getTime());
		System.out.println(date16.getTime());*/
		
		/*Date nowDate = new Date();
		Date oneYearDate = DateUtils.getNextMonth(nowDate, -12);
		Date oneMonthDate = DateUtils.getNextMonth(nowDate, -1);
		System.out.println("删除时间为:"+DateUtils.date2Str(oneYearDate, DateUtils.NORMAL_FORMAT)+" 前\"上报\",\"订阅\",\"查询\"的日志记录");
		System.out.println("删除时间为:"+DateUtils.date2Str(oneMonthDate, DateUtils.NORMAL_FORMAT)+" 前的\"登录\",\"操作\"日志记录");*/
	}
	
	public static boolean isNumber(String str){
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}
	
	public static String post(String strURL,String params) {

	    try {
	        URL url = new URL(strURL);// 创建连接 
	        HttpURLConnection connection = (HttpURLConnection) 
	        url.openConnection(); 
	        connection.setDoOutput(true); 
	        connection.setDoInput(true); 
	        connection.setUseCaches(false); 
	        connection.setInstanceFollowRedirects(true); 
	        connection.setRequestMethod("POST");// 设置请求方式   
	        connection.setRequestProperty("Accept","application/json");// 设置接收数据的格式   
	        connection.setRequestProperty("Content-Type","application/json");// 设置发送数据的格式   
	        connection.connect(); 
	        OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream(),"UTF-8");// utf-8编码   
	        out.append(params); out.flush(); out.close(); // 读取响应   
	        int length = (int) connection.getContentLength();// 获取长度   
	        InputStream is = connection.getInputStream(); 
	        if (length != -1){
	            byte[] data = new byte[length]; 
	            byte[] temp = new byte[512]; 
	            int readLen = 0; int destPos = 0; 
	            while ((readLen = is.read(temp)) > 0){
	                System.arraycopy(temp, 0, data, destPos, readLen); 
	                destPos += readLen; 
	            }
	            String result = new String(data, "UTF-8");
	            System.out.println(result); 
	            return result; 
	        } 
	    } catch (Exception e) {
	        // TODO: handle exception
	        e.printStackTrace();
	    }
	    return "error";
	    
	}
}
