/**
 * 
 */
package com.jiuyi.doctor.prescription.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.annotations.ReadableDate;
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

	private String id;
	/** 处方编号 */
	private String number;
	private int doctorId;
	@NotNull
	private Integer patientId;
	private int relativeId;
	@NotEmpty
	private String sickHistory;
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
	private BigDecimal price;
	private int type;
	private String remark;// 修改备注
	private int version;

	// 就诊人信息相关
	@NotNull
	@Range(max = 2, min = 1)
	private Integer relativeGender;
	@NotEmpty
	private String relativeName;
	@NotEmpty
	private String relativeNation;
	@NotNull
	private Date relativeBirthday;
	private String relativeUid;

	// 患者相关
	private String patientName;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String patientHead;
	private String patientPhone;
	private int patientGender;

	// 审核配药信息
	private int reviewResult;// 审核结果，1通过 2不通过
	private String reviewDoctorName;
	@ReadableDate("yyyy-MM-dd HH:mm:ss")
	private String reviewDate;

	// 配药状态
	private int presStatus;// 配药状态 1未配药 2已经配药
	private String presDoctorName;
	@ReadableDate("yyyy-MM-dd HH:mm:ss")
	private String presDate;

	private int payType;// 费别
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
		res.put("status", this.status);
		res.put("medicineTakeStatus", this.medicineTakeStatus);
		res.put("relativeBirthday", this.relativeBirthday);
		res.put("relativeGender", this.relativeGender);
		res.put("relativeName", this.relativeName);
		res.put("relativeUid", this.relativeUid);
		res.put("patientName", this.patientName);
		res.put("patientHead", this.patientHead);
		res.put("price", this.price);
		res.put("type", this.type);
		res.put("typeName", PrescriptionType.getNameById(this.type));
		res.put("createTime", DateUtil.date2Str(this.createTime, "yyyy-MM-dd HH:mm:ss"));
		res.put("updateTime", DateUtil.date2Str(this.updateTime, "yyyy-MM-dd HH:mm:ss"));
		return res;
	}

	public MapObject serializeDetail() {
		MapObject res = serializeToMapObject();
		res.put("reviewDoctorName", this.reviewDoctorName);
		res.put("reviewDate", this.reviewDate);
		res.put("presDoctorName", this.presDoctorName);
		res.put("presDate", this.presDate);
		res.put("reviewResult", this.reviewResult);
		res.put("presStatus", this.presStatus);
		res.put("payType", this.payType);
		res.put("patientPhone", this.patientPhone);
		res.put("patientGender", this.patientGender);
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

	public Integer getPatientId() {
		return patientId;
	}

	public int getRelativeId() {
		return relativeId;
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

	public int getMedicineTakeStatus() {
		return medicineTakeStatus;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getRelativeGender() {
		return relativeGender;
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

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public void setRelativeId(int relativeId) {
		this.relativeId = relativeId;
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

	public void setMedicineTakeStatus(int medicineTakeStatus) {
		this.medicineTakeStatus = medicineTakeStatus;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setRelativeGender(Integer relativeGender) {
		this.relativeGender = relativeGender;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getRelativeBirthday() {
		return relativeBirthday;
	}

	public void setRelativeBirthday(Date relativeBirthday) {
		this.relativeBirthday = relativeBirthday;
	}

	public String getReviewDoctorName() {
		return reviewDoctorName;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public String getPresDoctorName() {
		return presDoctorName;
	}

	public String getPresDate() {
		return presDate;
	}

	public void setReviewDoctorName(String reviewDoctorName) {
		this.reviewDoctorName = reviewDoctorName;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public void setPresDoctorName(String presDoctorName) {
		this.presDoctorName = presDoctorName;
	}

	public void setPresDate(String presDate) {
		this.presDate = presDate;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public int getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(int patientGender) {
		this.patientGender = patientGender;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getReviewResult() {
		return reviewResult;
	}

	public void setReviewResult(int reviewResult) {
		this.reviewResult = reviewResult;
	}

	public int getPresStatus() {
		return presStatus;
	}

	public void setPresStatus(int presStatus) {
		this.presStatus = presStatus;
	}

	public String getRelativeNation() {
		return relativeNation;
	}

	public void setRelativeNation(String relativeNation) {
		this.relativeNation = relativeNation;
	}

	public String getSickHistory() {
		return sickHistory;
	}

	public void setSickHistory(String sickHistory) {
		this.sickHistory = sickHistory;
	}

}
