/**
 * EnableThrottingPolicyAnnotationTest.java
 * author: yujiakui
 * 2017年9月23日
 * 上午10:21:19
 */
package com.ctfin.framework.throttle.test;

import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.annotation.EnableThrottlingPolicy;

/**
 * @author yujiakui
 *
 *         上午10:21:19
 *
 */
@Component
/*@DisableThrottlingPolicy*/
@EnableThrottlingPolicy(rules = {})
public class EnableThrottingPolicyAnnotationTest {

	/*@ThrottlingGroupRule(groupRuleName = "groupruleName", rules = {}, groupAcquirerBeanName = "",
			groupPersitBeanName = "")*/
	public void test() {
		System.out.println("=============test===============");
	}
}
