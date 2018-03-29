/**
 * ThrottleGroupRuleValueAcquirer.java
 * author: yujiakui
 * 2017年9月25日
 * 下午4:00:06
 */
package com.ctfin.framework.throttle.handler;

/**
 * @author yujiakui
 *
 *         下午4:00:06
 *
 *         限流组规则对应值得获取器
 */
public interface ThrottleValueAcquirer {

	/**
	 * 获取器：主要这个acquire的参数对应的就是需要限流对应的参数
	 *
	 * @param objects
	 * @return
	 */
	public ThrottleValueAcquirerResult acquire(Object... objects);
}
