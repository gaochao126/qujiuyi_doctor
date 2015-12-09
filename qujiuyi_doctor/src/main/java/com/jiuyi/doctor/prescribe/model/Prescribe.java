package com.jiuyi.doctor.prescribe.model;

import java.util.Date;
import java.util.List;

import com.jiuyi.frame.annotations.Age;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 下午3:53:12
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Prescribe implements ISerializableObj {

	private int id;
	private int doctorId;
	private int patientId;
	private String patientName;// 系统中患者的姓名
	@Age
	private int patientAge;
	private int patientGender;
	private String symptoms;
	private String symptomsImages;
	private String name;// 实际配药者姓名
	private int age;
	private int gender;
	private int status;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String patientHead;
	private Date createTime;
	private List<Medicine> medicines;
	@ConfigPrefix("preslist.path")
	private String presListImage;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("patientId", this.patientId);
		res.put("patientName", this.patientName);
		res.put("patientAge", this.patientAge);
		res.put("patientGender", this.patientGender);
		res.put("patientHead", this.patientHead);
		res.put("symptoms", this.symptoms);
		res.put("symptomsImages", this.symptomsImages);
		res.put("name", this.name);
		res.put("age", this.age);
		res.put("gender", this.gender);
		res.put("status", this.status);
		res.put("createTime", DateUtil.date2Str(createTime));
		res.put("medicines", this.medicines);
		res.put("presListImage", presListImage);
		return res;
	}

	public int getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getPatientId() {
		return patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public String getSymptomsImages() {
		return symptomsImages;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getGender() {
		return gender;
	}

	public int getStatus() {
		return status;
	}

	public String getPatientHead() {
		return patientHead;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public List<Medicine> getMedicines() {
		return medicines;
	}

	public String getPresListImage() {
		return presListImage;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public void setSymptomsImages(String symptomsImages) {
		this.symptomsImages = symptomsImages;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPatientHead(String patientHead) {
		this.patientHead = patientHead;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}

	public void setPresListImage(String presListImage) {
		this.presListImage = presListImage;
	}

	public int getBirthday() {
		return patientAge;
	}

	public void setBirthday(int birthday) {
		this.patientAge = birthday;
	}

	public int getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(int patientGender) {
		this.patientGender = patientGender;
	}

	public int getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(int patientAge) {
		this.patientAge = patientAge;
	}

}
