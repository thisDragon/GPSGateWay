/*
 * 创建日期 Mar 27, 2006
 * 数据库主件随机生成器
 * 林良益@miracle
 */
package com.cari.sql;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DBKeyCreator {
    /*
     * 初始化类标量
     */
    static StringBuffer randomPool = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    static char[] arrayPool = new char[randomPool.length()];
    static {
        for(int i = 0 ; i < randomPool.length() ; i ++){
            arrayPool[i] = randomPool.charAt(i);
        }
    }
    
    
    public DBKeyCreator() {
    }
    
    /*
    private static void exchangePoolData(int position){
        char tmp = arrayPool[position];
        arrayPool[position] = arrayPool[arrayPool.length - 1];
        arrayPool[arrayPool.length - 1] = tmp;
    }*/
    
    private static void exchangePoolData(int position){
        char tmp = arrayPool[0];
        for(int i = 1 ; i <= position ; i++){
        	arrayPool[i - 1] = arrayPool[i];
        }
        arrayPool[position] = tmp;
    }
    
    public static String getRandomKey(int keyLength){
    	if (keyLength == 12) {
    		return IDCreator.getID12();
    	} else if (keyLength == 16) {
    		return IDCreator.getID16();
    	}
    	int keyLengthTmp= keyLength;
    	
        int nt = 0;
        StringBuffer theKeyBuffer = new  StringBuffer();
        
        
        int ns = (int)(Math.random() * 1000000) << 6;
        //System.out.println(ns);
        while(ns > 0 && keyLengthTmp > 1){
            nt = ns % 36;
            ns = (ns / 7);
            theKeyBuffer.append(arrayPool[nt]);
            exchangePoolData(nt);
            keyLengthTmp--;
        }
        
        
        ns = (int)System.currentTimeMillis() << 20;
        while(ns > 0 &&  keyLengthTmp > 1){
            nt = ns % 36;
            ns = (ns / 7);
            theKeyBuffer.append(arrayPool[nt]);
            exchangePoolData(nt);
            keyLengthTmp--;
        }
        
        Calendar calendar = new GregorianCalendar();
        Date now = new Date();
        calendar.setTime(now);
        ns = calendar.get(Calendar.YEAR) * 365 + calendar.get(Calendar.MONTH) * 30 + calendar.get(Calendar.DATE);
        while(ns > 0 &&  keyLengthTmp > 1){
            nt = ns % 36;
            ns = (ns / 7);
            theKeyBuffer.append(randomPool.substring(nt , nt + 1));
            keyLengthTmp--;
        }
        
        while(theKeyBuffer.length() < keyLength ){
        	nt = (int)(Math.random() * 36);
        	theKeyBuffer.append(arrayPool[nt]);
        }
        
        return theKeyBuffer.toString();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
       // HashSet keySet = new HashSet();
       //do{
            long begin = System.currentTimeMillis();
            for(int i = 0 ; i < 400; i ++){
                String myKey = DBKeyCreator.getRandomKey(32);
                System.out.println(myKey);
            }
            long end = System.currentTimeMillis();
            System.out.print((end - begin) / 1000.0);
            System.out.println("   OK!");
        //    keySet.clear();
        //}while(result);
    }

}
