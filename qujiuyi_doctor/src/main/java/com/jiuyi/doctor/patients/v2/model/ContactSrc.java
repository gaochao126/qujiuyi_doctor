package com.jiuyi.doctor.patients.v2.model;

/**
 * @Author: xutaoyang @Date: 上午10:18:25
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum ContactSrc {

	PERSONAL_PATIENT(1), COMMON(2);

	public final int id;

	private ContactSrc(int id) {
		this.id = id;
	}

}
