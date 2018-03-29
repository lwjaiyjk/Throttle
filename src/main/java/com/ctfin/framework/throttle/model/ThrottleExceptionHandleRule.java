/**
 * ThrottleExceptionHandleRule.java
 * author: yujiakui
 * 2018年3月22日
 * 下午2:34:22
 */
package com.ctfin.framework.throttle.model;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author yujiakui
 *
 *         下午2:34:22
 *
 *         限流异常处理规则
 */
public class ThrottleExceptionHandleRule implements Serializable {

	/** serial id */
	private static final long serialVersionUID = -3125104138596786580L;

	/** 异常处理方法别名 */
	private String excepHandleAlias;

	/** 类全定义名 */
	private String classFullName;

	/** 异常具体处理对应的方法 */
	private Method method;

	/**
	 * @return the excepHandleAlias
	 */
	public String getExcepHandleAlias() {
		return excepHandleAlias;
	}

	/**
	 * @param excepHandleAlias
	 *            the excepHandleAlias to set
	 */
	public void setExcepHandleAlias(String excepHandleAlias) {
		this.excepHandleAlias = excepHandleAlias;
	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ThrottleExceptionHandleRule [excepHandleAlias=");
		builder.append(excepHandleAlias);
		builder.append(", classFullName=");
		builder.append(classFullName);
		builder.append(", method=");
		builder.append(method);
		builder.append("]");
		return builder.toString();
	}

}
