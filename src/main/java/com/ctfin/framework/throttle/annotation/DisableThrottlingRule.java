/**
 * DisableThrottlingRule.java
 * author: yujiakui
 * 2018年1月9日
 * 下午4:29:09
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author yujiakui
 *
 *         下午4:29:09
 *
 *         限流规则失效
 */
public @interface DisableThrottlingRule {

	/**
	 * 失效限流规则对应的规则名称列表
	 * 
	 * @return
	 */
	String[] ruleNames() default {};
}
