package com.jiuyi.doctor.personal;

/**
 * @Author: xutaoyang @Date: 上午10:55:21
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum PersonalDocterType {

	WEEKLY(1, 25), MONTH(2, 100);

	public final int id;
	public final int default_price;

	private PersonalDocterType(int id, int default_price) {
		this.id = id;
		this.default_price = default_price;
	}

	public static int getPriceByType(int type) {
		for (PersonalDocterType serviceType : values()) {
			if (serviceType.id == type) {
				return serviceType.default_price;
			}
		}
		return 0;
	}

	public static boolean checkType(int type) {
		for (PersonalDocterType serviceType : values()) {
			if (serviceType.id == type) {
				return true;
			}
		}
		return false;
	}
}
