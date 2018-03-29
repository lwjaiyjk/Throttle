/**
 * GuvvaRateLimitThrottlePersistService.java
 * author: yujiakui
 * 2017年9月26日
 * 下午1:44:53
 */
package com.ctfin.framework.throttle.persistence.impl;

import java.util.Date;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.exception.ThrottleException;
import com.ctfin.framework.throttle.exception.ThrottleExceptionErrorEnum;
import com.ctfin.framework.throttle.exception.ThrottleExceptionSceneEnum;
import com.ctfin.framework.throttle.model.ThrottleConditionTypeEnum;
import com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult;
import com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

/**
 * @author yujiakui
 *
 *         下午1:44:53
 *
 *         使用guvva实现的限流
 */
@Component
public class GuvvaRateLimitThrottlePersistService implements ThrottlePersistenceService {

	/** 日志 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GuvvaRateLimitThrottlePersistService.class);
	/** 限流guvva对应的map */
	private Map<ThrottlePersistQueryRequest, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#selectByKey(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest)
	 */
	@Override
	public ThrottlePersistControlResult selectByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest) {
		LogUtils.debug(LOGGER, "GuvvaRateLimit.selectByKey对应的map={0}", rateLimiterMap);
		RateLimiter rateLimiter = rateLimiterMap.get(throttlePersistQueryRequest);
		if (null == rateLimiter) {
			return null;
		}
		boolean acquireResultFlag = rateLimiter.tryAcquire();
		ThrottlePersistControlResult result = new ThrottlePersistControlResult();
		result.setMaxAccessTimes(rateLimiter.getRate());
		result.setLastRefillTime(new Date().getTime());
		result.setTimeUnitType(ThrottleConditionTypeEnum.SECOND.getCode());
		result.setCurAlreadyAccessTimes(0);
		result.setSuccessFlag(acquireResultFlag);
		return result;
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#save(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest, com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult)
	 */
	@Override
	public boolean save(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult) {
		// 获得对应的限流单位类型，是秒，分钟等等
		ThrottleConditionTypeEnum throttleConditionTypeEnum = ThrottleConditionTypeEnum
				.getByCode(throttlePersistControlResult.getTimeUnitType());
		double permitsPerSecond = throttlePersistControlResult.getMaxAccessTimes()
				/ throttleConditionTypeEnum.getSecondTimes();
		RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
		rateLimiterMap.put(throttlePersistQueryRequest, rateLimiter);
		return true;
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#saveWithCallbackForIdempoent(java.util.List, org.aspectj.lang.ProceedingJoinPoint)
	 */
	@Override
	public Object saveWithCallbackForIdempoent(
			ThrottlePersistQueryRequest throttlePersistQueryRequests, ProceedingJoinPoint pjp) {
		throw new ThrottleException(ThrottleExceptionSceneEnum.NOT_SHOULD_EXIST,
				ThrottleExceptionErrorEnum.INVOKE_ERROR, "不应该出现的调用异常");
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#update(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest, com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult)
	 */
	@Override
	public boolean update(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult) {

		return true;
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#deleteByKey(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest)
	 */
	@Override
	public ThrottlePersistControlResult deleteByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest) {

		RateLimiter rateLimiter = rateLimiterMap.remove(throttlePersistQueryRequest);
		if (null == rateLimiter) {
			return null;
		}
		ThrottlePersistControlResult result = new ThrottlePersistControlResult();
		result.setCurAlreadyAccessTimes(0);
		result.setLastRefillTime(new Date().getTime());
		result.setMaxAccessTimes(rateLimiter.getRate());
		result.setTimeUnitType(ThrottleConditionTypeEnum.SECOND.getCode());
		return result;

	}

}
