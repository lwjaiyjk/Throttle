/**
 * ThrottleDrmParseModel.java
 * author: yujiakui
 * 2018年1月12日
 * 下午3:02:08
 */
package com.ctfin.framework.throttle.drm;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author yujiakui
 *
 *         下午3:02:08
 *
 *         限流drm解析模型
 */
public class ThrottleDrmParseModel implements Serializable {

	/** serial id */
	private static final long serialVersionUID = 5860346896596759837L;

	/** 类全名 */
	private String classFullName;

	/** 方法 */
	private Method method;

	/** 规则名称 */
	private String ruleName;

	/** 限流条件类型 */
	private String condTypeStr;

	/** 条件值 */
	private float condValue;

	/**
	 * @return the classFullName
	 */
	public String getClassFullName() {
		return classFullName;
	}

	/**
	 * @param classFullName
	 *            the classFullName to set
	 */
	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the condTypeStr
	 */
	public String getCondTypeStr() {
		return condTypeStr;
	}

	/**
	 * @param condTypeStr
	 *            the condTypeStr to set
	 */
	public void setCondTypeStr(String condTypeStr) {
		this.condTypeStr = condTypeStr;
	}

	/**
	 * @return the condValue
	 */
	public float getCondValue() {
		return condValue;
	}

	/**
	 * @param condValue
	 *            the condValue to set
	 */
	public void setCondValue(float condValue) {
		this.condValue = condValue;
	}

}
