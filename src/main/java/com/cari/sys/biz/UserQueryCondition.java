/*
 * 创建日期 May 21, 2006
 * 林良益@caripower
 */
package com.cari.sys.biz;

import java.util.ArrayList;
import java.util.Map;


import com.cari.sql.hibernate.AbstQueryCondition;
import com.cari.web.util.HttpParamCaster;

public class UserQueryCondition extends AbstQueryCondition {

	
	public UserQueryCondition() {
		this.baseHql = "select user from SysUser as user where user.userId is not null";
		this.countHql = "select count(user) from SysUser as user where user.userId is not null";	
	}

	public void setParameters(Map parameters) {
		// 查询条件参数记录列表
		ArrayList sqlCondition = new ArrayList(4);
		sqlConditionValue = new ArrayList(4);
		dataType = new ArrayList(4);

		// 获取用户ID（暂代工号）
		String userId = HttpParamCaster.getUTF8Parameter(parameters, "userId");
		if (userId != null && !"".equals(userId)) {
			sqlCondition.add("user.userId = ?");
			sqlConditionValue.add(userId);
			dataType.add("STRING");
		}

		// 获取用户姓名
		String userName = HttpParamCaster.getUTF8Parameter(parameters, "userName");
		if (userName != null && !"".equals(userName)) {
			sqlCondition.add("user.userName like ?");
			sqlConditionValue.add("%" + userName + "%");
			dataType.add("STRING");
		}
		
		// 获取部门
		String dept = HttpParamCaster.getUTF8Parameter(parameters, "dept");
		if (dept != null && !"".equals(dept)) {
			if ("ROOT".equals(dept)){
				sqlCondition.add("(user.dept=? or user.dept is null)");
				sqlConditionValue.add(dept);
				dataType.add("STRING");
			}else{
				sqlCondition.add("user.dept=?");
				sqlConditionValue.add(dept);
				dataType.add("STRING");
			}
		}
		
		// 获取手机
		String mobile = HttpParamCaster.getUTF8Parameter(parameters, "mobile");
		if (mobile != null && !"".equals(mobile)) {
			sqlCondition.add("user.mobile like ?");
			sqlConditionValue.add(mobile);
			dataType.add("STRING");
		}
		
		// 组装HQL
		StringBuffer sb_sql = new StringBuffer(baseHql);
		StringBuffer sb_count = new StringBuffer(countHql);
		for (int i = 0; i < sqlCondition.size(); i++) {
			sb_sql.append(" and ").append((String) sqlCondition.get(i));
			sb_count.append(" and ").append((String) sqlCondition.get(i));
		}

		sb_sql.append(" order by user.userId asc");
		countHql = sb_count.toString();
		prepareHql = sb_sql.toString();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
