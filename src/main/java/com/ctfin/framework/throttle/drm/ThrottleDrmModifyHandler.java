/**
 * ThrottleDrmModifyHandler.java
 * author: yujiakui
 * 2018年1月12日
 * 下午2:45:59
 */
package com.ctfin.framework.throttle.drm;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.model.ThrottleCondition;
import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         下午2:45:59
 *
 *         限流drm修改处理器
 */
@Component
public class ThrottleDrmModifyHandler extends AbstractThrottleDrmHandler {

	/**
	 * 处理drm信息
	 *
	 * @param drmInfo
	 *            类名#属性名#规则名称#条件类型#值，类名1#属性名1#规则名称1#条件类型1#值1
	 */
	@Override
	public void handleDrm(String drmInfo) {
		// 1. 解析drm信息
		List<ThrottleDrmParseModel> throttleDrmParseModels = parse(drmInfo);
		if (CollectionUtils.isEmpty(throttleDrmParseModels)) {
			return;
		}
		// 2. 根据推送的信息进行处理
		modifyThrottlePolicy(throttleDrmParseModels);
	}

	/**
	 * 限流策略修改
	 *
	 * @param throttleDrmParseModels
	 */
	private void modifyThrottlePolicy(List<ThrottleDrmParseModel> throttleDrmParseModels) {
		Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable = getThrottlePolicyClone();
		for (ThrottleDrmParseModel throttleDrmParseModel : throttleDrmParseModels) {
			try {
				Map<Method, ThrottlePolicy> methodThrottlePolicyMap = cloneThrottlePolicyTable
						.get(throttleDrmParseModel.getClassFullName());
				if (CollectionUtils.isEmpty(methodThrottlePolicyMap)) {
					LogUtils.warn(LOGGER,
							"drm限流策略对应的类名不存在throttleDrmParseModel={0},throttlePolicyTable={1}",
							throttleDrmParseModel, cloneThrottlePolicyTable);
				}
				ThrottlePolicy throttlePolicy = getMethodThrottlePolicyClone(
						throttleDrmParseModel.getMethod(), methodThrottlePolicyMap);
				if (null == throttlePolicy) {
					LogUtils.warn(LOGGER,
							"drm限流策略对应的方法不存在throttleDrmParseModel={0},throttlePolicyTable={1}",
							throttleDrmParseModel, cloneThrottlePolicyTable);
				}

				// 限流规则
				handleThrottleRule(cloneThrottlePolicyTable, throttleDrmParseModel,
						methodThrottlePolicyMap, throttlePolicy);
			} catch (Throwable throwable) {
				LogUtils.warn(LOGGER, "drm限流修改处理有问题errMsg={0}", throwable.getMessage());
			}
		}

		// 考虑多线程所以先复制一份
		throttlingParserFactory.setMethodThrottlePolicyTable(cloneThrottlePolicyTable);
	}

	/**
	 * 处理限流规则
	 *
	 * @param cloneThrottlePolicyTable
	 * @param throttleDrmParseModel
	 * @param methodThrottlePolicyMap
	 * @param throttlePolicy
	 * @param existFlag
	 */
	private void handleThrottleRule(
			Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable,
			ThrottleDrmParseModel throttleDrmParseModel,
			Map<Method, ThrottlePolicy> methodThrottlePolicyMap, ThrottlePolicy throttlePolicy) {
		boolean existFlag = false;
		List<ThrottleRule> throttleRules = throttlePolicy.getThrottleRules();
		for (ThrottleRule throttleRule : throttleRules) {
			if (StringUtils.equals(throttleDrmParseModel.getRuleName(),
					throttleRule.getRuleName())) {
				List<ThrottleCondition> throttleConditions = throttleRule.getRuleCnds();
				for (ThrottleCondition throttleCondition : throttleConditions) {
					if (StringUtils.equals(throttleCondition.getConditionTypeEnum().getCode(),
							throttleDrmParseModel.getCondTypeStr())) {
						throttleCondition.setMaxAccessTimes(throttleDrmParseModel.getCondValue());
						existFlag = true;
						break;
					}
				}
				break;
			}
		}
		if (existFlag) {
			methodThrottlePolicyMap.put(throttleDrmParseModel.getMethod(), throttlePolicy);
		} else {
			LogUtils.warn(LOGGER,
					"drm限流策略对应的规则或者限流条件不存在throttleDrmParseModel={0},throttlePolicyTable={1}",
					throttleDrmParseModel, cloneThrottlePolicyTable);
		}
	}

}
