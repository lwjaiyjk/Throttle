/**
 * ThrottleAspectAdvice.java
 * author: yujiakui
 * 2017年9月25日
 * 上午11:20:54
 */
package com.ctfin.framework.throttle.test;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author yujiakui
 *
 *         上午11:20:54
 *
 *         限流切面aop
 */

// @Component
// @Aspect
public class MyThrottleAspectAdvice {

	/**
	 * Pointcut 定义Pointcut，Pointcut的名称为aspectjMethod()，此方法没有返回值和参数
	 * 该方法就是一个标识，不进行调用
	 */
	@Pointcut("execution(* com.ctfin.framework.throttle.test.*.*(..))")
	public void aspectjMethod() {
	};

	/**
	 * Before 在核心业务执行前执行，不能阻止核心业务的调用。
	 *
	 * @param joinPoint
	 */
	@Before("aspectjMethod()")
	public void beforeAdvice(JoinPoint joinPoint) {
		System.out.println("-----beforeAdvice().invoke-----");
		System.out.println(" 此处意在执行核心业务逻辑前，做一些安全性的判断等等");
		System.out.println(" 可通过joinPoint来获取所需要的内容");
		System.out.println("-----End of beforeAdvice()------");
	}

	/**
	 * Around 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
	 *
	 * 注意：当核心业务抛异常后，立即退出，转向AfterAdvice 执行完AfterAdvice，再转到ThrowingAdvice
	 *
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "aspectjMethod()")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("-----aroundAdvice().invoke-----");
		System.out.println(" 此处可以做类似于Before Advice的事情");
		System.out.println(pjp.getSignature());

		// 调用核心逻辑
		Object retVal = pjp.proceed();
		System.out.println(" 此处可以做类似于After Advice的事情");
		System.out.println("-----End of aroundAdvice()------");
		return retVal;
	}

}
