/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * 处方实体类
 * 
 * @author xutaoyang
 *
 */
public class Prescription implements ISerializableObj {

	@NotEmpty
	private String id;
	/** 处方编号 */
	private String number;
	private int doctorId;
	private int patientId;
	private int relativeId;
	@NotEmpty
	private String allergies;
	@NotEmpty
	private String illness;
	@NotEmpty
	private String diagnosis;
	private Date createTime;
	private Date updateTime;
	private int status;
	private int medicineTakeStatus;

	// 就诊人信息相关
	private int relativeAge;
	private int relativeGender;
	private String relativeName;
	private String relativeUid;

	// 患者相关
	private String patientName;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String patientHead;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuyi.frame.front.ISerializableObj#serializeToMapObject()
	 */
	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("number", this.number);
		res.put("doctorId", this.doctorId);
		res.put("patientId", this.patientId);
		res.put("relativeId", this.relativeId);
		res.put("allergies", this.allergies);
		res.put("illness", this.illness);
		res.put("diagnosis", this.diagnosis);
		res.put("createTime", DateUtil.date2Str(this.createTime));
		res.put("updateTime", DateUtil.date2Str(this.updateTime));
		res.put("status", this.status);
		res.put("medicineTakeStatus", this.medicineTakeStatus);
		res.put("relativeAge", this.relativeAge);
		res.put("relativeGender", this.relativeGender);
		res.put("relativeName", this.relativeName);
		res.put("relativeUid", this.relativeUid);
		res.put("patientName", this.patientName);
		res.put("patientHead", this.patientHead);
		return res;
	}

	public String getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getPatientId() {
		return patientId;
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

	public String getRelativeName() {
		return relativeName;
	}

	public String getRelativeUid() {
		return relativeUid;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getPatientHead() {
		return patientHead;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
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

	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}

	public void setRelativeUid(String relativeUid) {
		this.relativeUid = relativeUid;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public void setPatientHead(String patientHead) {
		this.patientHead = patientHead;
	}

	public int getRelativeAge() {
		return relativeAge;
	}

	public int getRelativeGender() {
		return relativeGender;
	}

	public void setRelativeAge(int relativeAge) {
		this.relativeAge = relativeAge;
	}

	public void setRelativeGender(int relativeGender) {
		this.relativeGender = relativeGender;
	}

	public int getMedicineTakeStatus() {
		return medicineTakeStatus;
	}

	public void setMedicineTakeStatus(int medicineTakeStatus) {
		this.medicineTakeStatus = medicineTakeStatus;
	}

	public int getRelativeId() {
		return relativeId;
	}

	public void setRelativeId(int relativeId) {
		this.relativeId = relativeId;
	}

}
