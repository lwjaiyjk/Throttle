/**
 * MyIdempoentPersistService.java
 * author: yujiakui
 * 2017年9月26日
 * 下午5:31:46
 */
package com.ctfin.framework.throttle.test;

import java.util.Date;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult;
import com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         下午5:31:46
 *
 */
@Component
public class MyIdempoentPersistService implements ThrottlePersistenceService {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(MyIdempoentPersistService.class);

	private Map<ThrottlePersistQueryRequest, ThrottlePersistControlResult> requestResultMap = Maps
			.newHashMap();

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#selectByKey(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest)
	 */
	@Override
	public ThrottlePersistControlResult selectByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest) {
		LOGGER.info(
				"--------MyIdempoentPersistService selectByKey ---------------" + requestResultMap);
		return requestResultMap.get(throttlePersistQueryRequest);
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#save(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest, com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult)
	 */
	@Override
	public boolean save(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult) {
		LOGGER.info("------------MyIdempoentPersistService save------------");
		requestResultMap.put(throttlePersistQueryRequest, throttlePersistControlResult);
		LOGGER.info("-----------map----------" + requestResultMap);
		return true;
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#saveWithCallbackForIdempoent(java.util.List, org.aspectj.lang.ProceedingJoinPoint)
	 */
	@Override
	public Object saveWithCallbackForIdempoent(
			ThrottlePersistQueryRequest throttlePersistQueryRequest, ProceedingJoinPoint pjp) {
		LOGGER.info(
				"------------MyIdempoentPersistService saveWithCallbackForIdempoent------------");
		try {
			Object object = pjp.proceed();
			ThrottlePersistControlResult result = new ThrottlePersistControlResult();
			result.setCurAlreadyAccessTimes(0);
			result.setLastRefillTime(new Date().getTime());
			result.setMaxAccessTimes(throttlePersistQueryRequest.getMaxAccessTimes());
			result.setRetValue(object);
			result.setSuccessFlag(true);
			result.setTimeUnitType(throttlePersistQueryRequest.getUnitTimeType());
			save(throttlePersistQueryRequest, result);

			return object;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#update(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest, com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult)
	 */
	@Override
	public boolean update(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult) {
		LOGGER.info("------------MyIdempoentPersistService update------------");
		return false;
	}

	/*
	 * @see com.ctfin.framework.throttle.persistence.ThrottlePersistenceService#deleteByKey(com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest)
	 */
	@Override
	public ThrottlePersistControlResult deleteByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest) {
		return null;
	}

}
