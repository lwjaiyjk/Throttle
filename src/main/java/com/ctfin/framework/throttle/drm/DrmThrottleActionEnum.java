/**
 * DrmThrottleActionEnum.java
 * author: yujiakui
 * 2018年1月10日
 * 下午3:30:58
 */
package com.ctfin.framework.throttle.drm;

/**
 * @author yujiakui
 *
 *         下午3:30:58
 *
 *         限流行为枚举
 */
public enum DrmThrottleActionEnum {

	/** 新增限流条件 ADD */
	ADD("ADD", "新增限流条件", "add", "新增限流条件"),

	/** 修改限流条件 */
	MODIFY("MODIFY", "修改限流条件", "MODIFY", "修改限流条件"),

	/** 失效限流条件 */
	DISABLE("DISABLE", "失效限流条件", "disable", "失效限流条件");

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
	public static DrmThrottleActionEnum getByCode(String code) {
		for (DrmThrottleActionEnum ele : values()) {
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
	private DrmThrottleActionEnum(String code, String chineseName, String englishName,
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
