/**
 * ThrottleRuleTypeEnum.java
 * author: yujiakui
 * 2017年9月23日
 * 上午9:57:35
 */
package com.ctfin.framework.throttle.model;

/**
 * @author yujiakui
 *
 *         上午9:57:35
 *
 *         限流规则类型枚举
 */
public enum ThrottleRuleTypeEnum {

	/** 幂等类型 */
	IDEMPOTENT("idempotent", "幂等类型", "idempotent", "幂等类型", 1),

	/** 限流类型 */
	THROTTLE("throttle", "限流类型", "throttle", "限流类型");

	private String code;

	private String chineseName;

	private String englishName;

	private String desc;

	private int weight;

	/**
	 * 根据code查询对应的枚举类型
	 *
	 * @param code
	 * @return
	 */
	public static ThrottleRuleTypeEnum getByCode(String code) {
		for (ThrottleRuleTypeEnum ele : values()) {
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
	 * @param cndKeyType
	 * @param cndValueType
	 */
	private ThrottleRuleTypeEnum(String code, String chineseName, String englishName, String desc) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.desc = desc;
		weight = 2;
	}

	/**
	 * @param key
	 * @param chineseName
	 * @param englishName
	 * @param desc
	 * @param weight
	 */
	private ThrottleRuleTypeEnum(String code, String chineseName, String englishName, String desc,
			int weight) {
		this.code = code;
		this.chineseName = chineseName;
		this.englishName = englishName;
		this.desc = desc;
		this.weight = weight;
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
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

}
