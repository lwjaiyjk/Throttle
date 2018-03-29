/**
 * ThrottleCondtionPersistence.java
 * author: yujiakui
 * 2017年9月25日
 * 下午11:01:49
 */
package com.ctfin.framework.throttle.persistence;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author yujiakui
 *
 *         下午11:01:49
 *
 *         限流条件持久化（默认持久化到redis中）
 */
public interface ThrottlePersistenceService {

	/**
	 * 根据key获得对应的领域对象
	 *
	 * @param throttlePersistQueryRequest
	 * @return
	 */
	public ThrottlePersistControlResult selectByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest);

	/**
	 * 持久化
	 *
	 * @param key
	 * @param throttleConditionPersistModel
	 * @return
	 */
	public boolean save(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult);

	/**
	 * 对于幂等需要进行callBack处理
	 *
	 * @param throttlePersistQueryRequests
	 * @param pjp
	 * @return
	 */
	public Object saveWithCallbackForIdempoent(
			ThrottlePersistQueryRequest throttlePersistQueryRequests, ProceedingJoinPoint pjp);

	/**
	 * 修改
	 *
	 * @param key
	 * @param throttleConditionPersistModel
	 *            从select中查询出来的值，在框架中并没有进行更新， 需要自己在实现的时候进行更新
	 * @return
	 */
	public boolean update(ThrottlePersistQueryRequest throttlePersistQueryRequest,
			ThrottlePersistControlResult throttlePersistControlResult);

	/**
	 * 删除
	 *
	 * @param throttlePersistQueryRequest
	 * @return
	 */
	public ThrottlePersistControlResult deleteByKey(
			ThrottlePersistQueryRequest throttlePersistQueryRequest);

}
