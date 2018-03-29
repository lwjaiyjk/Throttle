/**
 * AbstractThrottleRequestHandler.java
 * author: yujiakui
 * 2017年9月27日
 * 下午5:31:10
 */
package com.ctfin.framework.throttle.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctfin.framework.throttle.handler.ThrottleRequestHandler;

/**
 * @author yujiakui
 *
 *         下午5:31:10
 *
 */
public abstract class AbstractThrottleRequestHandler<T, S> implements ThrottleRequestHandler<T, S> {

	/** 日志 */
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractThrottleRequestHandler.class);

	/** 通用entryValue值 */
	protected static final String DEFAULT_ENTRY_VALUE = "*";

	/** 默认渠道名称是系统 */
	protected final static String DEFAULT_CHANNEL = "SYSTEM";

}
