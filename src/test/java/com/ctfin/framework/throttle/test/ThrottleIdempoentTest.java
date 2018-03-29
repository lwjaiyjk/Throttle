/**
 * ThrottleIdempoentTest.java
 * author: yujiakui
 * 2017年9月26日
 * 下午4:32:25
 */
package com.ctfin.framework.throttle.test;

import org.springframework.stereotype.Component;

/**
 * @author yujiakui
 *
 *         下午4:32:25
 *
 *         限流幂等测试
 */
@Component
// @EnableThrottlingPolicy(groupRules = {})

/*@EnableThrottlingPolicy(groupRules = { @ThrottlingGroupRule(groupRuleName = "", rules = {},
		groupAcquirerBeanName = "", groupRuleType = ThrottleGroupRuleTypeEnum.IDEMPOTENT) })
*/
/*@EnableThrottlingPolicy(groupRules = { @ThrottlingGroupRule(groupRuleName = "subgroupruleName",
		rules = { @ThrottlingRule(entry = "", conditions = {}) },
		groupAcquirerBeanName = "myThrottleValueAcquirer") })*/

/*@EnableThrottlingPolicy(groupRules = { @ThrottlingGroupRule(groupRuleName = "subgroupruleName",
		rules = { @ThrottlingRule(entry = "", conditions = {}) },
		groupAcquirerBeanName = "myThrottleValueAcquirer",
		groupRuleType = ThrottleGroupRuleTypeEnum.IDEMPOTENT) })*/

/*@EnableThrottlingPolicy(groupRules = {
		@ThrottlingGroupRule(groupRuleName = "subgroupruleName",
				rules = { @ThrottlingRule(entry = "", conditions = {
						@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.SECOND,
								maxAccessTimes = 200,
								cndAcquirerBeanName = "myThrottleValueAcquirer"),
						@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.MINUTE,
								maxAccessTimes = 10000,
								cndAcquirerBeanName = "myThrottleValueAcquirer") }) },
				groupAcquirerBeanName = "myThrottleValueAcquirer"),
		@ThrottlingGroupRule(groupRuleName = "subgroupruleName",
				rules = { @ThrottlingRule(entry = "", conditions = {
						@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.SECOND,
								maxAccessTimes = 200,
								cndAcquirerBeanName = "myThrottleValueAcquirer"),
						@ThrottlingCondition(conditionType = ThrottleConditionTypeEnum.MINUTE,
								maxAccessTimes = 10000,
								cndAcquirerBeanName = "myThrottleValueAcquirer") }) },
				groupAcquirerBeanName = "myThrottleValueAcquirer",
				groupRuleType = ThrottleGroupRuleTypeEnum.IDEMPOTENT,
				groupPersitBeanName = "myIdempoentPersistService") })*/
public class ThrottleIdempoentTest {

	// @DisableThrottlingPolicy
	public void test(String name, String sex) {
		System.out.println("----------------------name:" + name + ",sex:" + sex);
	}
}
