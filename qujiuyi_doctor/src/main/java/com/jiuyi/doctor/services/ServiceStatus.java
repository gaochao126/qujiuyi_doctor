package com.jiuyi.doctor.services;

/**
 * @Author: xutaoyang @Date: 上午10:40:34
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum ServiceStatus {
	/** 未开通 */
	CLOSED,

	/** 已开通 */
	OPENED,

	/** 暂停 */
	PAUSE, ;

	public static boolean checkId(int id) {
		return id >= 0 && id < values().length;
	}
}
