/**
 * ThrottleConditionRequestHandler.java
 * author: yujiakui
 * 2017年9月26日
 * 上午8:43:04
 */
package com.ctfin.framework.throttle.handler.impl;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirerResult;
import com.ctfin.framework.throttle.model.ThrottleCondition;
import com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午8:43:04
 *
 *         限流请求条件处理器
 */
@Component
public class ThrottleConditionRequestHandler extends
		AbstractThrottleRequestHandler<List<ThrottlePersistQueryRequest>, List<ThrottleCondition>> {

	/**
	 * 处理
	 *
	 * @param pjp
	 * @param throttleConditions
	 */
	@Override
	public List<ThrottlePersistQueryRequest> handle(ProceedingJoinPoint pjp,
			List<ThrottleCondition> throttleConditions) {
		List<ThrottlePersistQueryRequest> queryRequests = Lists.newArrayList();
		if (CollectionUtils.isEmpty(throttleConditions)) {
			return queryRequests;
		}

		// 对限流条件进行处理
		for (ThrottleCondition throttleCondition : throttleConditions) {
			if (!throttleCondition.isEffectFlag()) {
				// 限流条件失效则进行下一个处理
				continue;
			}
			ThrottlePersistQueryRequest throttlePersistQueryRequest = handleThrottleCondition(pjp,
					throttleCondition);

			if (null != throttleCondition) {
				queryRequests.add(throttlePersistQueryRequest);
			}

		}
		return queryRequests;
	}

	/**
	 * 处理限流条件
	 *
	 * @param pjp
	 * @param throttleCondition
	 */
	private ThrottlePersistQueryRequest handleThrottleCondition(ProceedingJoinPoint pjp,
			ThrottleCondition throttleCondition) {

		// 根据beanName从对应的Spring上下文中获得对应的bean对象
		ThrottleValueAcquirer throttleValueAcquirerForCnd = throttleCondition.getCndValueAcquirer();
		String channelName = DEFAULT_CHANNEL;
		if (null != throttleValueAcquirerForCnd) {
			ThrottleValueAcquirerResult throttleValueAcquirerResult = throttleValueAcquirerForCnd
					.acquire(pjp.getArgs());
			String entryValue = throttleValueAcquirerResult.getEntryValue();
			channelName = throttleValueAcquirerResult.getSourceChannel();

			// 判断是否在白名单,如果在直接返回
			if (throttleCondition.getCndWhiteNames().contains(entryValue)) {
				return null;
			}

		}

		// 组装查询redis中对应的key
		ThrottlePersistQueryRequest queryRequest = new ThrottlePersistQueryRequest();

		queryRequest.setUnitTimeType(throttleCondition.getConditionTypeEnum().getCode());
		queryRequest.setMaxAccessTimes(throttleCondition.getMaxAccessTimes());
		queryRequest.setSourceChannel(channelName);

		return queryRequest;
	}

}
