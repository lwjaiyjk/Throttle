/**
 * ThrottleConditionType.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:27:18
 */
package com.ctfin.framework.throttle.model;

/**
 * @author yujiakui
 *
 *         上午9:27:18
 *
 */
public enum ThrottleConditionTypeEnum {

	/** 秒单位 */
	SECOND("second", "秒", "second", "秒单位", 1L),

	/** 分钟单位 */
	MINUTE("minute", "分钟", "minute", "分钟单位", 60L),

	/** 小时单位 */
	HOUR("hour", "小时", "hour", "小时单位", 60 * 60L),

	/** 天单位 */
	DAY("day", "天", "day", "天单位", 24 * 60 * 60L),

	/** 周单位 */
	WEEKDAY("weekday", "周", "weekday", "周单位", 7 * 24 * 60 * 60L),

	/** 月单位 */
	MONTH("month", "月", "month", "月单位", 30 * 24 * 60 * 60L),

	/** 年单位 */
	YEAR("year", "年", "year", "年单位", 365 * 24 * 60 * 60L);

	private String code;

	private String chineseName;

	private String englishName;

	private String desc;

	/** 秒的倍数 */
	private long secondTimes;

	/**
	 * 根据code查询对应的枚举类型
	 *
	 * @param code
	 * @return
	 */
	public static ThrottleConditionTypeEnum getByCode(String code) {
		for (ThrottleConditionTypeEnum ele : values()) {
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
	private ThrottleConditionTypeEnum(String code, String chineseName, String englishName,
			String desc, long secondTimes) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.desc = desc;
		this.secondTimes = secondTimes;
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

	/**
	 * @return the secondTimes
	 */
	public long getSecondTimes() {
		return secondTimes;
	}

	/**
	 * @param secondTimes
	 *            the secondTimes to set
	 */
	public void setSecondTimes(long secondTimes) {
		this.secondTimes = secondTimes;
	}

}
