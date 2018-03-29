/**
 * ThrottingPolicyAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 下午2:12:57
 */
package com.ctfin.framework.throttle.parser.impl;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ctfin.framework.throttle.annotation.EnableThrottlingPolicy;
import com.ctfin.framework.throttle.annotation.ThrottlingRule;
import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.model.ThrottleRuleTypeEnum;
import com.ctfin.framework.throttle.parser.ThrottlingAnnotationParser;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author yujiakui
 *
 *         下午2:12:57
 *
 *         限流策略注解解析器
 */
@Component
public class ThrottlingPolicyAnnotationParser
		extends AbstractThrottlingAnnotationParser<ThrottlePolicy, EnableThrottlingPolicy> {

	/** 拆分生成 */
	private final static String SPLIT_GEN = "_SPLIT_GEN";

	/*
	 * @see com.ctfin.framework.throttle.parser.ThrottingAnnotationParser#parse(java.lang.Object)
	 */
	@Override
	public ThrottlePolicy parse(EnableThrottlingPolicy param) {

		ThrottlePolicy throttlePolicy = new ThrottlePolicy();
		throttlePolicy.setPolicyPersistBeanName(param.policyPersistBeanName());
		throttlePolicy.setPolicyPersistenceService(getBeanWithoutException(
				param.policyPersistBeanName(), ThrottlePersistenceService.class));
		throttlePolicy.setExcepHandleMethodAlias(param.excepHandleMethodAlias());
		throttlePolicy.setThrottleRules(parserRule(param.rules(), throttlePolicy));
		return throttlePolicy;
	}

	/**
	 * 解析策略组规则
	 *
	 * @param throttingGroupRules
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<ThrottleRule> parserRule(ThrottlingRule[] throttingRules,
			ThrottlePolicy throttlePolicy) {
		ThrottlingAnnotationParser parser = throttingParserFactory
				.getParserByClass(ThrottlingRule.class);

		Set<String> throttleNames = Sets.newHashSet();
		List<ThrottleRule> throttleRules = Lists.newArrayList();
		for (ThrottlingRule throttingRule : throttingRules) {
			ThrottleRule throttleRule = (ThrottleRule) parser.parse(throttingRule);
			// 如果规则对应的异常处理为空，则使用策略上的
			if (StringUtils.isEmpty(throttleRule.getExcepHandleMethodAlias())) {
				throttleRule.setExcepHandleMethodAlias(throttlePolicy.getExcepHandleMethodAlias());
			}
			if (StringUtils.isEmpty(throttleRule.getRulePersistBeanName())) {
				if (StringUtils.isEmpty(throttlePolicy.getPolicyPersistBeanName())
						|| null == throttlePolicy.getPolicyPersistenceService()) {
					// 规则和策略上都是空，则报错
					LogUtils.logAndThrowException(LOGGER,
							MessageFormat.format(
									"限流持久化对应的beanName为空或者对应的持久化bean不存在policy={0},rule={1}",
									throttlePolicy, throttleRule));
				}
				throttleRule.setRulePersistBeanName(throttlePolicy.getPolicyPersistBeanName());
				throttleRule
						.setRulePersistenceService(throttlePolicy.getPolicyPersistenceService());
			}
			// 相同ruleName的将会报错
			if (throttleNames.contains(throttleRule.getRuleName())) {
				// 如果存在则报错
				LogUtils.logAndThrowException(LOGGER, MessageFormat
						.format("相同规则名称存在不止一次throttleRuleName={0}", throttleRule.getRuleName()));
			} else if (ThrottleRuleTypeEnum.IDEMPOTENT
					.equals(throttleRule.getThrottleRuleTypeEnum())) {
				// 幂等的情况下拆分为两个：一个是没有条件的幂等规则，一个是使用幂等条件的限流规则
				ThrottleRule cloneThrottleRule = cloneThrottleRule(throttleRule);
				cloneThrottleRule.setThrottleRuleTypeEnum(ThrottleRuleTypeEnum.THROTTLE);
				cloneThrottleRule.setRuleName(cloneThrottleRule.getRuleName() + SPLIT_GEN);
				throttleRule.setRuleCnds(null);
				throttleNames.add(throttleRule.getRuleName());
				throttleRules.add(throttleRule);
				throttleRules.add(cloneThrottleRule);
			} else {
				throttleRules.add(throttleRule);
			}
		}

		return throttleRules;
	}

	/**
	 * 克隆限流规则
	 *
	 * @param throttleRule
	 * @return
	 */
	private ThrottleRule cloneThrottleRule(ThrottleRule throttleRule) {
		ThrottleRule cloneThrottleRule = null;
		try {
			cloneThrottleRule = throttleRule.clone();
		} catch (CloneNotSupportedException e) {
			LogUtils.logAndThrowException(LOGGER,
					MessageFormat.format("限流规则clone失败throttleRule={0}", throttleRule), e);
		}
		return cloneThrottleRule;
	}

	/*
	 * @see com.ctfin.framework.throttle.parser.impl.AbstractThrottingAnnotationParser#getParseAnnotationTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<EnableThrottlingPolicy>> getParseAnnotationTypes() {
		return Lists.newArrayList(EnableThrottlingPolicy.class);
	}

	/* (non-Javadoc)
	 * @see com.ctfin.framework.throttle.parser.ThrottlingAnnotationParser#filter(java.lang.Object)
	 */
	@Override
	public boolean filter(ThrottlePolicy inputParam) {
		// 失效不进行过滤，因为有些方法可能就是不进行限流了，对应的策略为失效状态
		if (!inputParam.isEffectFlag()) {
			return false;
		}
		List<ThrottleRule> throttleRules = inputParam.getThrottleRules();
		if (CollectionUtils.isEmpty(throttleRules)) {
			return true;
		}
		ThrottlingAnnotationParser parser = throttingParserFactory
				.getParserByClass(ThrottlingRule.class);
		Iterator<ThrottleRule> iterator = throttleRules.iterator();
		while (iterator.hasNext()) {
			ThrottleRule throttleRule = iterator.next();
			if (parser.filter(throttleRule)) {
				iterator.remove();
				continue;
			}
		}

		return CollectionUtils.isEmpty(throttleRules);
	}

}
