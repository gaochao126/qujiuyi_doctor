package com.jiuyi.doctor.patients.model;

/**
 * @Author: xutaoyang @Date: 下午2:42:54
 *
 * @Description 医生和患者之间的几种关系
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum DoctorPatientRelation {

	/** 无关系 */
	NONE,
	/** 常用联系人 */
	CONTACT,
	/** 陌生人 */
	UNFAMILIAR,
	/** 黑名单 */
	BLACKLIST;

	public static DoctorPatientRelation getTypeById(int id) {
		if (id < 0 || id > values().length) {
			return null;
		}
		return DoctorPatientRelation.values()[id];
	}
}
