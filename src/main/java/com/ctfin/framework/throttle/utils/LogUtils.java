/**
 * LogUtils.java
 * author: yujiakui
 * 2017年9月25日
 * 下午2:11:08
 */
package com.ctfin.framework.throttle.utils;

import java.text.MessageFormat;

import org.slf4j.Logger;

/**
 * @author yujiakui
 *
 *         下午2:11:08
 *
 *         日志工具
 */
public class LogUtils {

	/**
	 *
	 * 打日志，并抛异常
	 *
	 * @param logger
	 * @param errorMsg
	 */
	public static void logAndThrowException(Logger logger, String errorMsg) {
		logger.debug(errorMsg);
		// 已经存在对应key的解析器，重复注册
		throw new RuntimeException(errorMsg);
	}

	/**
	 * 打印日志并抛出异常
	 *
	 * @param logger
	 * @param errorMsg
	 * @param throwable
	 */
	public static void logAndThrowException(Logger logger, String errorMsg, Throwable throwable) {
		logger.debug(errorMsg);
		// 已经存在对应key的解析器，重复注册
		throw new RuntimeException(errorMsg, throwable);
	}

	/**
	 * 打印日志
	 *
	 * @param logger
	 * @param pattern
	 * @param arguments
	 */
	public static void info(Logger logger, String pattern, Object... arguments) {
		if (logger.isInfoEnabled()) {
			String logMsg = MessageFormat.format(pattern, arguments);
			logger.info(logMsg);
		}

	}

	/**
	 * 打印日志
	 *
	 * @param logger
	 * @param pattern
	 * @param arguments
	 */
	public static void debug(Logger logger, String pattern, Object... arguments) {
		if (logger.isDebugEnabled()) {
			String logMsg = MessageFormat.format(pattern, arguments);
			logger.debug(logMsg);
		}

	}

	/**
	 * 打印日志
	 *
	 * @param logger
	 * @param pattern
	 * @param arguments
	 */
	public static void warn(Logger logger, String pattern, Object... arguments) {
		if (logger.isWarnEnabled()) {
			String logMsg = MessageFormat.format(pattern, arguments);
			logger.warn(logMsg);
		}

	}

	/**
	 * 打印日志
	 *
	 * @param logger
	 * @param pattern
	 * @param arguments
	 */
	public static void error(Logger logger, String pattern, Object... arguments) {
		if (logger.isErrorEnabled()) {
			String logMsg = MessageFormat.format(pattern, arguments);
			logger.error(logMsg);
		}

	}
}
