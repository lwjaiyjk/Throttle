/**
 * ThrottleRule.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:23:07
 */
package com.ctfin.framework.throttle.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午9:23:07
 *
 *         限流规则
 */
public class ThrottleRule implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = 4589235997039105249L;

	/** 规则组条件映射,其中key对应的特定场景的值，对于通用则使用COMMON */
	private List<ThrottleCondition> ruleCnds;

	/** 规则名称：如按ip地址限流 */
	private String ruleName;

	/** 规则值获取器 {@link ThrottleValueAcquirer} 比如按照ip进行过滤等等 */
	private String ruleAcquirerBeanName;

	/** 持久化规则对应的bean名称:要实现接口 {@link ThrottlePersistenceService} */
	private String rulePersistBeanName;

	/** 对应的规则对应值得获取器：例如按IP地址限流等等 */
	private ThrottleValueAcquirer ruleValueAcquirer;

	/** 持久化对象 */
	private ThrottlePersistenceService rulePersistenceService;

	/** 规则类型枚举：限流还是幂等 */
	private ThrottleRuleTypeEnum throttleRuleTypeEnum;

	/** 规则白名单 */
	private List<String> ruleWhiteNames = Lists.newArrayList();

	/** 生效标记 */
	private boolean effectFlag = true;

	/** 异常处理方法的别名 */
	private String excepHandleMethodAlias;

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder("ThrottleRule{");
		stringBuilder.append("ruleCnds=[");
		if (!CollectionUtils.isEmpty(ruleCnds)) {
			for (ThrottleCondition throttleCondition : ruleCnds) {
				stringBuilder.append(throttleCondition);
				stringBuilder.append(";");
			}
		}
		stringBuilder.append("]");

		stringBuilder.append(";ruleName=");
		stringBuilder.append(ruleName);
		stringBuilder.append(";ruleAcquirerBeanName=");
		stringBuilder.append(ruleAcquirerBeanName);
		stringBuilder.append(";ruleValueAcquirer=");
		stringBuilder.append(ruleValueAcquirer);
		stringBuilder.append(";rulePersistBeanName=");
		stringBuilder.append(rulePersistBeanName);
		stringBuilder.append(";rulePersistenceService=");
		stringBuilder.append(rulePersistenceService);
		stringBuilder.append(";throttleRuleTypeEnum=");
		stringBuilder.append(throttleRuleTypeEnum);
		stringBuilder.append(";excepHandleMethodAlias=");
		stringBuilder.append(excepHandleMethodAlias);

		stringBuilder.append(";ruleWhiteNames=[");
		if (!CollectionUtils.isEmpty(ruleWhiteNames)) {
			for (String whiteName : ruleWhiteNames) {
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
	public ThrottleRule clone() throws CloneNotSupportedException {
		ThrottleRule throttleRule = (ThrottleRule) super.clone();
		throttleRule.setRuleCnds(Lists.newArrayList(ruleCnds));
		throttleRule.setRuleName(ruleName);
		throttleRule.setRuleValueAcquirer(ruleValueAcquirer);
		throttleRule.setRuleAcquirerBeanName(ruleAcquirerBeanName);
		throttleRule.setRuleWhiteNames(Lists.newArrayList(ruleWhiteNames));
		throttleRule.setRulePersistBeanName(rulePersistBeanName);
		throttleRule.setRulePersistenceService(rulePersistenceService);
		throttleRule.setThrottleRuleTypeEnum(throttleRuleTypeEnum);
		throttleRule.setExcepHandleMethodAlias(excepHandleMethodAlias);
		return throttleRule;
	}

	/**
	 * @return the ruleCnds
	 */
	public List<ThrottleCondition> getRuleCnds() {
		return ruleCnds;
	}

	/**
	 * @param ruleCnds
	 *            the ruleCnds to set
	 */
	public void setRuleCnds(List<ThrottleCondition> ruleCnds) {
		this.ruleCnds = ruleCnds;
	}

	/**
	 * @return the ruleWhiteNames
	 */
	public List<String> getRuleWhiteNames() {
		return ruleWhiteNames;
	}

	/**
	 * @param ruleWhiteNames
	 *            the ruleWhiteNames to set
	 */
	public void setRuleWhiteNames(List<String> ruleWhiteNames) {
		this.ruleWhiteNames = ruleWhiteNames;
	}

	/**
	 * @return the ruleAcquirerBeanName
	 */
	public String getRuleAcquirerBeanName() {
		return ruleAcquirerBeanName;
	}

	/**
	 * @param ruleAcquirerBeanName
	 *            the ruleAcquirerBeanName to set
	 */
	public void setRuleAcquirerBeanName(String ruleAcquirerBeanName) {
		this.ruleAcquirerBeanName = ruleAcquirerBeanName;
	}

	/**
	 * @return the ruleValueAcquirer
	 */
	public ThrottleValueAcquirer getRuleValueAcquirer() {
		return ruleValueAcquirer;
	}

	/**
	 * @param ruleValueAcquirer
	 *            the ruleValueAcquirer to set
	 */
	public void setRuleValueAcquirer(ThrottleValueAcquirer ruleValueAcquirer) {
		this.ruleValueAcquirer = ruleValueAcquirer;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the rulePersistBeanName
	 */
	public String getRulePersistBeanName() {
		return rulePersistBeanName;
	}

	/**
	 * @param rulePersistBeanName
	 *            the rulePersistBeanName to set
	 */
	public void setRulePersistBeanName(String rulePersistBeanName) {
		this.rulePersistBeanName = rulePersistBeanName;
	}

	/**
	 * @return the rulePersistenceService
	 */
	public ThrottlePersistenceService getRulePersistenceService() {
		return rulePersistenceService;
	}

	/**
	 * @param rulePersistenceService
	 *            the rulePersistenceService to set
	 */
	public void setRulePersistenceService(ThrottlePersistenceService rulePersistenceService) {
		this.rulePersistenceService = rulePersistenceService;
	}

	/**
	 * @return the throttleRuleTypeEnum
	 */
	public ThrottleRuleTypeEnum getThrottleRuleTypeEnum() {
		return throttleRuleTypeEnum;
	}

	/**
	 * @param throttleRuleTypeEnum
	 *            the throttleRuleTypeEnum to set
	 */
	public void setThrottleRuleTypeEnum(ThrottleRuleTypeEnum throttleRuleTypeEnum) {
		this.throttleRuleTypeEnum = throttleRuleTypeEnum;
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
