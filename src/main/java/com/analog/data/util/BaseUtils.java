package com.analog.data.util;

import java.util.UUID;

/**
* @ClassName: BaseUtils
* @Description: 基础工具类
* @author yangjianlong
* @date 2019年11月6日上午9:24:49
*
 */
public class BaseUtils {

	public static String createId32(){
	    UUID guid = UUID.randomUUID();
	    return guid.toString().replace("-", "");
	}
}
