package com.jiuyi.doctor.user.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.ConfigPrefix;

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

	private Integer titleId; // 职称

	private String skill;
	private String experience;

	private Integer offlineId;// 对应的线下医生的id

	private Integer gender;

	@Column("head")
	@ConfigPrefix("doctor.head.path")
	private String headPath;
	private String idCardPath;

	@ConfigPrefix("doctor.titlecard.path")
	private String titleCardPath;
	private String licenseCardPath;

	/** 注册日期 */
	private Date registerDate;

	/** 0注册时提交 1信息修改 */
	private int type;

	/** 0全部 1头像 2职称证 */
	private int field;

	/** 0待审核 1审核通过 2审核未通过 */
	private int status;

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

	public int getField() {
		return field;
	}

	public void setField(int field) {
		this.field = field;
	}

	/** 0待审核 1审核通过 2审核未通过 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getHeadPath() {
		return headPath;
	}

	public void setHeadPath(String headPath) {
		this.headPath = headPath;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}
