/**
 * ThrottingRuleAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:40:26
 */
package com.ctfin.framework.throttle.parser.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ctfin.framework.throttle.annotation.ThrottlingCondition;
import com.ctfin.framework.throttle.annotation.ThrottlingRule;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.model.ThrottleCondition;
import com.ctfin.framework.throttle.model.ThrottleConditionTypeEnum;
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
 *         上午11:40:26
 *
 *         限流规则解析器
 */
@Component
public class ThrottlingRuleAnnotationParser
		extends AbstractThrottlingAnnotationParser<ThrottleRule, ThrottlingRule> {

	/*
	 * @see com.ctfin.framework.throttle.parser.ThrottingAnnotationParser#parse(java.lang.Object)
	 */
	@Override
	public ThrottleRule parse(ThrottlingRule param) {
		ThrottleRule throttleRule = new ThrottleRule();

		List<ThrottleCondition> throttleConditions = parseRuleCondition(param.conditions());
		throttleRule.setRuleCnds(throttleConditions);
		if (StringUtils.isEmpty(param.ruleName())) {
			LogUtils.logAndThrowException(LOGGER, MessageFormat.format("规则名称不能为空param={0}", param));
		}
		throttleRule.setRuleName(param.ruleName());
		throttleRule.setRuleAcquirerBeanName(param.ruleAcquirerBeanName());
		throttleRule.setRuleValueAcquirer(
				getBeanWithoutException(param.ruleAcquirerBeanName(), ThrottleValueAcquirer.class));
		throttleRule.setRuleWhiteNames(Lists.newArrayList(param.ruleWhiteNames()));
		throttleRule.setRulePersistBeanName(param.rulePersistBeanName());
		throttleRule.setRulePersistenceService(getBeanWithoutException(param.rulePersistBeanName(),
				ThrottlePersistenceService.class));
		throttleRule.setThrottleRuleTypeEnum(param.ruleType());
		throttleRule.setExcepHandleMethodAlias(param.excepHandleMethodAlias());
		return throttleRule;
	}

	/**
	 * 解析规则中对应的条件
	 *
	 * @param throttlingRule
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<ThrottleCondition> parseRuleCondition(ThrottlingCondition[] throttingConditions) {

		List<ThrottleCondition> throttleConditions = Lists.newLinkedList();
		ThrottlingAnnotationParser parser = throttingParserFactory
				.getParserByClass(ThrottlingCondition.class);
		Set<ThrottleConditionTypeEnum> throttleConditionTypeEnums = Sets.newHashSet();
		for (ThrottlingCondition throttingCondition : throttingConditions) {
			if (!throttleConditionTypeEnums.contains(throttingCondition.conditionType())) {
				throttleConditionTypeEnums.add(throttingCondition.conditionType());
			} else {
				LogUtils.logAndThrowException(LOGGER,
						MessageFormat.format("同一个规则里面有两个条件对应的类型一样conditionType={0}",
								throttingCondition.conditionType()));
			}
			throttleConditions.add((ThrottleCondition) parser.parse(throttingCondition));
		}
		return throttleConditions;
	}

	/*
	 * @see com.ctfin.framework.throttle.parser.impl.AbstractThrottingAnnotationParser#getParseAnnotationTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<ThrottlingRule>> getParseAnnotationTypes() {
		return Lists.newArrayList(ThrottlingRule.class);
	}

	/* (non-Javadoc)
	 * @see com.ctfin.framework.throttle.parser.ThrottlingAnnotationParser#filter(java.lang.Object)
	 */
	@Override
	public boolean filter(ThrottleRule inputParam) {
		if (ThrottleRuleTypeEnum.THROTTLE.equals(inputParam.getThrottleRuleTypeEnum())) {
			return CollectionUtils.isEmpty(inputParam.getRuleCnds());
		}
		return false;

	}

}
