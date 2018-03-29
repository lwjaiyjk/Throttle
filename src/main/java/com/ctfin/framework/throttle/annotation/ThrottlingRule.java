/**
 * EnableThrottlingRule.java
 * author: yujiakui
 * 2017年9月23日
 * 上午10:16:25
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.model.ThrottleRuleTypeEnum;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;

@Documented
@Retention(RUNTIME)
@Target({ METHOD })
@Inherited
/**
 * @author yujiakui
 *
 *         上午10:16:25
 *
 *         限流规则
 */
public @interface ThrottlingRule {

	/**
	 * 限流规则对应的条件列表
	 *
	 * @return
	 */
	ThrottlingCondition[] conditions();

	/**
	 * 规则名称：每一个规则对应的名称不能一样
	 *
	 * @return
	 */
	String ruleName();

	/**
	 * 规则对应的entryValue获取器，默认值为“”表示对于接口的请求{@link ThrottleValueAcquirer}
	 *
	 * @return
	 */
	String ruleAcquirerBeanName() default "";

	/**
	 * 规则白名单
	 *
	 * @return
	 */
	String[] ruleWhiteNames() default {};

	/**
	 * 持久化规则对应的bean名称:要实现接口 {@link ThrottlePersistenceService}
	 *
	 * @return
	 */
	String rulePersistBeanName() default "";

	/**
	 * 组条件类型
	 *
	 * @return
	 */
	ThrottleRuleTypeEnum ruleType() default ThrottleRuleTypeEnum.THROTTLE;

	/**
	 * 限流异常处理方法的别名,对应的方法参数类型必须是(ProceedingJoinPoint,ThrottlePolicy,ThrottleException)
	 *
	 * @return
	 */
	String excepHandleMethodAlias() default "";

}
