package com.jiuyi.doctor.patients;

/**
 * @Author: xutaoyang @Date: 上午10:44:13
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum SystemTag {

	PERSONAL_TAG(1, "已购买私人医生服务");

	public final int id;
	public final String name;

	private SystemTag(int id, String name) {
		this.id = id;
		this.name = name;
	}

}
