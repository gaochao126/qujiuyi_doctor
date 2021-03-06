package com.jiuyi.doctor.user.model;

import java.util.concurrent.atomic.AtomicBoolean;

import org.hibernate.validator.constraints.NotEmpty;

import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.zervice.user.model.User;

/**
 * @Author: xutaoyang @Date: 下午4:16:03
 *
 * @Description 医生实体类
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Doctor extends User {

	private int id;
	private String phone;
	@NotEmpty
	private String name;
	@NotEmpty
	private String hospital;
	@NotEmpty
	private String department;
	private String officePhone;
	private String headPath;
	private String idCardPath;
	private String titleCardPath;
	private String licenseCardPath;

	/** 线下医生id */
	private int offlineId;
	private int status;
	private int score;
	private Integer titleId; // 职称
	private String skill;// 特长
	private String experience; // 职业经历
	private String position;// 职位
	private String graduationSchool;// 毕业院校

	private int hospitalId;
	private int departmentId;
	private int praisedNum;// 被赞次数
	private String qrCodeImg;// 二维码图片地址

	private transient long lastAccesss;

	private transient String access_token;
	private transient String channelId;
	private transient Integer deviceType;

	/** true表示 需要更新token了，当下一次访问接口的时候，将返回最新的token */
	private AtomicBoolean needUpdateToken = new AtomicBoolean(false);
	private String newToken;
	private long tokenGenTime;// token生成的时间

	public Doctor() {
	}

	public Doctor(int id, String phone) {
		this.id = id;
		this.phone = phone;
	}

	public Doctor(int id, String phone, String name, String hospital, String department, String headPath, String idCardPath, String titleCardPath, String licenseCardPath, int status, int score,
			Integer titleId, String skill, String experience, String position, String graduationSchool, int praisedNum, String qrCodeImg) {
		this.id = id;
		this.phone = phone;
		this.name = name;
		this.hospital = hospital;
		this.department = department;
		this.headPath = headPath;
		this.idCardPath = idCardPath;
		this.titleCardPath = titleCardPath;
		this.licenseCardPath = licenseCardPath;
		this.status = status;
		this.score = score;
		this.titleId = titleId;
		this.skill = skill;
		this.experience = experience;
		this.position = position;
		this.graduationSchool = graduationSchool;
		this.praisedNum = praisedNum;
		this.qrCodeImg = qrCodeImg;
	}

	@Override
	public MapObject serializeToMapObject() {
		String headPath = StringUtil.isNullOrEmpty(this.headPath) ? "" : DBConfigStatic.getConfig("doctor.head.path") + this.headPath;
		String idCardPath = StringUtil.isNullOrEmpty(this.idCardPath) ? "" : DBConfigStatic.getConfig("doctor.idcard.path") + this.idCardPath;
		String titleCardPath = StringUtil.isNullOrEmpty(this.titleCardPath) ? "" : DBConfigStatic.getConfig("doctor.titlecard.path") + this.titleCardPath;
		String licenseCardPath = StringUtil.isNullOrEmpty(this.licenseCardPath) ? "" : DBConfigStatic.getConfig("doctor.licensecard.path") + this.licenseCardPath;
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("phone", this.phone);
		res.put("name", this.name);
		res.put("hospital", this.hospital);
		res.put("department", this.department);
		res.put("officePhone", this.officePhone);
		res.put("head", headPath);
		res.put("idCardPath", idCardPath);
		res.put("titleCardPath", titleCardPath);
		res.put("licenseCardPath", licenseCardPath);
		res.put("status", this.status);
		res.put("score", this.score);
		res.put("title", DoctorTitle.getTitleNameById(this.titleId));
		res.put("skill", this.skill);
		res.put("experience", this.experience);
		res.put("position", this.position);
		res.put("graduationSchool", this.graduationSchool);
		res.put("praisedNum", this.praisedNum);
		res.put("qrCodeImg", this.qrCodeImg);
		return res;
	}

	/***
	 * 用户长时间没有操作，将从内存中移除
	 */
	@Override
	public boolean isExpire(long now, int expireTime) {
		if (lastAccesss == 0) {
			lastAccesss = System.currentTimeMillis();
			return false;
		}
		long passedTime = now - lastAccesss;
		return passedTime > expireTime;
	}

	/**
	 * token是否过期
	 * 
	 * @param now
	 * @param tokenExpireTime
	 * @return
	 */
	public boolean isTokenExpire(long now, int tokenExpireTime) {
		if (tokenGenTime == 0) {
			tokenGenTime = System.currentTimeMillis();
			return false;
		}
		return now - tokenGenTime > tokenExpireTime;
	}

	@Override
	public int getId() {
		return id;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public String getHospital() {
		return hospital;
	}

	public String getDepartment() {
		return department;
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

	@Override
	public int getStatus() {
		return status;
	}

	public String getSkill() {
		return skill;
	}

	public String getExperience() {
		return experience;
	}

	@Override
	public long getLastAccesss() {
		return lastAccesss;
	}

	@Override
	public String getAccess_token() {
		return access_token;
	}

	public String getChannelId() {
		return channelId;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getTitleId() {
		return titleId;
	}

	public void setTitleId(Integer titleId) {
		this.titleId = titleId;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	@Override
	public void setLastAccesss(long lastAccesss) {
		this.lastAccesss = lastAccesss;
	}

	@Override
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getPosition() {
		return position;
	}

	public String getGraduationSchool() {
		return graduationSchool;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setGraduationSchool(String graduationSchool) {
		this.graduationSchool = graduationSchool;
	}

	public int getPraisedNum() {
		return praisedNum;
	}

	public void setPraisedNum(int praisedNum) {
		this.praisedNum = praisedNum;
	}

	public String getQrCodeImg() {
		return qrCodeImg;
	}

	public void setQrCodeImg(String qrCodeImg) {
		this.qrCodeImg = qrCodeImg;
	}

	public int getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}

	@Override
	public void setLastAccess() {
		this.lastAccesss = System.currentTimeMillis();
	}

	public void copyInfo(Doctor doctor) {
		this.lastAccesss = doctor.getLastAccesss();
		this.channelId = doctor.getChannelId();
		this.deviceType = doctor.getDeviceType();
	}

	public int getOfflineId() {
		return offlineId;
	}

	public void setOfflineId(int offlineId) {
		this.offlineId = offlineId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getNewToken() {
		return newToken;
	}

	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}

	public AtomicBoolean getNeedUpdateToken() {
		return needUpdateToken;
	}

	public void setNeedUpdateToken(AtomicBoolean needUpdateToken) {
		this.needUpdateToken = needUpdateToken;
	}

	public long getTokenGenTime() {
		return tokenGenTime;
	}

	public void setTokenGenTime(long tokenGenTime) {
		this.tokenGenTime = tokenGenTime;
	}

}
