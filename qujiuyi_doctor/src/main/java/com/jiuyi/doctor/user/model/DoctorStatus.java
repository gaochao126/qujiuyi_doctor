package com.jiuyi.doctor.user.model;

/**
 * @Author: xutaoyang @Date: 下午4:19:19
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum DoctorStatus {

	// @formatter:off
	
	// 以下有序，加的时候请在后面加
	/** 未提交认证信息 */
	NEED_AUTH(0,1,2,3,4),
	/** 审核中 */
	UNDER_VERIFY(1,1),
	/** 审核失败 */
	VERIFY_FAIL(2,2),
	/** 审核成功，正常状态 */
	NORMAL(3,3),
	/** 冻结 */
	FROZEN(4,4), ;
	
	// @formatter:on
	public int id;
	public int[] validStatus;

	private DoctorStatus(int id, int... validStatus) {
		this.id = id;
		this.validStatus = validStatus;
	}

	public boolean hasPrivilege(int status) {
		for (int valid : validStatus) {
			if (status == valid) {
				return true;
			}
		}
		return false;
	}
}
