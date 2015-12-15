/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.Date;

/**
 * 处方实体类
 * 
 * @author xutaoyang
 *
 */
public class Prescription {

	private String id;
	private int doctorId;
	private int patientId;
	private int patientRelativeId;
	private String allergies;
	private String illness;
	private String diagnosis;
	private Date createTime;
	private Date updateTime;
	private int status;

	public String getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getPatientRelativeId() {
		return patientRelativeId;
	}

	public String getAllergies() {
		return allergies;
	}

	public String getIllness() {
		return illness;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setPatientRelativeId(int patientRelativeId) {
		this.patientRelativeId = patientRelativeId;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public void setIllness(String illness) {
		this.illness = illness;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
