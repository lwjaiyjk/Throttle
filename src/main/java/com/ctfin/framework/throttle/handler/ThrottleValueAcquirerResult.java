/**
 * ThrottleValueAcquirerResult.java
 * author: yujiakui
 * 2017年9月26日
 * 上午10:09:14
 */
package com.ctfin.framework.throttle.handler;

import java.io.Serializable;

/**
 * @author yujiakui
 *
 *         上午10:09:14
 *
 *         限流值请求获取结果
 */
public class ThrottleValueAcquirerResult implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -3002047242488958574L;

	/** 对应的结果值 */
	private String entryValue;

	/** 来源渠道 */
	private String sourceChannel;

	public ThrottleValueAcquirerResult() {
	}

	public ThrottleValueAcquirerResult(String entryValue, String sourceChannel) {
		this.entryValue = entryValue;
		this.sourceChannel = sourceChannel;
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

}
