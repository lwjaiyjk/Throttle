/**
 * ThrottleDrmHandler.java
 * author: yujiakui
 * 2018年1月12日
 * 下午2:52:55
 */
package com.ctfin.framework.throttle.drm;

/**
 * @author yujiakui
 *
 *         下午2:52:55
 *
 *         限流drm信息处理
 */
public interface ThrottleDrmHandler {

	/**
	 * 处理drm信息
	 * 
	 * @param drmInfo
	 */
	public void handle(String drmInfo);
}
