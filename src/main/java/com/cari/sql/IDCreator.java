package com.cari.sql;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author zsy
 *	生成唯一的ID
 */

public class IDCreator {
	private static final String str64 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_~";
	private static final BigInteger SIXTEEN = new BigInteger("16");
	private static final BigInteger XIXTY_FOUR = new BigInteger("64");
	/**
	 * 将16进制转化为36进制
	 * @param strHex
	 * @return
	 */
	private static String hexTo64(String strHex) {
		strHex = strHex.toUpperCase();
		BigInteger total = BigInteger.ZERO;
		
		//转换成十进制
		BigInteger step = BigInteger.ONE;
		BigInteger tem = null;
		int hexLength = strHex.length();
		while (hexLength > 0) {
			char c = strHex.charAt(hexLength -1);
			tem = new BigInteger(Integer.toString(str64.indexOf(c)));
			tem = tem.multiply(step);
			total = total.add(tem);
			
			step = step.multiply(SIXTEEN);
			hexLength--;
		}
		//转换成36进制
		String result = "";
		while (total.compareTo(BigInteger.ZERO) > 0) {
			tem = total.remainder(XIXTY_FOUR);
			total = total.divide(XIXTY_FOUR);
			int i = tem.intValue();
			result = str64.substring(i, i + 1) + result;
		}
      return result;
    }
	
	/**
	 * 取得系统时间，精确到100秒，返回四位
	 * @return
	 */
	private static String getTimeStr() {
		String timeStr = hexTo64(Long.toHexString(System.currentTimeMillis() / 100000));
		if (timeStr.length() > 4) {
			return timeStr.substring(timeStr.length() -4, timeStr.length());
		} else {
			return timeStr;
		}
	}
	
	/**
	 * 生成12位ID
	 * @return
	 */
	public static String getID12() {
		String uuid = UUID.randomUUID().toString();
		int i = uuid.length();
		uuid = hexTo64(uuid.substring(i - 12, i)) + getTimeStr();
		if (uuid.length() > 12) {
			uuid = uuid.substring(0, 12);
		}
		return uuid;
	}
	
	/**
	 * 生成15位ID
	 * @return
	 */
	public static String getID16() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		int i = uuid.length();
		uuid = hexTo64(uuid.substring(i - 18, i)) + getTimeStr();
		if (uuid.length() > 16) {
			uuid = uuid.substring(0, 16);
		}
		return uuid;
	}
	
	/**
	 * 生成25位ID
	 * @return
	 */
	public static String getID25() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		uuid = hexTo64(uuid);
		if (uuid.length() > 25) {
			uuid = uuid.substring(0, 25);
		}
		return uuid;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	HashSet set = new HashSet();
		int i = 0;
		while (true)  {
			
			if (i++ % 10000 == 0) {
				System.out.println(i + "--" + getID12());
			}
			if (!set.add(getID12())) {
				System.out.println("重复：" + i);
				break;
			}
		}*/
	}
}
