/**
 * ThrottingGroupRuleAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 下午2:29:26
 */
package com.ctfin.framework.throttle.parser.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.annotation.ThrottlingGroupRule;
import com.ctfin.framework.throttle.annotation.ThrottlingRule;
import com.ctfin.framework.throttle.model.ThrottleGroupRule;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.parser.ThrottlingAnnotationParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         下午2:29:26
 *
 *         限流组规则注解解析器
 */
@Component
public class ThrottlingGroupRuleAnnotationParser
		extends AbstractThrottlingAnnotationParser<ThrottleGroupRule, ThrottlingGroupRule> {

	/*
	 * @see com.ctfin.framework.throttle.parser.ThrottingAnnotationParser#parse(java.lang.Object)
	 */
	@Override
	public ThrottleGroupRule parse(ThrottlingGroupRule param) {
		ThrottleGroupRule throttleGroupRule = new ThrottleGroupRule();

		throttleGroupRule.setExcepHandleMethodAlias(param.excepHandleMethodAlias());
		throttleGroupRule.setThrottleRuleMap(
				parseThrottleRule(param.rules(), param.excepHandleMethodAlias()));

		return throttleGroupRule;
	}

	/**
	 * 解析注解规则
	 *
	 * @param throttlingRules
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, ThrottleRule> parseThrottleRule(ThrottlingRule[] throttlingRules,
			String grpExcepHandleAlias) {

		Map<String, ThrottleRule> throttleRuleMap = Maps.newHashMap();
		ThrottlingAnnotationParser parser = throttingParserFactory
				.getParserByClass(ThrottlingRule.class);
		for (ThrottlingRule throttlingRule : throttlingRules) {
			ThrottleRule throttleRule = (ThrottleRule) parser.parse(throttlingRule);
			if (StringUtils.isEmpty(throttleRule.getExcepHandleMethodAlias())) {
				throttleRule.setExcepHandleMethodAlias(grpExcepHandleAlias);
			}
			throttleRuleMap.put(throttleRule.getRuleName(), throttleRule);
		}
		return throttleRuleMap;
	}

	/*
	 * @see com.ctfin.framework.throttle.parser.impl.AbstractThrottingAnnotationParser#getParseAnnotationTypes()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Class<ThrottlingGroupRule>> getParseAnnotationTypes() {
		return Lists.newArrayList(ThrottlingGroupRule.class);
	}

}
