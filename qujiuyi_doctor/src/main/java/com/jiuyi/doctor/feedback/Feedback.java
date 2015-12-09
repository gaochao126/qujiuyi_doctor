package com.jiuyi.doctor.feedback;

/**
 * @Author: xutaoyang @Date: 下午1:42:03
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Feedback {

	private int doctorId;
	private int userType;
	private String content;
	private String contactWay;

	public Feedback(int doctorId, String content, String contactWay) {
		this.doctorId = doctorId;
		this.userType = 1;
		this.content = content;
		this.contactWay = contactWay;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getUserType() {
		return userType;
	}

	public String getContent() {
		return content;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

}
