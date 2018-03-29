/**
 * MethodFilterUtil.java
 * author: yujiakui
 * 2017年9月23日
 * 下午5:36:27
 */
package com.ctfin.framework.throttle.utils;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         下午5:36:27
 *
 *         方法过滤工具
 */
public class MethodFilterUtil {

	/** 过滤的方法列表 */
	public static final List<String> excludeFilterMethods = Lists.newArrayList();

	static {
		excludeFilterMethods.add("wait");
		excludeFilterMethods.add("equals");
		excludeFilterMethods.add("toString");
		excludeFilterMethods.add("hashCode");
		excludeFilterMethods.add("getClass");
		excludeFilterMethods.add("notify");
		excludeFilterMethods.add("notifyAll");
	}

	/**
	 * 对方法进行过滤，返回true表示不用处理，直接过滤掉
	 *
	 * @param method
	 * @return
	 */
	public static boolean filterMethod(Method method) {
		if (excludeFilterMethods.contains(method.getName())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 对方法进行过滤，返回true表示不用处理，直接过滤掉
	 *
	 * @param method
	 * @return
	 */
	public static boolean filterMethod(ProceedingJoinPoint pjp) {
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		return filterMethod(method);
	}
}
