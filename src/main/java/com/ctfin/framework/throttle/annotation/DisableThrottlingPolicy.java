/**
 * DisableThrottling.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:20:21
 */
package com.ctfin.framework.throttle.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * @author yujiakui
 *
 *         上午9:20:21
 *
 *         关闭限流的注解
 */
public @interface DisableThrottlingPolicy {

}
