package com.analog.data.cache;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import com.analog.data.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JedisUtilTest extends BaseTest {
	@Autowired
	private JedisUtil jedisUtil;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Sets jedisSets;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Test
	public void testSetListAndGetList() throws Exception {
	}

}
