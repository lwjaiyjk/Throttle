/**
 * ThrottlePolicy.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:22:08
 */
package com.ctfin.framework.throttle.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.persistence.ThrottlePersistenceService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午9:22:08
 *
 *         限流策略: 这个是对应在方法级别上的
 *
 */
public class ThrottlePolicy implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -8729728677419878328L;

	/** 限流规则 */
	private List<ThrottleRule> throttleRules;

	/** 持久化规则对应的bean名称:要实现接口 {@link ThrottlePersistenceService} */
	private String policyPersistBeanName;

	/** 持久化对象 */
	private ThrottlePersistenceService policyPersistenceService;

	/** 生效标记 */
	private boolean effectFlag = true;

	/** 异常处理方法的别名 */
	private String excepHandleMethodAlias;

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder("ThrottlePolicy{");
		stringBuilder.append("throttleRules=[");
		if (!CollectionUtils.isEmpty(throttleRules)) {
			for (ThrottleRule throttleRule : throttleRules) {
				stringBuilder.append(throttleRule);
				stringBuilder.append(";");
			}
		}
		stringBuilder.append("]");

		stringBuilder.append(";effectFlag=");
		stringBuilder.append(effectFlag);
		stringBuilder.append(";policyPersistBeanName=");
		stringBuilder.append(policyPersistBeanName);
		stringBuilder.append(";policyPersistenceService=");
		stringBuilder.append(policyPersistenceService);
		stringBuilder.append(";excepHandleMethodAlias=");
		stringBuilder.append(excepHandleMethodAlias);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	@Override
	public ThrottlePolicy clone() throws CloneNotSupportedException {
		ThrottlePolicy throttlePolicy = (ThrottlePolicy) super.clone();
		throttlePolicy.setThrottleRules(Lists.newArrayList(throttleRules));
		throttlePolicy.setEffectFlag(effectFlag);
		throttlePolicy.setPolicyPersistBeanName(policyPersistBeanName);
		throttlePolicy.setPolicyPersistenceService(policyPersistenceService);
		throttlePolicy.setExcepHandleMethodAlias(excepHandleMethodAlias);
		return throttlePolicy;
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
	 * @return the throttleRules
	 */
	public List<ThrottleRule> getThrottleRules() {
		return throttleRules;
	}

	/**
	 * @param throttleRules
	 *            the throttleRules to set
	 */
	public void setThrottleRules(List<ThrottleRule> throttleRules) {
		this.throttleRules = throttleRules;
	}

	/**
	 * @return the policyPersistBeanName
	 */
	public String getPolicyPersistBeanName() {
		return policyPersistBeanName;
	}

	/**
	 * @param policyPersistBeanName
	 *            the policyPersistBeanName to set
	 */
	public void setPolicyPersistBeanName(String policyPersistBeanName) {
		this.policyPersistBeanName = policyPersistBeanName;
	}

	/**
	 * @return the policyPersistenceService
	 */
	public ThrottlePersistenceService getPolicyPersistenceService() {
		return policyPersistenceService;
	}

	/**
	 * @param policyPersistenceService
	 *            the policyPersistenceService to set
	 */
	public void setPolicyPersistenceService(ThrottlePersistenceService policyPersistenceService) {
		this.policyPersistenceService = policyPersistenceService;
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
