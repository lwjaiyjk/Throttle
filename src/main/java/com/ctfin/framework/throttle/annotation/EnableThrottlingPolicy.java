/**
 * EnableThrottling.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:19:34
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
// 接口标注，则对应的子类也具有这种限流特性
@Inherited
/**
 * @author yujiakui
 *
 *         上午9:19:34
 *
 *         开启限流的注解
 *
 */
public @interface EnableThrottlingPolicy {

	/**
	 * 规则列表：对这里面的所有规则都要进行遍历
	 *
	 * @return
	 */
	ThrottlingRule[] rules();

	/**
	 * 如果规则对应的持久化beanName为空，则使用对应的策略上持久化规则
	 *
	 * 持久化规则对应的bean名称:要实现接口 {@link ThrottlePersistenceService}
	 *
	 * @return
	 */
	String policyPersistBeanName() default "guvvaRateLimitThrottlePersistService";

	/**
	 * 限流异常处理方法的别名,对应的方法参数类型必须是(ProceedingJoinPoint,ThrottlePolicy,ThrottleException)
	 *
	 * @return
	 */
	String excepHandleMethodAlias() default "";

}
