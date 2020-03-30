/*
 * 创建日期 2006-1-4
 *
 */
package com.cari.web.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author linliangyi@team of miracle
 * @author Dreamer He, 2006-9-1
 *
 * 创建日期 2006-1-4 
 * 页面显示辅助工具类
 */
public class DataFormater {
    /**
     * 
     */
    public DataFormater() {
        super();
        // TODO 自动生成构造函数存根
    }
    
    /**
     * 修正WEB页显示中的NULL
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(String arg){
        String result = "";
        if(arg != null){
            result = arg;
        }
        return result;
    }

    /**
     * 修正WEB页显示中的NULL
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(Object arg){
        String result = "";
        if(arg != null){
            result = arg.toString();
        }
        return result;
    }
    
    /**
     * 转化WEB页显示的日期
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(Date arg){
        return noNullValue(arg , "yyyy年MM月dd日");
    }


    /**
     * 转化WEB页显示的日期
     * @param arg
     * @param format 修正后的日期格式
     * @return 修正后的结果
     */
    public static String noNullValue(Date arg , String format){
        String result = "";
        if(arg != null){
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(arg);
        }
        return result;
    }
    
    /**
     * 转化WEB页显示的日期时间
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(Timestamp arg){
        return noNullValue(arg , "yyyy年MM月dd日 HH时mm分ss秒");
    }
    
    /**
     * 转化WEB页显示的日期时间
     * @param arg
     * @param format 修正后的日期时间格式
     * @return 修正后的结果
     */
    public static String noNullValue(Timestamp arg , String format){
        String result = "";
        if(arg != null){
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(arg);
        }
        return result;
    }
    
    /**
     * 值为0时不显示
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(double arg){
        String result = "";
        if(arg != 0){
            result = Double.toString(arg);
        }
        return result;
    }
    
    public static String noNullValue(int arg){
    	String result = "";
    	if (arg != 0){
    		result = Integer.toString(arg);
    	}
    	return result;
    }
    public static String noNullValue(long arg){
    	String result = "";
    	if (arg != 0){
    		result = Long.toString(arg);
    	}
    	return result;
    }

    /**
     * 将整型数转化成定长的字符窜
     * @return String
     */
    public static String intToString(int number , int length){
    	String intStr = String.valueOf(number);
    	if(intStr.length() > length){
    		throw new IllegalArgumentException("Argument 'length' is smaller than number's length.");
    	}else if(intStr.length() < length){
    		StringBuffer tmpIntStr = new StringBuffer();
    		for(int i=0 ; i < length - intStr.length() ; i++){
    			tmpIntStr.append('0');
    		}
    		intStr = tmpIntStr.append(intStr).toString();
    	}
    	return intStr;
    }

    /**
     * 子窜反转
     * @return
     */
    public static String reverse(String srcString){
    	if(srcString != null){
    		StringBuffer sb = new StringBuffer(srcString);
    		return sb.reverse().toString();
    	}
    	return "";
    }
    
    /**
     * 字节转2位16进制字窜
     * @param b
     * @return
     */
    private final static String[] hexDigits = {
        "0", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "A", "B", "C", "D", "E", "F"};
    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    /**
     * 字节数组专16进制字窜
     * @param b
     * @return
     */
    public static String byteArrayToString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    
    /**
     * 对字窜进新MD5摘要,并将摘要转化成16进制编码
     * @param origin 原始字窜
     * @return 摘要编码字窜
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToString(md.digest(origin.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }
    
    /**
     * 字窜全角转半角的函数(DBC case)
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        if (input == null ) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
    
	/**
	 * 将从数据库中取出的字符串进行UTF-8编码，满足网页上的要求
	 * @param sValue 从数据库中取出的字符串
	 * @return 编码后的字符串
	 */
	public static String strEncoding(String sValue){
		String sTemp = null;
		if (sValue == null){
			return "";
		}else{
			try{
				sTemp = new String(sValue.getBytes("UTF-8"),"UTF-8");
				return sTemp;
			}catch(UnsupportedEncodingException e){
				return sValue;
			}
		}
	}
	
	//Lucene保留字
    private static final String LUCENE_SPECIAL_CHARS = "[\\+\\-\\&&\\||\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\]";
	 /**
     * 过滤Lucene保留字
     * @param orgStr 原始字符串
     * @param useSpace =true 使用空格代替 ， = false 则使用转义符
     * @return
     */ 
    public static String filterLuceneReservedWord(String orgStr , boolean useSpace){
    	String result = orgStr;
    	if(useSpace){
    		result = orgStr.replaceAll(LUCENE_SPECIAL_CHARS , " ");
    	}else{
    		result = orgStr.replaceAll(LUCENE_SPECIAL_CHARS , "");
    	}
    	return result;
    }
    
    //需要过滤的标点符号（目前的系统中，不需要支持输入的逗号、句号等内容）
    private static final String SEARCH_CHARS_REPLACEMENT = "[\\,\\.\\，\\。\\、]";
    /**
     * 过滤标点符号
     * @param sOld 未过滤字符
     * @param useSpace 是否使用空格
     * @return
     */
    public static String filterInterpunction(String sOld, boolean useSpace){
    	String sResult = sOld;
    	if (useSpace){
    		sResult = sResult.replaceAll(SEARCH_CHARS_REPLACEMENT," ");
    	}else{
    		sResult = sResult.replaceAll(SEARCH_CHARS_REPLACEMENT,"");
    	}
    	//去除多余的空格
    	sResult = sResult.replaceAll("\\s{2,}"," ");
    	return sResult;
    }
    
    //用于将Lucene的关键字转换为需要查询的字符
	private static final String[] LUCENE_CHARS_REPLACEMENT = {"\\\\","\\\\\\\\"
		,"\\+","\\\\+","&&","\\\\&&","[\\||]","\\\\||"
		,"!","\\\\!","\\{","\\\\{"
		,"\\}","\\\\}","\\[","\\\\["
		,"\\]","\\\\]","\\(","\\\\("
		,"\\)","\\\\)","\\^","\\\\^"
		,"\"","\\\\\"","~","\\\\~"
		,"\\*","\\\\*","\\?","\\\\?"
		,":","\\\\:"};
	/**
	 * 转换Lucene保留字
	 * 参考资料：
	 * 作为查询语法的一部分，Lucene支持特殊字符。当前特殊支付包括：
	 * + - && || ! ( ) { } [ ] ^ " ~ * ? : \
	 * 要转化一个特殊字符，就在要转化的字符前加上 "\" ， 例如，搜索 (1+1):2 使用查询语句：
	 *  \(1\+1\)\:2
	 * @param sOld 输入的字符串
	 * @return 转换过的字符串
	 */
    public static String convertLuceneReservedWord(String sOld){
    	String sResult = sOld;
    	for (int i=0;i<LUCENE_CHARS_REPLACEMENT.length;i+=2){
    		sResult = sResult.replaceAll(LUCENE_CHARS_REPLACEMENT[i],LUCENE_CHARS_REPLACEMENT[i+1]);
    	}
    	return sResult;
    }
    
    public static void main(String arg[]){
    	/*
    	String[] a = {"名师眼睛(火车站店)眼睛-心灵的窗口--你好&&我&好,大家\"好||昊然|好"
    			,"昊然[何裴]+何禧，靠!晕死^了~怎么会\\这样?***注意****雷大干:再来!"};
    	for (int i=0;i<a.length;i++){
    		System.out.println("原文：" + a[i]);
    		System.out.println("过滤保留字：" + DataFormater.filterLuceneReservedWord(a[i], false));
    		System.out.println("转换保留字：" + DataFormater.convertLuceneReservedWord(a[i]));
    	}
    	*/
    	String sSearch = "电信， 长乐、 你好。   谁说的";
    	System.out.println("原文：" + sSearch);
    	System.out.println("不用空格过滤：" + DataFormater.filterInterpunction(sSearch,false));
    	System.out.println("使用空格过滤：" + DataFormater.filterInterpunction(sSearch,true));
    }
}
