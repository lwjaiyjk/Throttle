/**
 * RequestHandler.java
 * author: yujiakui
 * 2017年9月27日
 * 下午5:28:57
 */
package com.ctfin.framework.throttle.handler;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author yujiakui
 *
 *         下午5:28:57
 *
 *         请求处理
 */
public interface ThrottleRequestHandler<T, S> {

	/**
	 * 处理
	 *
	 * @param pjp
	 * @param inputParam
	 * @return
	 */
	public T handle(ProceedingJoinPoint pjp, S inputParam) throws Throwable;
}
