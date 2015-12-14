package com.jiuyi.doctor.consult.model;

import java.math.BigDecimal;
import java.util.Date;

import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.annotations.ReadableDate;
import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 上午11:01:03
 *
 * @Description 图文咨询详情
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Consult implements ISerializableObj {

	private String id;
	private int patientId;
	private int patientAge;
	private int patientGender;
	private int doctorId;
	private int acceptStatus;
	private int consultStatus;
	private int payStatus;
	private String symptoms;
	private String symptomsImages;
	@ReadableDate
	private String createTime;
	private String patientName;
	private int age;
	private int gender;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String headPortrait;
	private String phone;
	private BigDecimal money;
	private int type;
	private String name;
	private boolean hasComment;
	private Date startTime;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("patientId", this.patientId);
		res.put("patientAge", this.patientAge);
		res.put("patientGender", this.patientGender);
		res.put("acceptStatus", this.acceptStatus);
		res.put("consultStatus", this.consultStatus);
		res.put("symptoms", this.symptoms);
		res.put("symptomsImages", this.symptomsImages);
		res.put("createTime", this.createTime);
		res.put("patientName", this.patientName);
		res.put("name", this.name);
		res.put("age", this.age);
		res.put("gender", this.gender);
		res.put("patientHead", this.headPortrait);
		res.put("phone", StringUtil.isNullOrEmpty(phone) ? "" : StringUtil.hideStr(this.phone, 3, 6));
		res.put("money", this.money, 0);
		res.put("type", this.type);
		res.put("hasComment", this.hasComment);
		long remainTime = this.startTime == null ? 0 : DBConfigStatic.getConfigInt("consult.autostop.time") - (System.currentTimeMillis() - this.startTime.getTime());
		res.put("remainTime", Math.max(0, remainTime));
		return res;
	}

	public String getId() {
		return id;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getAcceptStatus() {
		return acceptStatus;
	}

	public int getConsultStatus() {
		return consultStatus;
	}

	public String getSymptoms() {
		return symptoms;
	}

	public String getSymptomsImages() {
		return symptomsImages;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getAge() {
		return age;
	}

	public int getGender() {
		return gender;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public int getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setAcceptStatus(int acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public void setConsultStatus(int consultStatus) {
		this.consultStatus = consultStatus;
	}

	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	public void setSymptomsImages(String symptomsImages) {
		this.symptomsImages = symptomsImages;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public String getName() {
		return name;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPatientAge() {
		return patientAge;
	}

	public int getPatientGender() {
		return patientGender;
	}

	public void setPatientAge(int patientAge) {
		this.patientAge = patientAge;
	}

	public void setPatientGender(int patientGender) {
		this.patientGender = patientGender;
	}

	public boolean isHasComment() {
		return hasComment;
	}

	public void setHasComment(boolean hasComment) {
		this.hasComment = hasComment;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

}
