/**
 * ThrottleExceptionErrorEnum.java
 * author: yujiakui
 * 2017年9月26日
 * 上午11:16:30
 */
package com.ctfin.framework.throttle.exception;

/**
 * @author yujiakui
 *
 *         上午11:16:30
 *
 *         限流异常错误枚举
 */
public enum ThrottleExceptionErrorEnum {

	/** 通用错误码 */
	COMMON_ERROR("0001", "通用错误码", "common error", "通用错误码"),

	/** 调用错误异常 */
	INVOKE_ERROR("0002", "调用错误异常", "invoke error", "调用错误异常"),

	/** 解析错误异常 */
	PARSER_ERROR("0004", "解析错误异常", "parser error", "解析错误异常"),

	/** 幂等命中 */
	IDEMPOTENT_EXIST("0005", "幂等命中", "idempotent exist", "幂等命中"),

	/** 限流条件不通过 */
	THROTTLE_CND_NOT_PASS("0003", "限流条件不通过", "throttle condition not pass", "限流条件不通过");

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
	public static ThrottleExceptionErrorEnum getByCode(String code) {
		for (ThrottleExceptionErrorEnum ele : values()) {
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
	private ThrottleExceptionErrorEnum(String code, String chineseName, String englishName,
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
