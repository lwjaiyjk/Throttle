/**
 * TestMain.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:04:17
 */
package com.ctfin.framework.throttle.test;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         上午11:04:17
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/*	AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
					ThrottleConfigure.class);*/

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.throttle");
		System.out.println(annotationConfigApplicationContext);

		EnableThrottingPolicyAnnotationSubTest enableThrottingPolicyAnnotationSubTest = annotationConfigApplicationContext
				.getBean(EnableThrottingPolicyAnnotationSubTest.class);
		/*	MyThrottleAspectAdvice throttleAspectAdvice = annotationConfigApplicationContext
					.getBean(MyThrottleAspectAdvice.class);
		System.out.println(throttleAspectAdvice);*/
		int successNum = 0;
		for (int i = 0; i <= 100; i++) {
			try {
				enableThrottingPolicyAnnotationSubTest.test("yjk", "man");
				successNum++;
			} catch (Exception exception) {
				System.out.println(exception.getMessage());
			}
			/*try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		System.out.println("------------------succcessNum-------" + successNum);

		ThrottleIdempoentTest throttleIdempoentTest = annotationConfigApplicationContext
				.getBean(ThrottleIdempoentTest.class);
		for (int i = 0; i < 10; i++) {
			try {
				throttleIdempoentTest.test("yjk", "woman");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * 克隆限流策略table
	 *
	 * @param throttlePolicyTable
	 * @return
	 */
	private static Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable(
			Map<String, Map<Method, ThrottlePolicy>> throttlePolicyTable) {
		Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable = Maps.newHashMap();
		for (Map.Entry<String, Map<Method, ThrottlePolicy>> throttlePolicyTableEntry : throttlePolicyTable
				.entrySet()) {
			cloneThrottlePolicyTable.put(throttlePolicyTableEntry.getKey(),
					Maps.newHashMap(throttlePolicyTableEntry.getValue()));
		}
		return cloneThrottlePolicyTable;
	}

	private void test1() {
		System.out.println("------------");
	}

}
