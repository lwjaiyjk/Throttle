/**
 * ThrottleGroupRule.java
 * author: yujiakui
 * 2017年9月23日
 * 下午2:25:10
 */
package com.ctfin.framework.throttle.model;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         下午2:25:10
 *
 *         组规则
 */
public class ThrottleGroupRule implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -4675803207504718422L;

	/** 限流规则map，其中key对应的rule的entry，而且这里面的只会匹配一个 */
	private Map<String, ThrottleRule> throttleRuleMap;

	/** 异常处理方法的别名 */
	private String excepHandleMethodAlias;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("ThrottleGroupRule{");
		stringBuilder.append("throttleRuleMap=[");
		if (null != throttleRuleMap && !throttleRuleMap.isEmpty()) {
			for (Map.Entry<String, ThrottleRule> entryEle : throttleRuleMap.entrySet()) {
				stringBuilder.append("entryValue=");
				stringBuilder.append(entryEle.getKey());
				stringBuilder.append(",throttleRule=");
				stringBuilder.append(entryEle.getValue());
				stringBuilder.append(";");
			}
		}
		stringBuilder.append(";excepHandleMethodAlias");
		stringBuilder.append(excepHandleMethodAlias);
		stringBuilder.append("]");

		return stringBuilder.toString();
	}

	@Override
	public ThrottleGroupRule clone() {
		ThrottleGroupRule throttleGroupRule = new ThrottleGroupRule();
		throttleGroupRule.setThrottleRuleMap(Maps.newHashMap(throttleRuleMap));
		throttleGroupRule.setExcepHandleMethodAlias(excepHandleMethodAlias);
		return throttleGroupRule;
	}

	/**
	 * @return the throttleRuleMap
	 */
	public Map<String, ThrottleRule> getThrottleRuleMap() {
		return throttleRuleMap;
	}

	/**
	 * @param throttleRuleMap
	 *            the throttleRuleMap to set
	 */
	public void setThrottleRuleMap(Map<String, ThrottleRule> throttleRuleMap) {
		this.throttleRuleMap = throttleRuleMap;
	}

	/**
	 * @return the excepHandleMethodAlias
	 */
	public String getExcepHandleMethodAlias() {
		return excepHandleMethodAlias;
	}

	/**
	 * @param excepHandleMethodAlias
	 *            the excepHandleMethodAlias to set
	 */
	public void setExcepHandleMethodAlias(String excepHandleMethodAlias) {
		this.excepHandleMethodAlias = excepHandleMethodAlias;
	}

}
