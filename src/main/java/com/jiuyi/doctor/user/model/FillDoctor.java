package com.jiuyi.doctor.user.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Author: xutaoyang @Date: 下午4:16:03
 *
 * @Description 用来接收医生填写认证信息的实体类
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class FillDoctor {

	private int doctorId;

	@NotEmpty
	private String name;
	@NotNull
	private Integer hospitalId;
	@NotNull
	private Integer departmentId;
	@NotEmpty
	private String officePhone;

	@NotNull
	private Integer titleId; // 职称

	private String skill;
	private String experience;

	private Integer offlineId;// 对应的线下医生的id

	private Integer gender;
	private String headPath;
	private String idCardPath;
	private String titleCardPath;
	private String licenseCardPath;

	private int type;

	public String getName() {
		return name;
	}

	public Integer getGender() {
		return gender;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public Integer getTitleId() {
		return titleId;
	}

	public String getHeadPath() {
		return headPath;
	}

	public String getIdCardPath() {
		return idCardPath;
	}

	public String getTitleCardPath() {
		return titleCardPath;
	}

	public String getLicenseCardPath() {
		return licenseCardPath;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void setHospitalId(Integer hospital) {
		this.hospitalId = hospital;
	}

	public void setDepartmentId(Integer department) {
		this.departmentId = department;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}

	public void setIdCardPath(String idCardPath) {
		this.idCardPath = idCardPath;
	}

	public void setTitleCardPath(String titleCardPath) {
		this.titleCardPath = titleCardPath;
	}

	public void setLicenseCardPath(String licenseCardPath) {
		this.licenseCardPath = licenseCardPath;
	}

	public Integer getOfflineId() {
		return offlineId;
	}

	public void setOfflineId(Integer offlineId) {
		this.offlineId = offlineId;
	}

	public String getSkill() {
		return skill;
	}

	public String getExperience() {
		return experience;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

}
