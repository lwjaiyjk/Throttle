/**
 * AbstractThrottleDrmHandler.java
 * author: yujiakui
 * 2018年1月12日
 * 下午2:54:08
 */
package com.ctfin.framework.throttle.drm;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctfin.framework.throttle.model.ThrottlePolicy;
import com.ctfin.framework.throttle.parser.ThrottlingParserFactory;
import com.ctfin.framework.throttle.utils.LogUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yujiakui
 *
 *         下午2:54:08
 *
 *         限流drm处理抽象类
 */
public abstract class AbstractThrottleDrmHandler implements ThrottleDrmHandler {

	/** logger */
	protected final static Logger LOGGER = LoggerFactory
			.getLogger(AbstractThrottleDrmHandler.class);

	@Autowired
	protected ThrottlingParserFactory throttlingParserFactory;

	/** 失效drm字段个数 */
	protected final static int DISABLE_DRM_FIELD_NUM = 4;

	/** 修改drm字段个数 */
	protected final static int MODIFY_DRM_FIELD_NUM = 5;

	/** 表示所有的进行推送 */
	protected final static String ALL = "*";

	/** 多个分割符 */
	private final static String MULTI_SEP = "@";

	/** 属性字段分割符 */
	private final static String FIELD_SEP = "#";

	/* 处理
	 * @see com.ctfin.framework.throttle.drm.ThrottleDrmHandler#handle(java.lang.String)
	 */
	@Override
	public void handle(String drmInfo) {
		handleDrm(drmInfo);
		LogUtils.info(LOGGER, "处理限流drm之后对应的限流策略表throttlePolicyTable={0}",
				throttlingParserFactory.getMethodThrottlePolicyTable());
	}

	/**
	 * 处理drm推送
	 *
	 * @param drmInfo
	 */
	protected abstract void handleDrm(String drmInfo);

	/**
	 * 获得限流策略的克隆版本
	 *
	 * @return
	 */
	protected Map<String, Map<Method, ThrottlePolicy>> getThrottlePolicyClone() {
		return cloneThrottlePolicyTable(throttlingParserFactory.getMethodThrottlePolicyTable());
	}

	/**
	 * 克隆限流策略table
	 *
	 * @param throttlePolicyTable
	 * @return
	 */
	protected Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable(
			Map<String, Map<Method, ThrottlePolicy>> throttlePolicyTable) {
		Map<String, Map<Method, ThrottlePolicy>> cloneThrottlePolicyTable = Maps.newHashMap();
		for (Map.Entry<String, Map<Method, ThrottlePolicy>> throttlePolicyTableEntry : throttlePolicyTable
				.entrySet()) {
			cloneThrottlePolicyTable.put(throttlePolicyTableEntry.getKey(),
					Maps.newHashMap(throttlePolicyTableEntry.getValue()));
		}
		return cloneThrottlePolicyTable;
	}

	/**
	 * 解析drm信息
	 *
	 * @param drmInfo：
	 *            类名#属性名#规则名称#条件类型#值@类名1#属性名1#规则名称1#条件类型1#值1
	 * @return
	 */
	protected List<ThrottleDrmParseModel> parse(String drmInfo) {
		List<ThrottleDrmParseModel> throttleDrmParseModels = Lists.newArrayList();
		if (StringUtils.isEmpty(drmInfo)) {
			LogUtils.warn(LOGGER, "drm推送的信息为空");
			return throttleDrmParseModels;
		}

		// 使用逗号分割对应的drm信息
		String[] singleDrmInfos = StringUtils.split(drmInfo, MULTI_SEP);
		try {
			for (String singleDrmInfo : singleDrmInfos) {
				// 处理单个drm信息
				ThrottleDrmParseModel throttleDrmParseModel = parseSingleDrmInfo(singleDrmInfo);
				throttleDrmParseModels.add(throttleDrmParseModel);
			}
		} catch (Throwable throwable) {
			LogUtils.error(LOGGER, "drm限流信息解析失败errMsg={0}", throwable.getMessage());
		}
		return throttleDrmParseModels;
	}

	/**
	 * 解析单个drm推送信息
	 *
	 * @param singleDrmInfo
	 *            类名#属性名#规则名称#条件类型#值 对于失效的场景对应的值不存在
	 * @return
	 */
	private ThrottleDrmParseModel parseSingleDrmInfo(String singleDrmInfo) {
		// 使用 # 分割对应的每一个属性对应的值
		String[] singleFieldInfos = StringUtils.split(singleDrmInfo, FIELD_SEP);
		if (singleFieldInfos.length < DISABLE_DRM_FIELD_NUM
				|| singleFieldInfos.length > MODIFY_DRM_FIELD_NUM) {
			LogUtils.logAndThrowException(LOGGER,
					MessageFormat.format("drm推送的信息长度不是{0}也不是{1},singleDrmInfo={2}",
							DISABLE_DRM_FIELD_NUM, MODIFY_DRM_FIELD_NUM, singleDrmInfo));
		}
		ThrottleDrmParseModel throttleDrmParseModel = new ThrottleDrmParseModel();
		throttleDrmParseModel.setClassFullName(singleFieldInfos[0]);
		if (StringUtils.equals(ALL, singleFieldInfos[1])
				|| StringUtils.equals(ALL, singleFieldInfos[0])) {
			throttleDrmParseModel.setMethod(null);
		} else {
			throttleDrmParseModel
					.setMethod(getMethodByName(singleFieldInfos[1], singleFieldInfos[0]));
		}
		throttleDrmParseModel.setRuleName(singleFieldInfos[2]);
		throttleDrmParseModel.setCondTypeStr(singleFieldInfos[3]);
		if (MODIFY_DRM_FIELD_NUM == singleFieldInfos.length) {
			String condValueStr = singleFieldInfos[4];
			try {
				float condValue = Float.parseFloat(condValueStr);
				throttleDrmParseModel.setCondValue(condValue);
			} catch (NumberFormatException numberFormatException) {
				LogUtils.warn(LOGGER, MessageFormat.format("修改限流对应的条件值不是数字类型{0}", singleDrmInfo),
						numberFormatException);
			}

		}

		return throttleDrmParseModel;
	}

	/**
	 * @param drmClassInfos
	 * @param drmMethodInfos
	 * @param classFullName
	 */
	private Method getMethodByName(String methodName, String classFullName) {
		Method methodObj = null;
		Method[] methods;
		try {
			methods = Class.forName(classFullName).getMethods();
			boolean methodExistFlag = false;
			for (Method method : methods) {
				if (StringUtils.equals(method.toGenericString(), methodName)) {
					// 说明方法存在
					methodObj = method;
					methodExistFlag = true;
					break;
				}
			}
			if (!methodExistFlag) {
				// 对应的方法不存在
				LogUtils.logAndThrowException(LOGGER, MessageFormat
						.format("drm推送的失效信息根据方法名获得对应的Method对象失败methodName={0}方法名不存在", methodName));
			}
		} catch (SecurityException | ClassNotFoundException e) {
			LogUtils.logAndThrowException(LOGGER, MessageFormat
					.format("drm推送的失效信息根据类全名获得对应的class对象失败classFullName={0}", classFullName), e);
		}

		return methodObj;
	}

	/**
	 * 克隆类和方法上对应的限流策略
	 *
	 * @param throttleDrmInfoModel
	 * @param cloneThrottlePolicyTable
	 * @return
	 */
	protected ThrottlePolicy getMethodThrottlePolicyClone(Method method,
			Map<Method, ThrottlePolicy> throttlePolicyMap) {

		ThrottlePolicy throttlePolicy = throttlePolicyMap.get(method);
		if (null == throttlePolicy) {
			// 获取的所有方法
			throttlePolicy = throttlePolicyMap.get(ThrottlingParserFactory.ALL_METHOD);
			return cloneThrottlePolicy(throttlePolicy);
		}
		return throttlePolicy;
	}

	/**
	 * 克隆限流策略
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
}
