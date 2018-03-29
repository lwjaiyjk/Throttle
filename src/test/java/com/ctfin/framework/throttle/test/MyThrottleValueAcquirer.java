/**
 * MyThrottleValueAcquirer.java
 * author: yujiakui
 * 2017年9月26日
 * 下午3:26:35
 */
package com.ctfin.framework.throttle.test;

import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.handler.ThrottleValueAcquirer;
import com.ctfin.framework.throttle.handler.ThrottleValueAcquirerResult;

/**
 * @author yujiakui
 *
 *         下午3:26:35
 *
 */
@Component
public class MyThrottleValueAcquirer implements ThrottleValueAcquirer {

	/*
	 * @see com.ctfin.framework.throttle.handler.ThrottleValueAcquirer#acquire(java.lang.Object[])
	 */
	public ThrottleValueAcquirerResult acquire(Object... objects) {
		ThrottleValueAcquirerResult throttleValueAcquirerResult = new ThrottleValueAcquirerResult();
		throttleValueAcquirerResult.setSourceChannel("SYSTEM");
		throttleValueAcquirerResult.setEntryValue("");
		return throttleValueAcquirerResult;
	}

}
