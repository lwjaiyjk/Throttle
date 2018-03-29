/**
 * ThrottingCondition.java
 * author: yujiakui
 * 2017年9月23日
 * 上午10:24:59
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.model.ThrottleConditionTypeEnum;

@Documented
@Retention(RUNTIME)
@Target({ METHOD })
/**
 * @author yujiakui
 *
 *         上午10:24:59
 *
 *         限流条件注解
 */
public @interface ThrottlingCondition {

	/**
	 * 条件类型
	 *
	 * @return
	 */
	ThrottleConditionTypeEnum conditionType() default ThrottleConditionTypeEnum.SECOND;

	/**
	 * 最大访问次数
	 *
	 * @return
	 */
	float maxAccessTimes();

	/**
	 * 条件对应的entryvalue的获取器:要实现接口{@link ThrottleValueAcquirer}
	 *
	 * @return
	 */
	String cndAcquirerBeanName() default "";

	/**
	 * 限流规则对应的白名单
	 *
	 * @return
	 */
	String[] cndWhiteNames() default {};

}
