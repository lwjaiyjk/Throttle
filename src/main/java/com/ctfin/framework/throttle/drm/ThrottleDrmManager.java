/**
 * ThrottleDrmManager.java
 * author: yujiakui
 * 2018年1月10日
 * 下午2:59:14
 */
package com.ctfin.framework.throttle.drm;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.client.DrmResourceManager;
import com.ctfin.framework.drm.client.annotation.DrmClassResource;
import com.ctfin.framework.drm.client.annotation.DrmFieldResource;
import com.ctfin.framework.drm.client.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         下午2:59:14
 *
 *         限流drm推送的资源管理
 */
@Component
@DrmClassResource
public class ThrottleDrmManager implements DrmResourceManager {

	/** 日志对象 */
	private final static Logger LOGGER = LoggerFactory.getLogger(ThrottleDrmManager.class);

	/** 限流drm推送失效处理器 */
	@Autowired
	private ThrottleDrmHandler throttleDrmDisableHandler;

	/** 限流drm推送修改处理器 */
	@Autowired
	private ThrottleDrmHandler throttleDrmModifyHandler;

	/**
	 * drm 推送的限流信息：格式为 类名#属性名#规则名称#条件类型#值@类名1#属性名1#规则名称1#条件类型1#值1
	 */
	@DrmFieldResource
	private String drmModifyThrottleInfo;

	/** 限流推送钱的值 */
	private String drmModifyThrottleInfoBef;

	/**
	 * 限流disable推送信息 ：类名#属性名#规则名称#条件类型@类名1#属性名1#规则名称1#条件类型1
	 */
	@DrmFieldResource
	private String drmDisableThrottleInfo;

	/** 限流失效推送前的值 */
	private String drmDisableThrottleInfoBef;

	/*
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#beforeUpdate()
	 */
	@Override
	public void beforeUpdate() {
		LogUtils.info(LOGGER,
				"drm推送限流信息,推送前的值为drmModifyThrottleInfo=[{0}],drmDisableThrottleInfo=[{1}]",
				drmModifyThrottleInfo, drmDisableThrottleInfo);
		drmModifyThrottleInfoBef = drmDisableThrottleInfo;
		drmDisableThrottleInfoBef = drmDisableThrottleInfo;
	}

	/* (non-Javadoc)
	 * @see com.ctfin.framework.drm.client.DrmResourceManager#afterUpdate()
	 */
	@Override
	public void afterUpdate() {
		// 处理限流信息失效drm推送
		handleDisableThrottleDrm();
		// 处理限流信息修改drm推送
		handleModifyThrottleDrm();
	}

	/**
	 * 处理限流信息修改drm推送
	 */
	private void handleModifyThrottleDrm() {
		if (StringUtils.isNotEmpty(drmModifyThrottleInfo)
				&& !StringUtils.equals(drmModifyThrottleInfo.trim(), drmModifyThrottleInfoBef)) {
			drmModifyThrottleInfo = drmModifyThrottleInfo.trim();
			throttleDrmModifyHandler.handle(drmModifyThrottleInfo);
			LogUtils.info(LOGGER, "drm限流修改drmModifyThrottleInfo={0}", drmModifyThrottleInfo);
		}
	}

	/**
	 * 处理限流信息失效drm推送
	 *
	 */
	private void handleDisableThrottleDrm() {
		if (StringUtils.isNotEmpty(drmDisableThrottleInfo)
				&& !StringUtils.equals(drmDisableThrottleInfo.trim(), drmDisableThrottleInfoBef)) {
			drmDisableThrottleInfo = drmDisableThrottleInfo.trim();
			throttleDrmDisableHandler.handle(drmDisableThrottleInfo);
			LogUtils.info(LOGGER, "drm限流失效drmDisableThrottleInfo={0}", drmDisableThrottleInfo);
		}

	}

	/**
	 * @return the drmModifyThrottleInfo
	 */
	public String getDrmModifyThrottleInfo() {
		return drmModifyThrottleInfo;
	}

	/**
	 * @param drmModifyThrottleInfo
	 *            the drmModifyThrottleInfo to set
	 */
	public void setDrmModifyThrottleInfo(String drmModifyThrottleInfo) {
		this.drmModifyThrottleInfo = drmModifyThrottleInfo;
	}

	/**
	 * @return the drmDisableThrottleInfo
	 */
	public String getDrmDisableThrottleInfo() {
		return drmDisableThrottleInfo;
	}

	/**
	 * @param drmDisableThrottleInfo
	 *            the drmDisableThrottleInfo to set
	 */
	public void setDrmDisableThrottleInfo(String drmDisableThrottleInfo) {
		this.drmDisableThrottleInfo = drmDisableThrottleInfo;
	}

}
