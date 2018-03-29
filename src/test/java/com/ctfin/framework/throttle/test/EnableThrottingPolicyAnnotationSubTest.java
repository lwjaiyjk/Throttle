/**
 * EnableThrottingPolicyAnnotationSubTest.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:03:40
 */
package com.ctfin.framework.throttle.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.annotation.EnableThrottlingPolicy;
import com.ctfin.framework.throttle.annotation.ThrottlingCondition;
import com.ctfin.framework.throttle.annotation.ThrottlingExceptionRule;
import com.ctfin.framework.throttle.annotation.ThrottlingGroupRule;
import com.ctfin.framework.throttle.annotation.ThrottlingRule;
import com.ctfin.framework.throttle.exception.ThrottleException;
import com.ctfin.framework.throttle.model.ThrottleConditionTypeEnum;
import com.ctfin.framework.throttle.model.ThrottlePolicy;

/**
 * @author yujiakui
 *
 *         上午11:03:40
 *
 */
@Component
/*@EnableThrottlingPolicy(
		rules = { @ThrottlingRule(ruleName = "test1", conditions = {
				@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.SECOND,
						maxAccessTimes = 5, cndAcquirerBeanName = "myThrottleValueAcquirer"),
				@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.MINUTE,
						maxAccessTimes = 200, cndAcquirerBeanName = "myThrottleValueAcquirer") },
				ruleAcquirerBeanName = "myThrottleValueAcquirer") },
		excepHandleMethodAlias = "handleException")*/

@EnableThrottlingPolicy(rules = {})
public class EnableThrottingPolicyAnnotationSubTest extends EnableThrottingPolicyAnnotationTest {

	@ThrottlingGroupRule(
			rules = { @ThrottlingRule(ruleName = "test1", conditions = {
					@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.SECOND,
							maxAccessTimes = 5, cndAcquirerBeanName = "myThrottleValueAcquirer"),
					@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.MINUTE,
							maxAccessTimes = 200,
							cndAcquirerBeanName = "myThrottleValueAcquirer") },
					ruleAcquirerBeanName = "myThrottleValueAcquirer") },
			excepHandleMethodAlias = "handleException")
	public void test1(String name) {
		System.out.println("========sub======test===============" + name);
	}

	// @DisableThrottlingPolicy
	// @DisableThrottlingRule(ruleNames = { "test1", "test" })
	@ThrottlingGroupRule(
			rules = { @ThrottlingRule(ruleName = "test",
					conditions = { @ThrottlingCondition(
							conditionType = ThrottleConditionTypeEnum.SECOND, maxAccessTimes = 5,
							cndAcquirerBeanName = "myThrottleValueAcquirer") },
					ruleAcquirerBeanName = "myThrottleValueAcquirer") },
			excepHandleMethodAlias = "handleException")
	public void test(String name, String sex) {
		System.out.println("========sub======test===============" + name + " " + sex);
		this.test(name);
	}

	// @DisableThrottlingPolicy
	@ThrottlingGroupRule(rules = {})
	public void test(String name) {
		System.out.println("========sub======test===============" + name);
	}

	@ThrottlingExceptionRule(handleMethodAliasName = "handleException")
	public void handleException(ProceedingJoinPoint pj, ThrottlePolicy throttlePolicy,
			ThrottleException throttleException) {
		System.out.println("--------handleException-----------");
		System.out.println(pj);
		System.out.println(throttlePolicy);
		System.out.println(throttleException);
		throw new MyException();
	}

	/*@Override
	// @ThrottlingGroupRule(groupRuleName = "groupruleName13", rules = {})
	public void test() {
		System.out.println("=============test===============");
	}*/
}
