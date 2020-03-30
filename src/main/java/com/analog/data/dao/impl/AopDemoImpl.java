package com.analog.data.dao.impl;

import org.springframework.stereotype.Component;

import com.analog.data.dao.IAopDemo;

@Component
public class AopDemoImpl implements IAopDemo{

	@Override
    public int doAop(String param1, String param2) {
        System.out.println("doAop ......");
        return 1234567890;
    }

}
