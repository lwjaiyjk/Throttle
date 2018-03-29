/**
 * ThrottleRuleRequestHandler.java
 * author: yujiakui
 * 2017年9月27日
 * 下午4:25:45
 */
package com.ctfin.framework.throttle.handler.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ctfin.framework.throttle.exception.ThrottleException;
import com.ctfin.framework.throttle.exception.ThrottleExceptionErrorEnum;
import com.ctfin.framework.throttle.exception.ThrottleExceptionSceneEnum;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirerResult;
import com.ctfin.framework.throttle.model.ThrottleCondition;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.model.ThrottleRuleTypeEnum;
import com.ctfin.framework.throttle.persistence.ThrottlePersistControlResult;
import com.ctfin.framework.throttle.persistence.ThrottlePersistQueryRequest;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         下午4:25:45
 *
 */
@Component
public class ThrottleRuleRequestHandler
		extends AbstractThrottleRequestHandler<Object, List<ThrottleRule>> {

	@Autowired
	private ThrottleConditionRequestHandler throttleConditionRequestHandler;

	/**
	 * 限流规则处理
	 *
	 * @param pjp
	 * @param throttleRules
	 * @throws Throwable
	 */
	@Override
	public Object handle(ProceedingJoinPoint pjp, List<ThrottleRule> throttleRules)
			throws Throwable {

		// 如果throttleRules为空，则直接调用原来的方法
		if (CollectionUtils.isEmpty(throttleRules)) {
			return pjp.proceed();
		}

		// 遍历限流规则
		for (ThrottleRule throttleRule : throttleRules) {
			if (!throttleRule.isEffectFlag()) {
				// 限流规则无效则不进行处理，直接进行下一个处理
				continue;
			}
			// 单个限流规则进行处理
			handleThrottleRule(pjp, throttleRule);
		}
		return null;
	}

	/**
	 * 处理限流规则：单个规则进行处理
	 *
	 * @param pjp
	 * @param throttleRule
	 * @return
	 */
	public Object handleThrottleRule(ProceedingJoinPoint pjp, ThrottleRule throttleRule) {

		// 组装限流持久化查询请求
		List<ThrottlePersistQueryRequest> throttlePersistQueryRequests = assemblePersistQueryRequest(
				pjp, throttleRule);

		for (ThrottlePersistQueryRequest throttlePersistQueryRequest : throttlePersistQueryRequests) {
			// 处理单个限流持久化查询请求
			FunctionReturnResult functionReturnResult = handleThrottlePersistQueryRequest(
					throttleRule, throttlePersistQueryRequest, pjp);
			if (functionReturnResult.isSuccessFlag()) {
				throw new ThrottleException(ThrottleExceptionSceneEnum.IDEMPOTENT,
						ThrottleExceptionErrorEnum.IDEMPOTENT_EXIST, "幂等存在", throttleRule,
						functionReturnResult.getRetValue());
			}
		}
		return null;
	}

	/**
	 * 处理限流持久化查询请求
	 *
	 * @param throttleRule
	 * @param throttlePersistQueryRequest
	 * @return
	 */
	private FunctionReturnResult handleThrottlePersistQueryRequest(ThrottleRule throttleRule,
			ThrottlePersistQueryRequest throttlePersistQueryRequest, ProceedingJoinPoint pjp) {

		FunctionReturnResult functionReturnResult = new FunctionReturnResult();
		ThrottlePersistenceService throttlePersistenceService = throttleRule
				.getRulePersistenceService();
		ThrottlePersistControlResult throttlePersistControlResult = throttlePersistenceService
				.selectByKey(throttlePersistQueryRequest);
		if (null != throttlePersistControlResult) {
			if (ThrottleRuleTypeEnum.IDEMPOTENT.equals(throttleRule.getThrottleRuleTypeEnum())) {
				LogUtils.info(LOGGER, "幂等返回结果throttlePersistControlResult={0}",
						throttlePersistControlResult);
				functionReturnResult.setRetValue(throttlePersistControlResult.getRetValue());
				functionReturnResult.setSuccessFlag(true);
				return functionReturnResult;
			} else {
				// 如果当期访问次数大于这段时间的访问次数，则抛异常
				// TODO 更新对应的访问次数，使用RateLimiter的方式进行更新
				if (!throttlePersistControlResult.isSuccessFlag()) {
					String errorMsg = MessageFormat.format("限流条件request={0}不通过rule={1}",
							throttlePersistQueryRequest, throttleRule);
					throw new ThrottleException(ThrottleExceptionSceneEnum.THROTTLE_NOT_PASS,
							ThrottleExceptionErrorEnum.THROTTLE_CND_NOT_PASS, errorMsg,
							throttleRule, null);
				} else {
					// 限流了就不进行更新
					// 更新：具体如何更新是由对应的持久化对象自己实现
					throttlePersistenceService.update(throttlePersistQueryRequest,
							throttlePersistControlResult);
				}
			}
		} else if (ThrottleRuleTypeEnum.THROTTLE.equals(throttleRule.getThrottleRuleTypeEnum())) {
			// 首次保存，插入数据库，对于幂等要进行回调
			throttlePersistControlResult = new ThrottlePersistControlResult();
			throttlePersistControlResult.setCurAlreadyAccessTimes(1);
			throttlePersistControlResult.setLastRefillTime(new Date().getTime());
			throttlePersistControlResult
					.setMaxAccessTimes(throttlePersistQueryRequest.getMaxAccessTimes());
			throttlePersistControlResult
					.setTimeUnitType(throttlePersistQueryRequest.getUnitTimeType());
			throttlePersistenceService.save(throttlePersistQueryRequest,
					throttlePersistControlResult);
		} else if (ThrottleRuleTypeEnum.IDEMPOTENT.equals(throttleRule.getThrottleRuleTypeEnum())) {
			// 幂等的第一次
			Object idempoentResult = throttlePersistenceService
					.saveWithCallbackForIdempoent(throttlePersistQueryRequest, pjp);
			functionReturnResult.setSuccessFlag(true);
			functionReturnResult.setRetValue(idempoentResult);
		}
		return functionReturnResult;
	}

	/**
	 * 处理单个限流规则
	 *
	 * @param pjp
	 * @param throttleRule
	 * @return
	 */
	public List<ThrottlePersistQueryRequest> assemblePersistQueryRequest(ProceedingJoinPoint pjp,
			ThrottleRule throttleRule) {

		List<ThrottleCondition> throttleConditions = throttleRule.getRuleCnds();
		// 根据限流条件获得对应的限流条件请求查询参数
		List<ThrottlePersistQueryRequest> throttlePersistQueryRequests = throttleConditionRequestHandler
				.handle(pjp, throttleConditions);
		if (null == throttlePersistQueryRequests) {
			return Lists.newArrayList();
		}

		// 获取entryValue对应的值
		ThrottleValueAcquirer throttleValueAcquirer = throttleRule.getRuleValueAcquirer();
		String entryValue = DEFAULT_ENTRY_VALUE;
		String channelName = DEFAULT_CHANNEL;
		if (null != throttleValueAcquirer) {
			ThrottleValueAcquirerResult throttleValueAcquirerResult = throttleValueAcquirer
					.acquire(pjp.getArgs());
			entryValue = throttleValueAcquirerResult.getEntryValue();
			channelName = throttleValueAcquirerResult.getSourceChannel();
			// 规则白名单
			if (throttleRule.getRuleWhiteNames().contains(entryValue)) {
				return Lists.newArrayList();
			}
		}
		Class<?> targetClass = pjp.getTarget().getClass();

		for (ThrottlePersistQueryRequest throttlePersistQueryRequest : throttlePersistQueryRequests) {
			throttlePersistQueryRequest.setRuleName(throttleRule.getRuleName());
			if (StringUtils.isEmpty(throttlePersistQueryRequest.getSourceChannel())) {
				// 限流条件中的渠道覆盖规则中的渠道
				throttlePersistQueryRequest.setSourceChannel(channelName);
			}
			throttlePersistQueryRequest.setClassFullName(targetClass.getName());
			throttlePersistQueryRequest.setMethodSignaturename(pjp.getSignature().toLongString());
			throttlePersistQueryRequest.setEntryValue(entryValue);
		}

		return throttlePersistQueryRequests;

	}

	/**
	 * 内部类函数返回结果
	 *
	 * @author yujiakui
	 *
	 *         下午6:23:33
	 *
	 */
	class FunctionReturnResult {
		/** 成功标记 */
		private boolean successFlag;

		/** 返回值 */
		private Object retValue;

		public FunctionReturnResult() {
		}

		public FunctionReturnResult(boolean successFlag, Object retValue) {
			this.successFlag = successFlag;
			this.retValue = retValue;
		}

		/**
		 * @return the successFlag
		 */
		public boolean isSuccessFlag() {
			return successFlag;
		}

		/**
		 * @param successFlag
		 *            the successFlag to set
		 */
		public void setSuccessFlag(boolean successFlag) {
			this.successFlag = successFlag;
		}

		/**
		 * @return the retValue
		 */
		public Object getRetValue() {
			return retValue;
		}

		/**
		 * @param retValue
		 *            the retValue to set
		 */
		public void setRetValue(Object retValue) {
			this.retValue = retValue;
		}
	}

}
