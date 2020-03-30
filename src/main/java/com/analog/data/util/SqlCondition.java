/**
 * @author yanzihui
 */
package com.analog.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jodd.util.StringUtil;

/**
 * SQL条件
 * @author yanzihui
 * @date 2013-5-13 下午3:09:33
 */
public class SqlCondition {
	private StringBuilder sb = new StringBuilder(" ");
	private List<Object> params = new ArrayList<Object>();
	private Set<String> prefixSet = new HashSet<String>();

	public String getCondition() {
		return this.sb.toString();
	}

	public List<Object> getParams() {
		return this.params;
	}
	
	public Set<String> getPrefixSet() {
		return this.prefixSet;
	}

	public SqlCondition and() {
		this.sb.append(" and ");
		return this;
	}

	public SqlCondition or() {
		this.sb.append(" or ");
		return this;
	}
	
	public SqlCondition leftParen() {
		this.sb.append(" ( ");
		return this;
	}
	
	public SqlCondition rightParen() {
		this.sb.append(" ) ");
		return this;
	}

	public SqlCondition equal(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" = ? ");
		this.params.add(value);
		return this;
	}

	public SqlCondition equal(String column, Object value, String prefix) {
		addPrefix(prefix);
		return equal(column, value);
	}

	public SqlCondition notEqual(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" != ? ");
		this.params.add(value);
		return this;
	}

	public SqlCondition notEqual(String column, Object value, String prefix) {
		addPrefix(prefix);
		return notEqual(column, value);
	}

	public SqlCondition gt(String column, Object value) {
		checkParam(column, value);
		if (value instanceof Date) {
			//this.sb.append(column).append(" > ").append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
			this.sb.append(column).append(" > ").append("cast(? as datetime)");
			this.params.add(DateUtils.date2Str((Date) value,"yyyy-MM-dd HH:mm:ss"));
		} else {
			this.sb.append(column).append(" > ? ");
			this.params.add(value);
		}
		return this;
	}

	public SqlCondition gt(String column, Object value, String prefix) {
		addPrefix(prefix);
		return gt(column, value);
	}

	public SqlCondition ge(String column, Object value) {
		checkParam(column, value);
		if (value instanceof Date) {
			//this.sb.append(column).append(" >= ").append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
			this.sb.append(column).append(" >= ").append("cast(? as datetime)");
			this.params.add(DateUtils.date2Str((Date) value,"yyyy-MM-dd HH:mm:ss"));
		} else {
			this.sb.append(column).append(" >= ? ");
			this.params.add(value);
		}
		return this;
	}

	public SqlCondition ge(String column, Object value, String prefix) {
		addPrefix(prefix);
		return ge(column, value);
	}

	public SqlCondition lt(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" < ? ");
		this.params.add(value);
		return this;
	}

	public SqlCondition lt(String column, Object value, String prefix) {
		addPrefix(prefix);
		return lt(column, value);
	}

	public SqlCondition le(String column, Object value) {
		checkParam(column, value);
		if (value instanceof Date) {
			//this.sb.append(column).append(" <= ").append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
			this.sb.append(column).append(" <= ").append("cast(? as datetime)");
			this.params.add(DateUtils.date2Str((Date) value,"yyyy-MM-dd HH:mm:ss"));
		} else {
			this.sb.append(column).append(" <= ? ");
			this.params.add(value);
		}
		return this;
	}

	public SqlCondition le(String column, Object value, String prefix) {
		addPrefix(prefix);
		return le(column, value);
	}

	public SqlCondition le1(String column, Object value) {
		checkParam(column, value);
		if (value instanceof Date) {
			//this.sb.append(column).append(" < ").append("to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
			this.sb.append(column).append(" < ").append("cast(? as datetime)");
			this.params.add(DateUtils.date2Str((Date) value,
					"yyyy-MM-dd HH:mm:ss"));
		} else {
			this.sb.append(column).append(" < ? ");
			this.params.add(value);
		}
		return this;
	}
	
	public SqlCondition like(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" like ? ");
		this.params.add("%" + value + "%");
		return this;
	}

	public SqlCondition like(String column, Object value, String prefix) {
		addPrefix(prefix);
		return like(column, value);
	}

	public SqlCondition llike(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" like ? ");
		this.params.add("%" + value);
		return this;
	}

	public SqlCondition llike(String column, Object value, String prefix) {
		addPrefix(prefix);
		return llike(column, value);
	}

	public SqlCondition rlike(String column, Object value) {
		checkParam(column, value);
		this.sb.append(column).append(" like ? ");
		this.params.add(value + "%");
		return this;
	}

	public SqlCondition rlike(String column, Object value, String prefix) {
		addPrefix(prefix);
		return rlike(column, value);
	}

	public SqlCondition between(String column, Object value1, Object value2) {
		checkBetween(column, value1, value2);
		this.sb.append(column).append(" between ? and ? ");
		this.params.add(value1);
		this.params.add(value2);
		return this;
	}

	public SqlCondition between(String column, Object value1, Object value2,
			String prefix) {
		addPrefix(prefix);
		return between(column, value1, value2);
	}

	public SqlCondition notBetween(String column, Object value1, Object value2) {
		checkBetween(column, value1, value2);
		this.sb.append(column).append(" not between ? and ? ");
		this.params.add(value1);
		this.params.add(value2);
		return this;
	}

	public SqlCondition notBetween(String column, Object value1, Object value2,
			String prefix) {
		addPrefix(prefix);
		return notBetween(column, value1, value2);
	}

	public SqlCondition isNull(String column) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		this.sb.append(column).append(" is null ");
		return this;
	}

	public SqlCondition isNull(String column, String prefix) {
		addPrefix(prefix);
		return isNull(column);
	}

	public SqlCondition isNotNull(String column) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		this.sb.append(column).append(" is not null ");
		return this;
	}

	public SqlCondition isNotNull(String column, String prefix) {
		addPrefix(prefix);
		return isNotNull(column);
	}

	private SqlCondition ins(String column, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");

		this.sb.append(column).append(" in ( ");
		for (int i = 0; i < objs.length; ++i) {
			if(objs[i] instanceof Collection){
				
			}
			this.sb.append("?");
			this.params.add(objs[i]);
			if (i != objs.length - 1)
				this.sb.append(", ");
		}
		this.sb.append(") ");
		return this;
	}
	
	public SqlCondition in(String column, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");
		
		if(objs.length < MAXIN) return ins(column, objs);
		
		this.sb.append("( ");
		int divide = objs.length / MAXIN;
		int mod = objs.length % MAXIN;
		for (int i = 0; i < divide; i++) {
			Object[] b = Arrays.copyOfRange(objs, i*MAXIN, MAXIN*(i+1));
			if(i == 0) ins(column, b);
			else or().ins(column, b);
		}
		if(mod > 0){
			Object[] b = Arrays.copyOfRange(objs, (divide)*MAXIN, (divide)*MAXIN+mod);
			or().ins(column, b);
		}
		this.sb.append(" )");
		return this;
	}
	
	public SqlCondition in(String column, String prefix, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");
		
		if(objs.length < MAXIN) {
			addPrefix(prefix);
			return ins(column, objs);
		}
		this.sb.append("( ");
		int divide = objs.length / MAXIN;
		int mod = objs.length % MAXIN;
		for (int i = 0; i < divide; i++) {
			Object[] b = Arrays.copyOfRange(objs, i*MAXIN, MAXIN*(i+1));
			if(i != 0) or();
			addPrefix(prefix);
			ins(column, b);
		}
		if(mod > 0){
			Object[] b = Arrays.copyOfRange(objs, (divide)*MAXIN, (divide)*MAXIN+mod);
			or();
			addPrefix(prefix);
			ins(column, b);
		}
		this.sb.append(" )");
		return this;
	}

	private SqlCondition notIns(String column, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");

		this.sb.append(column).append(" not in ( ");
		for (int i = 0; i < objs.length; ++i) {
			this.sb.append("?");
			this.params.add(objs[i]);
			if (i != objs.length - 1)
				this.sb.append(", ");
		}
		this.sb.append(") ");
		return this;
	}
	private static final int MAXIN = 1000;
	
	public SqlCondition notIn(String column, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");

		if(objs.length < MAXIN) return notIns(column, objs);
		
		this.sb.append("( ");
		int divide = objs.length / MAXIN;
		int mod = objs.length % MAXIN;
		for (int i = 0; i < divide; i++) {
			Object[] b = Arrays.copyOfRange(objs, i*MAXIN, MAXIN*(i+1));
			if(i == 0) notIns(column, b);
			else and().notIns(column, b);
		}
		if(mod > 0){
			Object[] b = Arrays.copyOfRange(objs, (divide)*MAXIN, (divide)*MAXIN+mod);
			and().notIns(column, b);
		}
		this.sb.append(" )");
		return this;
	}

	public SqlCondition notIn(String column, String prefix, Object... objs) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (objs == null)
			throw new RuntimeException("objs值为null");
		if(objs.length < MAXIN) {
			addPrefix(prefix);
			return notIns(column, objs);
		}
		this.sb.append("( ");
		int divide = objs.length / MAXIN;
		int mod = objs.length % MAXIN;
		for (int i = 0; i < divide; i++) {
			Object[] b = Arrays.copyOfRange(objs, i*MAXIN, MAXIN*(i+1));
			if(i != 0) and();
			addPrefix(prefix);
			notIns(column, b);
		}
		if(mod > 0){
			Object[] b = Arrays.copyOfRange(objs, (divide)*MAXIN, (divide)*MAXIN+mod);
			and();
			addPrefix(prefix);
			notIns(column, b);
		}
		this.sb.append(" )");
		return this;
	}
	
	private void addPrefix(String prefix) {
		if (StringUtil.isEmpty(prefix)) {
			this.sb.append("");
			return;
		}
			
		this.sb.append(prefix).append(".");
		this.prefixSet.add(prefix);
	}

	private void checkParam(String column, Object value) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (value == null)
			throw new RuntimeException("value值为null");
	}

	private void checkBetween(String column, Object value1, Object value2) {
		if (StringUtil.isEmpty(column))
			throw new RuntimeException("列字段为空");
		if (value1 == null)
			throw new RuntimeException("value1值为null");
		if (value2 == null)
			throw new RuntimeException("value2值为null");
		if ((value1 instanceof Date) && (!(value2 instanceof Date)))
			throw new RuntimeException("value1和value2必须同时为日期类型");
		if ((value2 instanceof Date) && (!(value1 instanceof Date)))
			throw new RuntimeException("value1和value2必须同时为日期类型");
	}

}
