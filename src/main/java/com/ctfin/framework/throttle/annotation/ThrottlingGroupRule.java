/**
 * ThrottingGroupRule.java
 * author: yujiakui
 * 2017年9月23日
 * 下午2:18:59
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ METHOD })
@Inherited
/**
 * @author yujiakui
 *
 *         下午2:18:59
 *
 *         组规则
 */
public @interface ThrottlingGroupRule {

	/**
	 * 规则
	 *
	 * @return
	 */
	ThrottlingRule[] rules();

	/**
	 * 限流异常处理方法的别名,对应的方法参数类型必须是(ProceedingJoinPoint,ThrottlePolicy,ThrottleException)
	 *
	 * @return
	 */
	String excepHandleMethodAlias() default "";

}
