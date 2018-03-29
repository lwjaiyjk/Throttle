/**
 * AbstractThrottingAnnotationParser.java
 * author: yujiakui
 * 2017年9月23日
 * 上午11:47:01
 */
package com.ctfin.framework.throttle.parser.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ctfin.framework.throttle.parser.ThrottlingAnnotationParser;
import com.ctfin.framework.throttle.parser.ThrottlingAnnotationParserFactory;
import com.ctfin.framework.throttle.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         上午11:47:01
 *
 */
public abstract class AbstractThrottlingAnnotationParser<S, T>
		implements ThrottlingAnnotationParser<S, T>, ApplicationContextAware {

	/** 日志 */
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractThrottlingAnnotationParser.class);

	@Autowired
	protected ThrottlingAnnotationParserFactory throttingParserFactory;

	/** spring 上下文 */
	private ApplicationContext applicationContext;

	/**
	 * 获取对应的能够解析的注解类型
	 *
	 * @return
	 */
	public abstract List<Class<T>> getParseAnnotationTypes();

	/**
	 * 从spring 上下文中获得对应的bean对象，如果不存在不抛异常
	 *
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <Q> Q getBeanWithoutException(String beanName, Class<? super Q> typeClass) {

		Q beanObj = null;
		try {
			beanObj = (Q) applicationContext.getBean(beanName, typeClass);
		} catch (NoSuchBeanDefinitionException ex) {
			LogUtils.error(LOGGER, "根据beanName={0}从spring上下文获得bean不存在{1}", beanName,
					ex.getMessage());
		} catch (BeansException beansException) {
			LogUtils.error(LOGGER, "根据beanName={0}从spring上下文获得bean发生异常{1}", beanName,
					beansException.getMessage());
		}

		return beanObj;

	}

	/*
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 过滤对应的输入
	 *
	 * @param inputParam
	 * @return
	 */
	@Override
	public boolean filter(S inputParam) {
		return false;
	}
}
