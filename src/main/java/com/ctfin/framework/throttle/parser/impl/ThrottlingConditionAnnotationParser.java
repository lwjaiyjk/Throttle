/**
 * ThrottleConditionAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:33:24
 */
package com.ctfin.framework.throttle.parser.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.annotation.ThrottlingCondition;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.model.ThrottleCondition;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午11:33:24
 *
 *         限流条件注解解析器
 */
@Component
public class ThrottlingConditionAnnotationParser
		extends AbstractThrottlingAnnotationParser<ThrottleCondition, ThrottlingCondition> {

	/*
	 * @see com.ctfin.framework.throttle.parser.ThrottingAnnotationParser#parse(java.lang.Object)
	 */
	@Override
	public ThrottleCondition parse(ThrottlingCondition param) {
		ThrottleCondition throttleCondition = new ThrottleCondition();
		throttleCondition.setConditionTypeEnum(param.conditionType());
		throttleCondition.setMaxAccessTimes(param.maxAccessTimes());
		throttleCondition.setAcquirerCndBeanName(param.cndAcquirerBeanName());
		throttleCondition.setCndValueAcquirer(
				getBeanWithoutException(param.cndAcquirerBeanName(), ThrottleValueAcquirer.class));
		if (null != param.cndWhiteNames()) {
			throttleCondition.setCndWhiteNames(Lists.newArrayList(param.cndWhiteNames()));
		}
		return throttleCondition;
	}

	/*
	 * @see com.ctfin.framework.throttle.parser.impl.AbstractThrottingAnnotationParser#getParseAnnotationTypes()
	 */
	@Override
	public List<Class<ThrottlingCondition>> getParseAnnotationTypes() {
		return Lists.newArrayList(ThrottlingCondition.class);
	}

}
