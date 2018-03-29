/**
 * DefaultThrottleValueAcquirer.java
 * author: yujiakui
 * 2017年12月21日
 * 上午10:09:07
 */
package com.ctfin.framework.throttle.handler.impl;

import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirerResult;

/**
 * @author yujiakui
 *
 *         上午10:09:07
 *
 *         默认限流对应的值获取器
 */
@Component
public class DefaultThrottleValueAcquirer implements ThrottleValueAcquirer {

	/* (non-Javadoc)
	 * @see com.ctfin.framework.throttle.handler.ThrottleValueAcquirer#acquire(java.lang.Object[])
	 */
	@Override
	public ThrottleValueAcquirerResult acquire(Object... objects) {
		ThrottleValueAcquirerResult result = new ThrottleValueAcquirerResult();
		result.setEntryValue(AbstractThrottleRequestHandler.DEFAULT_ENTRY_VALUE);
		result.setSourceChannel(AbstractThrottleRequestHandler.DEFAULT_CHANNEL);
		return result;
	}

}
