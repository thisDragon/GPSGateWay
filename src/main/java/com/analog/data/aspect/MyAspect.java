package com.analog.data.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
* @ClassName: MyAspect
* @Description: dao层测试切面类
* @author yangjianlong
* @date 2020年1月8日上午9:23:58
*
 */
@Aspect
@Component
public class MyAspect {

	//@Pointcut("execution(* com.analog.data.dao.IAopDemo.doAop(..))")
	@Pointcut("execution(public * com.analog.data.dao.*.*(..))")
	private void myPointcut(){}
	
	/**
     * 前置通知
     */
    @Before(value ="myPointcut()")
    public void before(JoinPoint joinPoint){
    	Object[] args = joinPoint.getArgs();
    	for (int i = 0; i < args.length; i++) {
			//System.out.println(args[i]);
		}
        //System.out.println("前置通知....args的长度:"+args.length);
    }

    /**
     * 后置通知
     * returnVal,切点方法执行后的返回值
     */
    @AfterReturning(value="myPointcut()",returning = "returnVal")
    public void AfterReturn(Object returnVal){
        //System.out.println("后置通知...."+returnVal);
    }


    /**
     * 环绕通知
     * @param joinPoint 可用于执行切点的类
     * @return
     * @throws Throwable
     */
    @Around(value = "myPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //System.out.println("环绕通知前....");
        Object obj= (Object) joinPoint.proceed();
        //System.out.println("环绕通知后....");
        return obj;
    }

    /**
     * 抛出通知
     * @param e
     */
    @AfterThrowing(value="myPointcut()",throwing = "e")
    public void afterThrowable(Throwable e){
        //System.out.println("出现异常:msg="+e.getMessage());
    }

    /**
     * 无论什么情况下都会执行的方法
     */
    @After(value="myPointcut()")
    public void after(){
        //System.out.println("最终通知....");
    }
}
