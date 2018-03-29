/**
 * ThrottlingExceptionRule.java
 * author: yujiakui
 * 2018年3月22日
 * 下午2:07:09
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author yujiakui
 *
 *         下午2:07:09
 *
 *         限流异常处理规则
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface ThrottlingExceptionRule {

	/**
	 * 异常处理方法对应的别名
	 * 对应的方法参数类型必须是(ProceedingJoinPoint,ThrottlePolicy,ThrottleException)
	 *
	 * @return
	 */
	String handleMethodAliasName();
}
