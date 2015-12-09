package com.jiuyi.doctor.prescribe.model;

/**
 * @Author: xutaoyang @Date: 下午3:51:51
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DoctorPrescribe {

	private int id;
	private int doctorId;
	private int status;

	public DoctorPrescribe(int id, int doctorId, int status) {
		this.id = id;
		this.doctorId = doctorId;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getStatus() {
		return status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
