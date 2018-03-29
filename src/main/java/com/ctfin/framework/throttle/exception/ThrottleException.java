/**
 * ThrottleException.java
 * author: yujiakui
 * 2017年9月26日
 * 上午11:10:34
 */
package com.ctfin.framework.throttle.exception;

import com.ctfin.framework.throttle.model.ThrottleRule;

/**
 * @author yujiakui
 *
 *         上午11:10:34
 *
 *         限流对应的异常
 */
public class ThrottleException extends RuntimeException {

	/** serial id */
	private static final long serialVersionUID = -1336547895796917969L;

	/** 场景码 */
	private String sceneCode;

	/** 错误码 */
	private String errorCode;

	/** 限流规则 */
	private ThrottleRule throttleRule = null;

	/** 结果 */
	private Object result = null;

	/**
	 * @param sceneCode
	 * @param errorCode
	 * @param errorMsg
	 */
	public ThrottleException(String sceneCode, String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
		this.sceneCode = sceneCode;
	}

	/**
	 * @param sceneCode
	 * @param errorCode
	 * @param errorMsg
	 */
	public ThrottleException(ThrottleExceptionSceneEnum sceneEnum,
			ThrottleExceptionErrorEnum errorEnum, String errorMsg) {
		super(errorMsg);
		errorCode = errorEnum.getCode();
		sceneCode = sceneEnum.getCode();
	}

	/**
	 * @param sceneEnum
	 * @param errorEnum
	 * @param errorMsg
	 * @param throttleRule
	 * @param result
	 */
	public ThrottleException(ThrottleExceptionSceneEnum sceneEnum,
			ThrottleExceptionErrorEnum errorEnum, String errorMsg, ThrottleRule throttleRule,
			Object result) {
		super(errorMsg);
		errorCode = errorEnum.getCode();
		sceneCode = sceneEnum.getCode();
		this.throttleRule = throttleRule;
		this.result = result;
	}

	/**
	 * @param sceneCode
	 * @param errorCode
	 * @param errorMsg
	 */
	public ThrottleException(String sceneCode, String errorCode, String errorMsg,
			Throwable throwable) {
		super(errorMsg, throwable);
		this.errorCode = errorCode;
		this.sceneCode = sceneCode;
	}

	/**
	 * @return the sceneCode
	 */
	public String getSceneCode() {
		return sceneCode;
	}

	/**
	 * @param sceneCode
	 *            the sceneCode to set
	 */
	public void setSceneCode(String sceneCode) {
		this.sceneCode = sceneCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the throttleRule
	 */
	public ThrottleRule getThrottleRule() {
		return throttleRule;
	}

	/**
	 * @param throttleRule
	 *            the throttleRule to set
	 */
	public void setThrottleRule(ThrottleRule throttleRule) {
		this.throttleRule = throttleRule;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThrottleException [sceneCode=");
		builder.append(sceneCode);
		builder.append(", errorCode=");
		builder.append(errorCode);
		builder.append(", throttleRule=");
		builder.append(throttleRule);
		builder.append(", result=");
		builder.append(result);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
