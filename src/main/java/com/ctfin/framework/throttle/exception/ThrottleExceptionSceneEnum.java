/**
 * ThrottleExceptionSceneEnum.java
 * author: yujiakui
 * 2017年9月26日
 * 上午11:14:37
 */
package com.ctfin.framework.throttle.exception;

/**
 * @author yujiakui
 *
 *         上午11:14:37
 *
 *         限流异常场景枚举
 */
public enum ThrottleExceptionSceneEnum {

	/** 通用场景 */
	COMMON_SCENE("0001", "通用场景", "common scene", "通用场景"),

	/** 不应该存在的调用场景 */
	NOT_SHOULD_EXIST("0002", "不应该存在的调用场景", "not should exist invoke", "不应该存在的调用场景"),

	/** 幂等场景 */
	IDEMPOTENT("0004", "幂等场景", "idempotent", "幂等场景"),

	/** 被限流的场景(限流条件不通过) */
	THROTTLE_NOT_PASS("0003", "被限流的场景(限流条件不通过)", "throttle condition not pass", "被限流的场景(限流条件不通过)");

	private String code;

	private String chineseName;

	private String englishName;

	private String desc;

	/**
	 * 根据code查询对应的枚举类型
	 *
	 * @param code
	 * @return
	 */
	public static ThrottleExceptionSceneEnum getByCode(String code) {
		for (ThrottleExceptionSceneEnum ele : values()) {
			if (ele.getCode().equals(code)) {
				return ele;
			}
		}
		return null;
	}

	/**
	 * @param key
	 * @param chineseName
	 * @param englishName
	 * @param memo
	 */
	private ThrottleExceptionSceneEnum(String code, String chineseName, String englishName,
			String desc) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.desc = desc;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the chineseName
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * @param chineseName
	 *            the chineseName to set
	 */
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}

	/**
	 * @param englishName
	 *            the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
