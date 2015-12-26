/**
 * 
 */
package com.jiuyi.doctor.prescription.model;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.annotations.ReadableDate;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.StringUtil;

/**
 * 处方的备注，包括审核药师的审核结果和医生的修改批注
 * 
 * @author xutaoyang
 *
 */
public class PrescriptionRemark {

	/** 医生备注 */
	public MapObject doctorRemark() {
		if (StringUtil.isNullOrEmpty(this.remark)) {
			return null;
		}
		MapObject res = new MapObject();
		res.put("content", this.remark);
		res.put("username", this.doctorName);
		res.put("date", this.remarkDate);
		res.put("userType", 1);
		return res;
	}

	/** 审核医师回复 */
	public MapObject reviewRemark() {
		if (StringUtil.isNullOrEmpty(this.reviewFailReason)) {
			return null;
		}
		MapObject res = new MapObject();
		res.put("content", this.reviewFailReason);
		res.put("username", this.reviewDoctorName);
		res.put("date", this.remarkDate);
		res.put("userType", 2);
		return res;
	}

	private String remark;
	@ReadableDate("yyyy-MM-dd HH:mm:ss")
	private String remarkDate;
	private String reviewFailReason;
	@ReadableDate("yyyy-MM-dd HH:mm:ss")
	private String reviewDate;
	private String reviewDoctorName;
	@Column("name")
	private String doctorName;

	public String getRemark() {
		return remark;
	}

	public String getReviewFailReason() {
		return reviewFailReason;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public String getReviewDoctorName() {
		return reviewDoctorName;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setReviewFailReason(String reviewFailReason) {
		this.reviewFailReason = reviewFailReason;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public void setReviewDoctorName(String reviewDoctorName) {
		this.reviewDoctorName = reviewDoctorName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getRemarkDate() {
		return remarkDate;
	}

	public void setRemarkDate(String remarkDate) {
		this.remarkDate = remarkDate;
	}

}
