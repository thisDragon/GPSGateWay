package com.analog.data.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 其它的一些公共操作方法
 * @author yexuhui
 * 2012-7-26 上午10:01:47
 */
public class Utils {
	
	public static Set<String> suffixVideoSet = new HashSet<String>();
	public static Set<String> suffixPicSet = new HashSet<String>();
	public static Set<String> suffixLogSet = new HashSet<String>();
	public static Set<String> suffixYYSet = new HashSet<String>();
	public static Set<String> prefixTmnNumSet = new HashSet<String>();
	static{
		String[] suffixPic = {"jpg","png","jpeg","bmp","gif","pcx","tiff","tga",
				"exif","fpx","svg","psd","cdr","pcd","dxf","ufo","eps","hdri","ai","raw"};
		String[] suffixVideo = {"mp4","3gp","avi","zip","3g2"};
		String[] suffixLog={"log","txt"};
		String[] suffixYY = {"qcp"};
		String[] prefixTmnNum = {"86"};
		Collections.addAll(suffixPicSet, suffixPic);
		Collections.addAll(suffixVideoSet, suffixVideo);
		Collections.addAll(suffixLogSet, suffixLog);
		Collections.addAll(prefixTmnNumSet, prefixTmnNum);
		Collections.addAll(suffixYYSet, suffixYY);
	};
	
	/**
	 * 根据字符串生成32位的MD5串
	 * @param str 字符串
	 * @return 生成后的MD5
	 * @throws Exception
	 */
	public static String md5(String str) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] bytes = str.getBytes();
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(bytes);
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char[] myChar = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
				myChar[(k++)] = hexDigits[(byte0 & 0xF)];
			}

			return new String(myChar);
		} catch (Exception e) {
			throw new RuntimeException(str + " : md5失败", e);
		}
	}

	/**
	 * 根据字符串生成32位的sha串
	 * @param str 字符串 
	 * @return 生成后的sha串
	 * @throws Exception
	 */
	public static String sha(String str) throws Exception {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] bt = str.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-1"); 
			md.update(bt);
			byte[] updateBytes = md.digest();
			int len = updateBytes.length;
			char[] myChar = new char[32];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
			}
			for (int i = 0; i < (32-len); i++) {
				byte byte0 = updateBytes[i];
				myChar[(k++)] = hexDigits[(byte0 & 0xF)];
			}
			return new String(myChar);
		} catch (Exception e) {
			throw new RuntimeException(str + " : sha失败", e);
		}
	}
	
	/**
	 * 生成json字符串，适用于object有date或timestamp类型属性的时候
	 * @param obj 需要生成json的对象
	 * @param dateFormat 日期格式
	 * @param writeClassName 是否要写入类名
	 * @return json字符串
	 * yexuhui 2012-7-26 上午11:01:22
	 */
	public static String toJson(Object obj, String dateFormat,
			boolean writeClassName) {
		if(writeClassName)
			return JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.WriteClassName, 
				SerializerFeature.WriteMapNullValue);
		return JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.WriteMapNullValue);
	}	
	
	/**
	 * 导入properties文件
	 * @param fileName 文件名
	 * @return Properties对象
	 * @throws IOException
	 * yexuhui 2012-7-29 上午11:49:09
	 */
	public static Properties loadProperties(String fileName) throws IOException {
		InputStream is = null;
		try {
			is = Utils.class.getClassLoader().getResourceAsStream(fileName);
			Properties properties = new Properties();
			properties.load(is);
			return properties;
		} finally {
			if (is != null) is.close();
		}
	}	
	
	/**
	 * 缓存中的字符串转化为具体类型对象
	 * 目前该方法只在cache类中使用，该方法不是很精准，还需改进
	 * @param content 需要转化的字符串
	 * @param type 内容类型
	 * @return 具体类型对象
	 * @author yexuhui
	 * @date 2013-5-6 下午2:43:28
	 */
	public static Object fromCacheStr(String content, Class<?> type) {
		Object value = null;
		
		if(type.equals(Integer.class)) 
			value = Integer.parseInt(content);
		else if(type.equals(Double.class)) 
			value = Double.parseDouble(content);
		else if(type.equals(Boolean.class)) 
			value = Boolean.parseBoolean(content);				
		else if(type.equals(Timestamp.class))
			value = DateUtils.str2Timestamp(content, DateUtils.NORMAL_FORMAT);
		else if(type.equals(Date.class))
			value = DateUtils.str2Date(content, DateUtils.NORMAL_FORMAT);	
		else if(type.equals(Map.class))
			value = JSON.parse(content);
		else
			value = content;		
		
		return value;
	}
	
	/**
	 * 将对象转化为缓存中的字符串
	 * @param value 对象实体
	 * @param type 对象类型
	 * @return 对象的字符串表现形式
	 * @author yexuhui
	 * @date 2013-5-6 下午3:08:20
	 */
	public static String toCacheStr(Object value, Class<?> type) {
		if(value == null)throw new RuntimeException("参数value为空转换为缓存中的字符串失败");
		
		String result = null;
		if (type.equals(Timestamp.class))
			result = DateUtils.timestamp2Str((Timestamp) value, DateUtils.NORMAL_FORMAT);
		else if (type.equals(Date.class))
			result = DateUtils.date2Str((Date) value, DateUtils.NORMAL_FORMAT);
		else if (type.equals(Map.class))
			result = JSON.toJSONString(value);
		else
			result = value.toString();
		return result;
	}

	public static boolean paramIsNotEmpty(Object object) {
		return (object != null && String.valueOf(object).length() != 0);
	}
	
	public static String pageSql(String orginSql, int start, int end){
      StringBuilder sb = new StringBuilder();
      sb.append("select AA.* from ");
      sb.append("( ");
      sb.append("  select rownum as rn, TT.* ");
      sb.append("  from (%s) TT");
      sb.append(") AA ");
      sb.append("where rn > %d and rn <= %d ");
      return String.format(sb.toString(), new Object[] { orginSql, Integer.valueOf(start), Integer.valueOf(end) });
    }
	
	/**
	 * 拆分集合
	 * @param <T>
	 * @param resList  要拆分的集合
	 * @param count	每个集合的元素个数
	 * @return  返回拆分后的各个集合
	 */
	public static  <T> List<List<T>> split(List<T> resList,int count){
		
		if(resList==null ||count<1)
			return  null ;
		List<List<T>> ret=new ArrayList<List<T>>();
		int size=resList.size();
		if(size<=count){ //数据量不足count指定的大小
			ret.add(resList);
		}else{
			int pre=size/count;
			int last=size%count;
			//前面pre个集合，每个大小都是count个元素
			for(int i=0;i<pre;i++){
				List<T> itemList=new ArrayList<T>();
				for(int j=0;j<count;j++){
					itemList.add(resList.get(i*count+j));
				}
				ret.add(itemList);
			}
			//last的进行处理
			if(last>0){
				List<T> itemList=new ArrayList<T>();
				for(int i=0;i<last;i++){
					itemList.add(resList.get(pre*count+i));
				}
				ret.add(itemList);
			}
		}
		return ret;
		
	}
	
	 /**
	 * 取出数组中的最大值
	 * @param arr
	 * @return
     */
	public int getMaxInt(Integer[] atts){
		int max = atts[0];
		for(int i = 0; i < atts.length; i++){
			if(atts[i] > max){
				max = atts[i];
			}
		}
		return max;
	}
	
	/** 
     * 判断str1中包含str2的个数 
      * @param str1 
     * @param str2 
     * @return counter 
     */  
    public static int countStr(String str1, String str2) {  
    	int counter = 0;
        if (str1.indexOf(str2) == -1) {  
            return 0;  
        } else if (str1.indexOf(str2) != -1) {  
            counter++;  
            countStr(str1.substring(str1.indexOf(str2) +  
                   str2.length()), str2);  
               return counter;  
        }  
            return 0;  
    }  
	
    /**
     * 两个数组合并
     * @param a
     * @param b
     * @return
     * @auther yinxingxing
     * @date   2017年6月6日
     */
	@SuppressWarnings("unchecked")
	public static <T> T[] concat(T[] a, T[] b) {  
	    final int alen = a.length;  
	    final int blen = b.length;  
	    if (alen == 0) {  
	        return b;  
	    }  
	    if (blen == 0) {  
	        return a;  
	    }  
	    final T[] result = (T[]) java.lang.reflect.Array.  
	            newInstance(a.getClass().getComponentType(), alen + blen);  
	    System.arraycopy(a, 0, result, 0, alen);  
	    System.arraycopy(b, 0, result, alen, blen);  
	    return result;  
	}
    
    /**
     * @Title: randomLengthUUID   
     * @Description: 获取随机位数的随机数   
     * @param index
     * @return 
     * String      
     * @throws
     * @author: yangjianlong
     * @date: 2020年1月6日 下午3:24:19
     */
    public static String randomLengthUUID(int index){
    	String uuid = UUID.randomUUID().toString().replace("-", "");
    	if (index > 0) {
			uuid = uuid.substring(0, index);
		}
		return uuid;
    }
    
    /**
     * @Title: isNumber   
     * @Description: 判断是否是数字
     * @param str
     * @return 
     * boolean      
     * @throws
     * @author: yangjianlong
     * @date: 2020年1月6日 下午3:24:45
     */
    public static boolean isNumber(String str){
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}
    
    /**
     * @Title: isLong   
     * @Description: 判断是否是Long类型
     * @param str
     * @return 
     * boolean      
     * @throws
     * @author: yangjianlong
     * @date: 2020年1月7日 上午8:52:36
     */
    public static boolean isLong(String str){
    	boolean isLong = false;
    	try {
    		Long.parseLong(str);
    		isLong = true;
		} catch (Exception e) {
			isLong = false;
		}
		return isLong;
	}
	
/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> resList=Arrays.asList("0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78","79","80","81","82","83","84","85","86","87","88","89","90","91","92","93","94","95","96","97","98","99");
		List<List<String>> ret=split(resList,10);
		
		for(int i=0;i<ret.size();i++){
			//System.out.println(ret.get(i));
		}
		
	}
}
