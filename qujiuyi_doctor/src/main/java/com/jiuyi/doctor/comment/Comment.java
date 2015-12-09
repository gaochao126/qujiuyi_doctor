package com.jiuyi.doctor.comment;

import com.jiuyi.frame.annotations.Age;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.annotations.ReadableDate;
import com.jiuyi.frame.constants.Constants;

public class Comment {

	private int id;
	private int patientId;
	private String patientName;
	private int patientGender;
	private @Age int patientAge;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String patientHead;
	private int serviceType;
	private String serviceId;
	private String commentContent;
	private int satisfaction;
	@ReadableDate
	private String commentTime;

	public int getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(int patientAge) {
		this.patientAge = patientAge;
	}

	public int getId() {
		return id;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getServiceType() {
		return serviceType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getPatientName() {
		return patientName;
	}

	public String getPatientHead() {
		return patientHead;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public void setPatientHead(String patientHead) {
		this.patientHead = patientHead;
	}

	public int getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(int patientGender) {
		this.patientGender = patientGender;
	}

}
