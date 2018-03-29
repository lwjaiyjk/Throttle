/**
 * ThrottlePersistQueryRequest.java
 * author: yujiakui
 * 2017年9月26日
 * 上午10:27:47
 */
package com.ctfin.framework.throttle.persistence;

import java.io.Serializable;

/**
 * @author yujiakui
 *
 *         上午10:27:47
 *
 *         限流持久化查询请求
 */
public class ThrottlePersistQueryRequest implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = 2956588423411103909L;

	/** 来源渠道 */
	private String sourceChannel;

	/** 类型:时间单位类型 */
	private String unitTimeType;

	/** 类全称 */
	private String classFullName;

	/** 方法签名名称 */
	private String methodSignaturename;

	/** 入口值 */
	private String entryValue;

	/** 最大访问次数 */
	private float maxAccessTimes;

	/** 规则入口 */
	private String ruleName;

	/**
	 * 优化hashcode：如果一个类是非可变的，并且计算散列码的代价也比较大，
	 * 那么你应该考虑把散列码缓存在对象内部，而不是每次请求的时候都重新计算散列码。 如果你觉得这种类型的大多数对象会被用做散列键，那么你应该在实例被创建
	 * 的时候就计算散列码。否则的话，你可以选择“迟缓初始化”散列码，一直 到hashCode被第一次调用的时候才初始化
	 */
	private volatile int hashcode = 0;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("ThrottlePersistQueryRequest{");
		stringBuilder.append("sourceChannel=");
		stringBuilder.append(sourceChannel);
		stringBuilder.append(",unitTimeType=");
		stringBuilder.append(unitTimeType);
		stringBuilder.append(",classFullName=");
		stringBuilder.append(classFullName);
		stringBuilder.append(",methodSignaturename=");
		stringBuilder.append(methodSignaturename);
		stringBuilder.append(",entryValue=");
		stringBuilder.append(entryValue);
		stringBuilder.append(",maxAccessTimes=");
		stringBuilder.append(maxAccessTimes);
		stringBuilder.append(",ruleName=");
		stringBuilder.append(ruleName);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof ThrottlePersistQueryRequest)) {
			return false;
		}
		ThrottlePersistQueryRequest throttlePersistQueryRequest = (ThrottlePersistQueryRequest) object;

		if (!stringEquals(sourceChannel, throttlePersistQueryRequest.getSourceChannel())) {
			return false;
		}
		if (!stringEquals(unitTimeType, throttlePersistQueryRequest.getUnitTimeType())) {
			return false;
		}
		if (!stringEquals(classFullName, throttlePersistQueryRequest.getClassFullName())) {
			return false;
		}
		if (!stringEquals(methodSignaturename,
				throttlePersistQueryRequest.getMethodSignaturename())) {
			return false;
		}
		if (!stringEquals(entryValue, throttlePersistQueryRequest.getEntryValue())) {
			return false;
		}
		if (maxAccessTimes != throttlePersistQueryRequest.getMaxAccessTimes()) {
			return false;
		}
		if (!stringEquals(ruleName, throttlePersistQueryRequest.getRuleName())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		if (hashcode == 0) {
			int result = 11;
			result = result * 17 + (sourceChannel == null ? 0 : sourceChannel.hashCode());
			result = result * 17 + (unitTimeType == null ? 0 : unitTimeType.hashCode());
			result = result * 17 + (classFullName == null ? 0 : classFullName.hashCode());
			result = result * 17
					+ (methodSignaturename == null ? 0 : methodSignaturename.hashCode());
			result = result * 17 + (entryValue == null ? 0 : entryValue.hashCode());
			result = result * 17 + (int) maxAccessTimes;
			result = result * 17 + (ruleName == null ? 0 : ruleName.hashCode());
			hashcode = result;
		}

		return hashcode;
	}

	/**
	 * 字符串判断
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	private boolean stringEquals(String str1, String str2) {
		if (str1 == null) {
			if (null == str2) {
				return true;
			} else {
				return false;
			}
		} else {
			return str1.equals(str2);
		}
	}

	/**
	 * @return the sourceChannel
	 */
	public String getSourceChannel() {
		return sourceChannel;
	}

	/**
	 * @param sourceChannel
	 *            the sourceChannel to set
	 */
	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	/**
	 * @return the classFullName
	 */
	public String getClassFullName() {
		return classFullName;
	}

	/**
	 * @param classFullName
	 *            the classFullName to set
	 */
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

	/**
	 * @return the methodSignaturename
	 */
	public String getMethodSignaturename() {
		return methodSignaturename;
	}

	/**
	 * @param methodSignaturename
	 *            the methodSignaturename to set
	 */
	public void setMethodSignaturename(String methodSignaturename) {
		this.methodSignaturename = methodSignaturename;
	}

	/**
	 * @return the entryValue
	 */
	public String getEntryValue() {
		return entryValue;
	}

	/**
	 * @param entryValue
	 *            the entryValue to set
	 */
	public void setEntryValue(String entryValue) {
		this.entryValue = entryValue;
	}

	/**
	 * @return the unitTimeType
	 */
	public String getUnitTimeType() {
		return unitTimeType;
	}

	/**
	 * @param unitTimeType
	 *            the unitTimeType to set
	 */
	public void setUnitTimeType(String unitTimeType) {
		this.unitTimeType = unitTimeType;
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
	 * @return the hashcode
	 */
	public int getHashcode() {
		return hashcode;
	}

	/**
	 * @param hashcode
	 *            the hashcode to set
	 */
	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}

}
