/**
 * ThrottlePersistControlResult.java
 * author: yujiakui
 * 2017年9月26日
 * 上午10:56:46
 */
package com.ctfin.framework.throttle.persistence;

import java.io.Serializable;

import com.ctfin.framework.throttle.model.ThrottleConditionTypeEnum;

/**
 * @author yujiakui
 *
 *         上午10:56:46
 *
 *         限流持久化控制结果
 */
public class ThrottlePersistControlResult implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -3073839394037291322L;

	/** 上次填充时间 */
	private long lastRefillTime;

	/** 最大访问次数 */
	private double maxAccessTimes;

	/** 当前已经访问次数 */
	private double curAlreadyAccessTimes;

	/** 时间单位类型 {@link ThrottleConditionTypeEnum} */
	private String timeUnitType;

	/** 返回值对象：对于幂等使用的 */
	private Object retValue;

	/** 成功标记 */
	private boolean successFlag;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("ThrottlePersistControlResult{");
		stringBuilder.append("lastRefillTime=");
		stringBuilder.append(lastRefillTime);
		stringBuilder.append(",maxAccessTimes=");
		stringBuilder.append(maxAccessTimes);
		stringBuilder.append(",curAlreadyAccessTimes=");
		stringBuilder.append(curAlreadyAccessTimes);
		stringBuilder.append(",timeUnitType=");
		stringBuilder.append(timeUnitType);
		stringBuilder.append(",retValue=");
		stringBuilder.append(retValue);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	/**
	 * @return the lastRefillTime
	 */
	public long getLastRefillTime() {
		return lastRefillTime;
	}

	/**
	 * @param lastRefillTime
	 *            the lastRefillTime to set
	 */
	public void setLastRefillTime(long lastRefillTime) {
		this.lastRefillTime = lastRefillTime;
	}

	/**
	 * @return the maxAccessTimes
	 */
	public double getMaxAccessTimes() {
		return maxAccessTimes;
	}

	/**
	 * @param maxAccessTimes
	 *            the maxAccessTimes to set
	 */
	public void setMaxAccessTimes(double maxAccessTimes) {
		this.maxAccessTimes = maxAccessTimes;
	}

	/**
	 * @return the curAlreadyAccessTimes
	 */
	public double getCurAlreadyAccessTimes() {
		return curAlreadyAccessTimes;
	}

	/**
	 * @param curAlreadyAccessTimes
	 *            the curAlreadyAccessTimes to set
	 */
	public void setCurAlreadyAccessTimes(double curAlreadyAccessTimes) {
		this.curAlreadyAccessTimes = curAlreadyAccessTimes;
	}

	/**
	 * @return the timeUnitType
	 */
	public String getTimeUnitType() {
		return timeUnitType;
	}

	/**
	 * @param timeUnitType
	 *            the timeUnitType to set
	 */
	public void setTimeUnitType(String timeUnitType) {
		this.timeUnitType = timeUnitType;
	}

	/**
	 * @return the retValue
	 */
	public Object getRetValue() {
		return retValue;
	}

	/**
	 * @param retValue
	 *            the retValue to set
	 */
	public void setRetValue(Object retValue) {
		this.retValue = retValue;
	}

	/**
	 * @return the successFlag
	 */
	public boolean isSuccessFlag() {
		return successFlag;
	}

	/**
	 * @param successFlag
	 *            the successFlag to set
	 */
	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}

}
