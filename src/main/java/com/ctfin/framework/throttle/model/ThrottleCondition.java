/**
 * ThrottleCondition.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:24:00
 */
package com.ctfin.framework.throttle.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午9:24:00
 *
 *         限流条件
 */
public class ThrottleCondition implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -3215548418872550725L;

	/** 条件类型枚举,增加对应的value转换器 */
	private ThrottleConditionTypeEnum conditionTypeEnum;

	/** 条件对应的值 */
	private float maxAccessTimes;

	/** 限流条件值获取器对应的beanName */
	private String acquirerCndBeanName;

	/** 条件值获取器 */
	private ThrottleValueAcquirer cndValueAcquirer;

	/** 条件白名单 */
	private List<String> cndWhiteNames = Lists.newArrayList();

	/** 生效标记 */
	private boolean effectFlag = true;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("ThrottleCondition{");
		stringBuilder.append("conditionTypeEnum=");
		stringBuilder.append(conditionTypeEnum == null ? null : conditionTypeEnum.getCode());
		stringBuilder.append(";maxAccessTimes=");
		stringBuilder.append(maxAccessTimes);
		stringBuilder.append(";acquirerCndBeanName=");
		stringBuilder.append(acquirerCndBeanName);
		stringBuilder.append(";cndValueAcquirer=");
		stringBuilder.append(cndValueAcquirer);

		stringBuilder.append(";cndWhiteNames=[");
		if (!CollectionUtils.isEmpty(cndWhiteNames)) {
			for (String whiteName : cndWhiteNames) {
				stringBuilder.append(whiteName);
				stringBuilder.append(";");
			}
		}
		stringBuilder.append("]");
		stringBuilder.append(";effectFlag=");
		stringBuilder.append(effectFlag);
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	@Override
	public ThrottleCondition clone() throws CloneNotSupportedException {
		ThrottleCondition throttleCondition = (ThrottleCondition) super.clone();
		throttleCondition.setConditionTypeEnum(conditionTypeEnum);
		throttleCondition.setMaxAccessTimes(maxAccessTimes);
		throttleCondition.setAcquirerCndBeanName(acquirerCndBeanName);
		throttleCondition.setCndWhiteNames(Lists.newArrayList(cndWhiteNames));
		throttleCondition.setCndValueAcquirer(cndValueAcquirer);
		throttleCondition.setEffectFlag(effectFlag);
		return throttleCondition;
	}

	/**
	 * @return the conditionTypeEnum
	 */
	public ThrottleConditionTypeEnum getConditionTypeEnum() {
		return conditionTypeEnum;
	}

	/**
	 * @param conditionTypeEnum
	 *            the conditionTypeEnum to set
	 */
	public void setConditionTypeEnum(ThrottleConditionTypeEnum conditionTypeEnum) {
		this.conditionTypeEnum = conditionTypeEnum;
	}

	/**
	 * @return the acquirerCndBeanName
	 */
	public String getAcquirerCndBeanName() {
		return acquirerCndBeanName;
	}

	/**
	 * @param acquirerCndBeanName
	 *            the acquirerCndBeanName to set
	 */
	public void setAcquirerCndBeanName(String acquirerCndBeanName) {
		this.acquirerCndBeanName = acquirerCndBeanName;
	}

	/**
	 * @return the cndWhiteNames
	 */
	public List<String> getCndWhiteNames() {
		return cndWhiteNames;
	}

	/**
	 * @param cndWhiteNames
	 *            the cndWhiteNames to set
	 */
	public void setCndWhiteNames(List<String> cndWhiteNames) {
		this.cndWhiteNames = cndWhiteNames;
	}

	/**
	 * @return the cndValueAcquirer
	 */
	public ThrottleValueAcquirer getCndValueAcquirer() {
		return cndValueAcquirer;
	}

	/**
	 * @param cndValueAcquirer
	 *            the cndValueAcquirer to set
	 */
	public void setCndValueAcquirer(ThrottleValueAcquirer cndValueAcquirer) {
		this.cndValueAcquirer = cndValueAcquirer;
	}

	/**
	 * @return the maxAccessTimes
	 */
	public float getMaxAccessTimes() {
		return maxAccessTimes;
	}

	/**
	 * @param maxAccessTimes
	 *            the maxAccessTimes to set
	 */
	public void setMaxAccessTimes(float maxAccessTimes) {
		this.maxAccessTimes = maxAccessTimes;
	}

	/**
	 * @return the effectFlag
	 */
	public boolean isEffectFlag() {
		return effectFlag;
	}

	/**
	 * @param effectFlag
	 *            the effectFlag to set
	 */
	public void setEffectFlag(boolean effectFlag) {
		this.effectFlag = effectFlag;
	}

}
