/**
 * ThrottlePolicyAnnotationAdvice.java
 * author: yujiakui
 * 2017年9月25日
 * 下午2:44:58
 */
package com.ctfin.framework.throttle.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.handler.impl.ThrottlePolicyRequestHandler;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.ctfin.framework.throttle.utils.MethodFilterUtil;

/**
 * @author yujiakui
 *
 *         下午2:44:58
 *
 *         限流注解拦截器
 */
@Aspect
@Component
public class ThrottlePolicyAnnotationAdvice {

	/** logger */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ThrottlePolicyAnnotationAdvice.class);

	@Autowired
	private ThrottlePolicyRequestHandler throttlePolicyRequestHandler;

	@Pointcut("@within(com.ctfin.framework.throttle.annotation.EnableThrottlingPolicy)")
	public void aspectjMethod() {
	}

	/**
	 * Around 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
	 *
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "aspectjMethod()")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		// 对方法进行过滤
		if (MethodFilterUtil.filterMethod(pjp)) {
			return pjp.proceed();
		} else {
			LogUtils.debug(LOGGER, "限流拦截器开始pjp={0}", pjp.toShortString());
			return throttlePolicyRequestHandler.handle(pjp, null);
		}
	}

}
