/**
 * ThrottingAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:30:33
 */
package com.ctfin.framework.throttle.parser;

/**
 * @author yujiakui
 *
 *         上午11:30:33
 *
 *         限流注解解析器
 */
public interface ThrottlingAnnotationParser<S, T> {

	/**
	 * 解析
	 *
	 * @param param
	 * @return
	 */
	public S parse(T param);

	/**
	 * 过滤对应的输入
	 *
	 * @param inputParam
	 * @return
	 */
	public boolean filter(S inputParam);
}
