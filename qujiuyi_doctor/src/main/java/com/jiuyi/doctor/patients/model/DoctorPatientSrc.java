/**
 * 
 */
package com.jiuyi.doctor.patients.model;

/**
 * 患者来源
 * 
 * @author xutaoyang
 *
 */
public enum DoctorPatientSrc {

	/** 患者购买了私人医生服务 */
	PERSONAL_PATIENT(1),

	/** 患者收藏了医生 */
	COLLECT(2),

	/** 医生服务过患者，比如图文咨询，处方等 */
	SERVICE(3),

	/** 医生手动添加 */
	MANUAL(4);

	public final int id;

	private DoctorPatientSrc(int id) {
		this.id = id;
	}
}
