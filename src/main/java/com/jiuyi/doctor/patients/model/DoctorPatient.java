/**
 * 
 */
package com.jiuyi.doctor.patients.model;

/**
 * 医患关系表
 * 
 * @author xutaoyang
 *
 */
public class DoctorPatient {

	private int id;
	private int doctorId;
	private int patientId;
	private String remark;
	private String note;
	/** see {@link DoctorPatientSrc} */
	private int src;// 来源
	/** see {@link DoctorPatientRelation} */
	private int relation;

	public DoctorPatient(int doctorId, int patientId, int src, int relation) {
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.src = src;
		this.relation = relation;
	}

	public DoctorPatient(int doctorId, int patientId, DoctorPatientSrc src, DoctorPatientRelation relation) {
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.src = src.id;
		this.relation = relation.ordinal();
	}

	public int getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public String getRemark() {
		return remark;
	}

	public String getNote() {
		return note;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

}
