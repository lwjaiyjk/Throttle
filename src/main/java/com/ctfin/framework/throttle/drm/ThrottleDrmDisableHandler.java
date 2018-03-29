/**
 * ThrottleDrmDisableHandler.java
 * author: yujiakui
 * 2018年1月10日
 * 下午4:18:36
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

/**
 * @author yujiakui
 *
 *         下午4:18:36
 *
 *         限流drm 失效处理器
 */
@Component
public class ThrottleDrmDisableHandler extends AbstractThrottleDrmHandler {

	/** 表示所有的进行推送 */
	private final static String ALL = "*";

	/**
	 * 处理drm推送信息
	 *
	 * @param drmInfo
	 *            类名#方法全名#规则名称%条件类型1&条件类型2，规则名称2%条件类型1&条件类型2^类名 2.。。
	 */
	@Override
	public void handleDrm(String drmInfo) {
		// 1. 解析drm信息
		List<ThrottleDrmParseModel> throttleDrmParseModels = parse(drmInfo);
		if (CollectionUtils.isEmpty(throttleDrmParseModels)) {
			return;
		}

		// 2. 失效限流表
		disableThrottleTable(throttleDrmParseModels);
	}

	/**
	 * 失效限流表
	 *
	 * @param throttleDrmParseModels
	 */
	private void disableThrottleTable(List<ThrottleDrmParseModel> throttleDrmParseModels) {

		Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable = getThrottlePolicyClone();
		for (ThrottleDrmParseModel throttleDrmParseModel : throttleDrmParseModels) {
			// 处理单个限流失效模型
			disableSingleThrottleDrmModel(throttleDrmParseModel, cloneThrottlePolicyTable);
		}
		throttlingParserFactory.setMethodThrottlePolicyTable(cloneThrottlePolicyTable);
	}

	/**
	 * 失效单个限流模型
	 *
	 * @param throttleDrmParseModel
	 * @param cloneThrottlePolicyTable
	 */
	private void disableSingleThrottleDrmModel(ThrottleDrmParseModel throttleDrmParseModel,
			Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable) {

		// 所有的类对应的限流规则都失效
		if (StringUtils.equals(throttleDrmParseModel.getClassFullName(), ALL)) {
			for (Map<Method, ThrottlePolicy> methodPolicyMap : cloneThrottlePolicyTable.values()) {
				for (ThrottlePolicy throttlePolicy : methodPolicyMap.values()) {
					throttlePolicy.setEffectFlag(false);
				}
			}
		} else if (null == throttleDrmParseModel.getMethod()) {
			// 对于类中所有的方法失效
			Map<Method, ThrottlePolicy> classMethodPolicy = cloneThrottlePolicyTable
					.get(throttleDrmParseModel.getClassFullName());
			for (ThrottlePolicy throttlePolicy : classMethodPolicy.values()) {
				throttlePolicy.setEffectFlag(false);
			}
		} else {
			// 限流类和方法对应的规则
			disableClassMethodRule(throttleDrmParseModel, cloneThrottlePolicyTable);
		}
	}

	/**
	 * 限流类和方法对应的规则
	 *
	 * @param throttleDrmParseModel
	 * @param cloneThrottlePolicyTable
	 */
	private void disableClassMethodRule(ThrottleDrmParseModel throttleDrmParseModel,
			Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable) {
		// 获取方法对应的限流策略
		ThrottlePolicy methodThrottlePolicy = getMethodThrottlePolicyClone(
				throttleDrmParseModel.getMethod(),
				cloneThrottlePolicyTable.get(throttleDrmParseModel.getClassFullName()));
		List<ThrottleRule> methodThrottleRules = methodThrottlePolicy.getThrottleRules();
		// 是否方法所有的规则都失效
		boolean allMethodRuleDisable = false;
		if (StringUtils.equals(ALL, throttleDrmParseModel.getRuleName())) {
			allMethodRuleDisable = true;
		}
		boolean methodRuleDisable = false;
		for (ThrottleRule throttleRule : methodThrottleRules) {
			// 所有的规则都失效
			if (allMethodRuleDisable) {
				throttleRule.setEffectFlag(false);
				methodRuleDisable = true;
				continue;
			}
			// 特定规则失效
			if (StringUtils.equals(throttleRule.getRuleName(),
					throttleDrmParseModel.getRuleName())) {
				// 失效对应的条件
				List<ThrottleCondition> throttleConditions = throttleRule.getRuleCnds();
				boolean allConditionDisable = false;
				if (StringUtils.equals(ALL, throttleDrmParseModel.getCondTypeStr())) {
					allConditionDisable = true;
				}
				for (ThrottleCondition throttleCondition : throttleConditions) {
					// 所有条件失效
					if (allConditionDisable) {
						throttleCondition.setEffectFlag(false);
						methodRuleDisable = true;
						continue;
					}
					// 特定条件失效
					if (StringUtils.equals(throttleCondition.getConditionTypeEnum().getCode(),
							throttleDrmParseModel.getCondTypeStr())) {
						// 失效条件
						throttleCondition.setEffectFlag(false);
						methodRuleDisable = true;
					}
				}
				break;
			}
		}

		// 重置方法对应的限流规则
		if (methodRuleDisable) {
			Map<Method, ThrottlePolicy> methodThrottlePolicyMap = cloneThrottlePolicyTable
					.get(throttleDrmParseModel.getClassFullName());
			methodThrottlePolicyMap.put(throttleDrmParseModel.getMethod(), methodThrottlePolicy);
		}
	}

}
