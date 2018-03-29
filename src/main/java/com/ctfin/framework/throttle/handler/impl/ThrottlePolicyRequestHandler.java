/**
 * ThrottlePolicyHandler.java
 * author: yujiakui
 * 2017年9月26日
 * 上午8:29:28
 */
package com.ctfin.framework.throttle.handler.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.exception.ThrottleException;
import com.ctfin.framework.throttle.exception.ThrottleExceptionErrorEnum;
import com.ctfin.framework.throttle.exception.ThrottleExceptionSceneEnum;
import com.ctfin.framework.throttle.model.ThrottleExceptionHandleRule;
import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.parser.ThrottlingParserFactory;

/**
 * @author yujiakui
 *
 *         上午8:29:28
 *
 *         限流策略处理器
 */
@Component
public class ThrottlePolicyRequestHandler extends AbstractThrottleRequestHandler<Object, Object> {

	@Autowired
	private ThrottlingParserFactory throttlingParserFactory;

	@Autowired
	private ThrottleRuleRequestHandler throttleRuleRequestHandler;

	/**
	 * 处理
	 *
	 * @param pjp
	 * @throws Throwable
	 */
	@Override
	public Object handle(ProceedingJoinPoint pjp, Object inputParam) throws Throwable {

		// 根据签名获得方法参数
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		// Class<?> originClassObj =
		// AopProxyUtils.ultimateTargetClass(pjp.getThis());
		Class<?> originClassObj = pjp.getTarget().getClass();

		// 获得对应的拦截类名
		String classFullName = originClassObj.getName();
		// 判断对应的方法是否在需要限流的方法中
		ThrottlePolicy throttlePolicy = throttlingParserFactory.getThrottlePolicy(classFullName,
				method);
		if (null != throttlePolicy && throttlePolicy.isEffectFlag()) {

			try {
				// 得到限流规则组列表
				throttleRuleRequestHandler.handle(pjp, throttlePolicy.getThrottleRules());
			} catch (ThrottleException throttleException) {
				// 限流异常方法处理
				return handleThrottleExceptionResult(pjp, throttlePolicy, throttleException);
			}
		}

		// 无限流满足，直接调用对应的方法
		return pjp.proceed();

	}

	/**
	 * @param pjp
	 * @param throttlePolicy
	 * @param throttleException
	 * @throws Throwable
	 */
	private Object handleThrottleExceptionResult(ProceedingJoinPoint pjp,
			ThrottlePolicy throttlePolicy, ThrottleException throttleException) throws Throwable {
		// 由于限流规则导致的异常，限流条件不通过
		if (ThrottleExceptionSceneEnum.THROTTLE_NOT_PASS.getCode()
				.equals(throttleException.getSceneCode())
				&& ThrottleExceptionErrorEnum.THROTTLE_CND_NOT_PASS.getCode()
						.equals(throttleException.getErrorCode())) {
			// 获取方法对应的异常处理别名
			if (!invokeThrottleException(pjp, throttlePolicy, throttleException)) {
				// 重新跑异常
				throw throttleException;
			}
			return null;
		} else if (ThrottleExceptionSceneEnum.IDEMPOTENT.getCode()
				.equals(throttleException.getSceneCode())
				&& ThrottleExceptionErrorEnum.IDEMPOTENT_EXIST.getCode()
						.equals(throttleException.getErrorCode())) {
			// 幂等情况,如果幂等业务端要处理，可以通过抛异常来阻止返回
			invokeThrottleException(pjp, throttlePolicy, throttleException);
			// 默认返回幂等结果
			return throttleException.getResult();
		} else {
			// 其他情况，继续抛异常
			throw throttleException;
		}
	}

	/**
	 * 调用限流异常处理
	 *
	 * @param pjp
	 * @param throttlePolicy
	 * @param throttleException
	 * @throws Throwable
	 */
	private boolean invokeThrottleException(ProceedingJoinPoint pjp, ThrottlePolicy throttlePolicy,
			ThrottleException throttleException) throws Throwable {
		ThrottleRule throttleRule = throttleException.getThrottleRule();
		ThrottleExceptionHandleRule throttleExceptionHandleRule = throttlingParserFactory
				.getExcepHandleMethodMap().get(throttleRule.getExcepHandleMethodAlias());

		if (null != throttleExceptionHandleRule
				&& (null != throttleExceptionHandleRule.getMethod())) {
			Method excepHandleMethod = throttleExceptionHandleRule.getMethod();
			excepHandleMethod.setAccessible(true);
			// 调用方法当前的类
			try {
				excepHandleMethod.invoke(pjp.getTarget(), pjp, throttlePolicy, throttleException);
			} catch (InvocationTargetException invocationTargetException) {
				// 抛内部包含实际的异常
				throw invocationTargetException.getTargetException();
			}
			return true;
		} else {
			return false;
		}

	}

}
