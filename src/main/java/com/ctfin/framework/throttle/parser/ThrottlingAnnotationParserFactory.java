/**
 * ThrottleParserFactory.java
 * author: yujiakui
 * 2017年9月23日
 * 上午10:57:34
 */
package com.ctfin.framework.throttle.parser;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.ctfin.framework.throttle.parser.impl.AbstractThrottlingAnnotationParser;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         上午10:57:34
 *
 *         限流解析工厂
 */
@Component
public class ThrottlingAnnotationParserFactory
		implements InitializingBean, ApplicationContextAware {

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ThrottlingAnnotationParserFactory.class);

	/** 解析器工厂 */
	@SuppressWarnings("rawtypes")
	private Map<Object, AbstractThrottlingAnnotationParser> parserMap = Maps.newHashMap();

	private ApplicationContext applicationContext;

	/*
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@SuppressWarnings({ "rawtypes" })
	public void afterPropertiesSet() throws Exception {
		Map<String, AbstractThrottlingAnnotationParser> throttleAnnotationParserMap = applicationContext
				.getBeansOfType(AbstractThrottlingAnnotationParser.class);
		Map<Object, AbstractThrottlingAnnotationParser> existParserMap = Maps.newHashMap(parserMap);
		for (Map.Entry<String, AbstractThrottlingAnnotationParser> entryEle : throttleAnnotationParserMap
				.entrySet()) {
			regist(entryEle.getValue(), existParserMap);
		}
		parserMap = existParserMap;
	}

	/**
	 * 根据class对象查询对应的解析器
	 *
	 * @param classObj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ThrottlingAnnotationParser getParserByClass(Class<?> classObj) {
		ThrottlingAnnotationParser parser = parserMap.get(classObj);
		if (null == parser) {
			String errorMsg = MessageFormat.format("限流注解key={0}对应的解析器不存在map={1}", classObj,
					parserMap);
			LogUtils.logAndThrowException(LOGGER, errorMsg);
		}
		return parser;
	}

	/**
	 * 注册注解解析器
	 *
	 * @param key
	 * @param parser
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void regist(AbstractThrottlingAnnotationParser parser,
			Map<Object, AbstractThrottlingAnnotationParser> existParserMap) {

		List<Object> annotationKeys = parser.getParseAnnotationTypes();
		for (Object key : annotationKeys) {
			ThrottlingAnnotationParser existParser = existParserMap.get(key);
			if (null == existParser) {
				existParserMap.put(key, parser);
			} else {
				if (parser != existParser) {
					String errorMsg = MessageFormat.format(
							"限流注解解析器重复key={0},parser={1},existParser={2}", key, parser,
							existParser);
					LogUtils.logAndThrowException(LOGGER, errorMsg);
				}
			}
		}

	}

	/*
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
