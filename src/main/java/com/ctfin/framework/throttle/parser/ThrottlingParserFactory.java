/**
 * ThrottlingParserFactory.java
 * author: yujiakui
 * 2017年9月23日
 * 下午4:03:00
 */
package com.ctfin.framework.throttle.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ctfin.framework.throttle.annotation.DisableThrottlingPolicy;
import com.ctfin.framework.throttle.annotation.DisableThrottlingRule;
import com.ctfin.framework.throttle.annotation.EnableThrottlingPolicy;
import com.ctfin.framework.throttle.annotation.ThrottlingExceptionRule;
import com.ctfin.framework.throttle.annotation.ThrottlingGroupRule;
import com.ctfin.framework.throttle.exception.ThrottleException;
import com.ctfin.framework.throttle.exception.ThrottleExceptionErrorEnum;
import com.ctfin.framework.throttle.exception.ThrottleExceptionSceneEnum;
import com.ctfin.framework.throttle.model.ThrottleExceptionHandleRule;
import com.ctfin.framework.throttle.model.ThrottleGroupRule;
import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.ctfin.framework.throttle.model.ThrottleRule;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.ctfin.framework.throttle.utils.MethodFilterUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author yujiakui
 *
 *         下午4:03:00
 *
 */
@Component
public class ThrottlingParserFactory implements InitializingBean, ApplicationContextAware {

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory.getLogger(ThrottlingParserFactory.class);

	/** spring 上下文 */
	private ApplicationContext applicationContext;

	/** 表示所有的接口名称 */
	public final static Method ALL_METHOD = null;

	/** 方法级别对应的限流策略 ,rowkey=对应的类对象Class全名，columnKey=对应的是方法 */
	private Map<String, Map<Method, ThrottlePolicy>> methodThrottlePolicyTable = Maps.newHashMap();

	/** 限流异常处理方法的table，rowKey=方法别名， colKey=类全名，value=对应的处理方法 */
	private Map<String, ThrottleExceptionHandleRule> excepHandleMethodMap = Maps.newHashMap();

	/** 限流注解解析器工厂 */
	@Autowired
	private ThrottlingAnnotationParserFactory throttlingAnnotationParserFactory;

	/**
	 * 获取对应方法的限流策略
	 *
	 * @param classFullName
	 * @param method
	 * @return
	 */
	public ThrottlePolicy getThrottlePolicy(String classFullName, Method method) {
		// 根据类名获得对应的限流策略
		Map<Method, ThrottlePolicy> throttlePolicyMap = methodThrottlePolicyTable
				.get(classFullName);

		// 根据方法名获得对应的限流策略
		ThrottlePolicy throttlePolicy = throttlePolicyMap.get(method);
		// 对应的特定方法限流规则不存在，则使用通用的
		if (null == throttlePolicy) {
			// 返回这个类对应的通用的策略
			return throttlePolicyMap.get(ALL_METHOD);
		} else {
			return throttlePolicy;
		}
	}

	/*
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/*
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> throttleBeanMap = applicationContext
				.getBeansWithAnnotation(EnableThrottlingPolicy.class);
		// 清空
		methodThrottlePolicyTable.clear();

		Iterator<Map.Entry<String, Object>> iterator = throttleBeanMap.entrySet().iterator();
		while (iterator.hasNext()) {

			Map.Entry<String, Object> entryEle = iterator.next();
			// 因为被拦截器拦住，使用cglib进行代理，所有需要获得对应的原始类
			Class<?> originBeanClass = AopProxyUtils.ultimateTargetClass(entryEle.getValue());

			// 过滤点失效的限流对象
			if (isIncludeDisableThrottlePolicy(originBeanClass)) {
				continue;
			}

			// 解析类上面对应的政策注解
			parseThrottlePolicyForClass(originBeanClass);

			// 解析类中方法对应的规则
			parseMethodThrottleRule(originBeanClass);

			// 对组规则按照对应的组规则类型的weight进行降序排序
			boolean removeFlag = sortAndCheckGrpRulesByWeight(
					methodThrottlePolicyTable.get(originBeanClass.getName()));
			if (removeFlag) {
				iterator.remove();
				continue;
			}
		}

		LOGGER.info(MessageFormat.format("根据限流注解得到的整个限流政策规则table={0},exceptionHanlerMap={1}",
				methodThrottlePolicyTable, excepHandleMethodMap));
	}

	/**
	 * 对限流策略中对应的组规则进行排序(按照组规则类型的权重进行排序)
	 *
	 * @param throttlePolicyMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean sortAndCheckGrpRulesByWeight(Map<Method, ThrottlePolicy> throttlePolicyMap) {

		Iterator<Map.Entry<Method, ThrottlePolicy>> it = throttlePolicyMap.entrySet().iterator();
		ThrottlingAnnotationParser throttlePolicyParser = throttlingAnnotationParserFactory
				.getParserByClass(EnableThrottlingPolicy.class);
		while (it.hasNext()) {

			Map.Entry<Method, ThrottlePolicy> entryEle = it.next();
			// 过滤对应的策略
			if (throttlePolicyParser.filter(entryEle.getValue())) {
				it.remove();
				continue;
			}

			List<ThrottleRule> throttleRules = entryEle.getValue().getThrottleRules();

			Collections.sort(throttleRules, new Comparator<ThrottleRule>() {

				@Override
				public int compare(ThrottleRule o1, ThrottleRule o2) {
					return o2.getThrottleRuleTypeEnum().getWeight()
							- o1.getThrottleRuleTypeEnum().getWeight();
				}
			});

		}

		return CollectionUtils.isEmpty(throttlePolicyMap);

	}

	/**
	 * 判读对应的类是否包含DisableThrottingPolicy注解
	 *
	 * @param classObj
	 * @return
	 */
	private boolean isIncludeDisableThrottlePolicy(Class<?> classObj) {
		// 过滤点失效的限流对象
		Annotation[] beanAnnotations = classObj.getAnnotations();
		boolean includeDisablePolicy = false;
		for (Annotation annotation : beanAnnotations) {
			if (annotation instanceof DisableThrottlingPolicy) {
				includeDisablePolicy = true;
				break;
			}
		}

		return includeDisablePolicy;
	}

	/**
	 * 解析每一个类上面对应的政策注解
	 *
	 * @param beanClass
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseThrottlePolicyForClass(Class<?> beanClass) {
		// 获取对应的解析器
		ThrottlingAnnotationParser parser = throttlingAnnotationParserFactory
				.getParserByClass(EnableThrottlingPolicy.class);
		// 获取限流政策注解
		EnableThrottlingPolicy enableThrottlingPolicy = beanClass
				.getAnnotation(EnableThrottlingPolicy.class);
		// 解析政策注解
		ThrottlePolicy throttlePolicy = (ThrottlePolicy) parser.parse(enableThrottlingPolicy);

		// 如果不包含则插入，否则报错
		if (null == methodThrottlePolicyTable.get(beanClass.getName())) {
			Map<Method, ThrottlePolicy> throttlePolicyMap = Maps.newHashMap();
			throttlePolicyMap.put(ALL_METHOD, throttlePolicy);
			methodThrottlePolicyTable.put(beanClass.getName(), throttlePolicyMap);
		} else {
			// 如果已经包含，则报错
			String errorMsg = MessageFormat.format("上下文中对应的beanClass={0}存在的政策多于1个(包括继承过来的)",
					beanClass.getName());
			LogUtils.logAndThrowException(LOGGER, errorMsg);
		}
	}

	/**
	 * 解析方法上的限流规则
	 *
	 * @param classObj
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseMethodThrottleRule(Class<?> classObj) {
		Method[] methods = classObj.getMethods();
		for (Method method : methods) {
			// 排除一些方法，排除Object对应的方法，仅仅需要public方法
			if (MethodFilterUtil.filterMethod(method)) {
				continue;
			}
			// 方法对应限流解析
			if (!parseMethodDisableThrottlePolicyAnnotation(classObj, method)) {
				// disable不存在，才解析方法上的限流组规则注解
				parseMethodThrottleGroupRuleAnnotation(classObj, method);
				// 解析方法对应的失效规则，判断注解 DisableThrottingRule
				parseMethodDisableThrottingRule(classObj, method);
			}

			// 判断方法是否是是对应限流异常处理方法
			parseMethodExceptionHandleAlias(classObj, method);
		}
	}

	/**
	 * 解析判断对应的方法是否是限流异常处理方法
	 *
	 * @param classObj
	 * @param method
	 */
	private void parseMethodExceptionHandleAlias(Class<?> classObj, Method method) {

		ThrottlingExceptionRule throttlingExceptionRule = method
				.getAnnotation(ThrottlingExceptionRule.class);
		if (null != throttlingExceptionRule) {
			// 校验限流异常处理对应的方法参数是否满足ProceedingJoinPoint,ThrottlePolicy,ThrottleException
			checkThrottleExceptionMethodParam(method);

			String aliasName = throttlingExceptionRule.handleMethodAliasName();
			if (StringUtils.isEmpty(aliasName)) {
				String errorMsg = MessageFormat.format("类{0}对应的方法{1}对应的限流异常处理方法的别名为空",
						classObj.getName(), method.getName());
				throw new ThrottleException(ThrottleExceptionSceneEnum.NOT_SHOULD_EXIST,
						ThrottleExceptionErrorEnum.PARSER_ERROR, errorMsg);
			}

			ThrottleExceptionHandleRule throttleExceptionHandleRule = excepHandleMethodMap
					.get(aliasName);

			if (null != throttleExceptionHandleRule) {
				// 说明同一个别名的限流规则已经存在了
				String errorMsg = MessageFormat.format("别名{0}对应的处理方法多于一个{1},[类名{2},方法名{3}]",
						aliasName, throttleExceptionHandleRule, classObj.getName(),
						method.getName());
				throw new ThrottleException(ThrottleExceptionSceneEnum.NOT_SHOULD_EXIST,
						ThrottleExceptionErrorEnum.PARSER_ERROR, errorMsg);
			}
			throttleExceptionHandleRule = new ThrottleExceptionHandleRule();
			throttleExceptionHandleRule.setExcepHandleAlias(aliasName);
			throttleExceptionHandleRule.setClassFullName(classObj.getName());
			throttleExceptionHandleRule.setMethod(method);

			// 将对应的异常处理规则放入map中
			excepHandleMethodMap.put(aliasName, throttleExceptionHandleRule);
		}
	}

	/**
	 * 校验限流异常处理对应的方法参数是否满足ProceedingJoinPoint,ThrottlePolicy,ThrottleException
	 *
	 * @param method
	 */
	private void checkThrottleExceptionMethodParam(Method method) {
		// 判断方法的参数是否满足顺序和类型（ProceedingJoinPoint,ThrottlePolicy,ThrottleException）
		Class<?>[] methodParamTypes = method.getParameterTypes();
		if (null != methodParamTypes && 3 == methodParamTypes.length) {
			if (methodParamTypes[0].equals(ProceedingJoinPoint.class)
					&& methodParamTypes[1].equals(ThrottlePolicy.class)
					&& methodParamTypes[2].equals(ThrottleException.class)) {
				return;
			}
		}

		String errorMsg = MessageFormat.format(
				"限流幂等异常处理对应的方法参数类型不符合要求(ProceedingJoinPoint,ThrottlePolicy,ThrottleException),其对应的是{0}",
				methodParamTypes);
		throw new ThrottleException(ThrottleExceptionSceneEnum.NOT_SHOULD_EXIST,
				ThrottleExceptionErrorEnum.PARSER_ERROR, errorMsg);
	}

	/**
	 * 解析注解 DisableThrottingRule 删除对应的ruleName的注解规则
	 *
	 * @param classObj
	 * @param method
	 */
	private void parseMethodDisableThrottingRule(Class<?> classObj, Method method) {
		DisableThrottlingRule disableThrottlingRule = method
				.getAnnotation(DisableThrottlingRule.class);
		if (null == disableThrottlingRule || null == disableThrottlingRule.ruleNames()
				|| 0 == disableThrottlingRule.ruleNames().length) {
			return;
		}

		Set<String> disableRuleNames = Sets.newHashSet(disableThrottlingRule.ruleNames());
		// 获得对应类名的限流策略
		Map<Method, ThrottlePolicy> throttlePolicyMap = methodThrottlePolicyTable
				.get(classObj.getName());
		ThrottlePolicy throttlePolicy = getThrottlePolicyByMethod(classObj, method);
		ThrottlePolicy cloneThrottlePolicy = cloneThrottlePolicy(throttlePolicy);
		List<ThrottleRule> throttleRules = cloneThrottlePolicy.getThrottleRules();
		Iterator<ThrottleRule> iterator = throttleRules.iterator();
		while (iterator.hasNext()) {
			ThrottleRule throttleRule = iterator.next();
			if (disableRuleNames.contains(throttleRule.getRuleName())) {
				iterator.remove();
			}
		}
		if (CollectionUtils.isEmpty(throttleRules)) {
			// 限流规则为空则将其设置为失效
			cloneThrottlePolicy.setEffectFlag(false);
		}

		throttlePolicyMap.put(method, cloneThrottlePolicy);
	}

	/**
	 * 限流策略clone
	 *
	 * @param throttlePolicy
	 * @return
	 */
	private ThrottlePolicy cloneThrottlePolicy(ThrottlePolicy throttlePolicy) {

		ThrottlePolicy cloneThrottlePolicy = null;
		try {
			cloneThrottlePolicy = throttlePolicy.clone();

		} catch (CloneNotSupportedException e) {
			LogUtils.logAndThrowException(LOGGER,
					MessageFormat.format("策略对象clone失败policy={0}", throttlePolicy), e);
		}
		return cloneThrottlePolicy;
	}

	/**
	 * 根据类名和方法名获得对应的限流策略
	 *
	 * @param classObj
	 * @param method
	 * @return
	 */
	private ThrottlePolicy getThrottlePolicyByMethod(Class<?> classObj, Method method) {
		// 获得对应类名的限流策略
		Map<Method, ThrottlePolicy> throttlePolicyMap = methodThrottlePolicyTable
				.get(classObj.getName());
		ThrottlePolicy throttlePolicy = throttlePolicyMap.get(method);
		if (null == throttlePolicy) {
			throttlePolicy = throttlePolicyMap.get(ALL_METHOD);
			if (null == throttlePolicy) {
				String errorMsg = MessageFormat.format("className={0}对应的通用接口*的政策为null",
						classObj.getName());
				LogUtils.logAndThrowException(LOGGER, errorMsg);
			}
		}
		return throttlePolicy;
	}

	/**
	 * 解析方法上的DisableThrottlingPolicy注解
	 *
	 * @param classObj
	 * @param method
	 * @return 是否存在DisableThrottlingPolicy
	 */
	private boolean parseMethodDisableThrottlePolicyAnnotation(Class<?> classObj, Method method) {
		DisableThrottlingPolicy disableThrottlingPolicy = method
				.getAnnotation(DisableThrottlingPolicy.class);
		if (null == disableThrottlingPolicy) {
			// 表示不存在
			return false;
		}

		// 获得对应类名的限流策略
		ThrottlePolicy throttlePolicy = getThrottlePolicyByMethod(classObj, method);
		ThrottlePolicy cloneThrottlePolicy = cloneThrottlePolicy(throttlePolicy);
		// 设置方法对应的限流策略失效
		cloneThrottlePolicy.setEffectFlag(false);
		Map<Method, ThrottlePolicy> throttlePolicyMap = methodThrottlePolicyTable
				.get(classObj.getName());
		throttlePolicyMap.put(method, cloneThrottlePolicy);

		return true;
	}

	/**
	 * 解析方法上的限流组规则注解
	 *
	 * @param classObj
	 * @param method
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseMethodThrottleGroupRuleAnnotation(Class<?> classObj, Method method) {
		ThrottlingGroupRule throttlingGroupRule = method.getAnnotation(ThrottlingGroupRule.class);
		if (null != throttlingGroupRule) {
			ThrottlingAnnotationParser parser = throttlingAnnotationParserFactory
					.getParserByClass(ThrottlingGroupRule.class);
			ThrottleGroupRule throttleGroupRule = (ThrottleGroupRule) parser
					.parse(throttlingGroupRule);

			Map<Method, ThrottlePolicy> throttlePolicyMap = methodThrottlePolicyTable
					.get(classObj.getName());
			ThrottlePolicy throttlePolicy = throttlePolicyMap.get(method);
			if (null == throttlePolicy) {
				ThrottlePolicy commonThrottlePolicy = throttlePolicyMap.get(ALL_METHOD);
				if (null == commonThrottlePolicy) {
					String errorMsg = MessageFormat.format("className={0}对应的通用接口*的政策为null",
							classObj.getName());
					LogUtils.logAndThrowException(LOGGER, errorMsg);
				}

				// 替换重复的组规则名称
				ThrottlePolicy cloneThrottlePolicy = replaceRepeatGrpRuleName(throttleGroupRule,
						commonThrottlePolicy);

				throttlePolicyMap.put(method, cloneThrottlePolicy);

			} else {
				// 报错
				String errorMsg = MessageFormat.format("classname={0},methodName={1}对应的限流政策已经存在",
						classObj.getName(), method.getName());
				LogUtils.logAndThrowException(LOGGER, errorMsg);

			}

		}
	}

	/**
	 * 替换重复的组规则名称
	 *
	 * @param throttleGroupRule
	 * @param commonThrottlePolicy
	 */
	private ThrottlePolicy replaceRepeatGrpRuleName(ThrottleGroupRule throttleGroupRule,
			ThrottlePolicy commonThrottlePolicy) {

		// 政策clone
		ThrottlePolicy cloneThrottlePolicy = cloneThrottlePolicy(commonThrottlePolicy);
		List<ThrottleRule> throttleRules = cloneThrottlePolicy.getThrottleRules();
		if (null == throttleRules) {
			throttleRules = Lists.newArrayList();
		}

		Map<String, ThrottleRule> methodThrottleRuleMap = throttleGroupRule.getThrottleRuleMap();
		Iterator<ThrottleRule> throttleRuleIterator = throttleRules.iterator();
		while (throttleRuleIterator.hasNext()) {
			ThrottleRule tmpThrottleRule = throttleRuleIterator.next();
			if (null != methodThrottleRuleMap.get(tmpThrottleRule.getRuleName())) {
				throttleRuleIterator.remove();
			}
		}
		// 方法组规则继承类政策
		methodThrottleRuleMap.forEach((ruleName, throttleRule) -> {
			if (throttleRule.getRulePersistBeanName().isEmpty()) {
				throttleRule
						.setRulePersistBeanName(commonThrottlePolicy.getPolicyPersistBeanName());
				throttleRule.setRulePersistenceService(
						commonThrottlePolicy.getPolicyPersistenceService());
			}
			if (StringUtils.isEmpty(throttleRule.getExcepHandleMethodAlias())) {
				throttleRule.setExcepHandleMethodAlias(
						commonThrottlePolicy.getExcepHandleMethodAlias());
			}
		});
		throttleRules.addAll(methodThrottleRuleMap.values());
		cloneThrottlePolicy.setThrottleRules(throttleRules);
		return cloneThrottlePolicy;
	}

	/**
	 * @return the methodThrottlePolicyTable
	 */
	public Map<String, Map<Method, ThrottlePolicy>> getMethodThrottlePolicyTable() {
		return methodThrottlePolicyTable;
	}

	/**
	 * @param methodThrottlePolicyTable
	 *            the methodThrottlePolicyTable to set
	 */
	public void setMethodThrottlePolicyTable(
			Map<String, Map<Method, ThrottlePolicy>> methodThrottlePolicyTable) {
		this.methodThrottlePolicyTable = methodThrottlePolicyTable;
	}

	/**
	 * @return the excepHandleMethodMap
	 */
	public Map<String, ThrottleExceptionHandleRule> getExcepHandleMethodMap() {
		return excepHandleMethodMap;
	}

}
