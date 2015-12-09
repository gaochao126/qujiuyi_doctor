package com.jiuyi.doctor.patients.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 下午4:48:20
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ReturnVisit {

	private int id;
	private transient int doctorId;
	private int patientId;
	private Date visitDate;
	private String desc;

	public ReturnVisit(int id, int doctorId, int patientId, Date visitDate, String desc) {
		this.id = id;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.visitDate = visitDate;
		this.desc = desc;
	}

	public static final RowMapper<ReturnVisit> builder = new RowMapper<ReturnVisit>() {

		@Override
		public ReturnVisit mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int doctorId = rs.getInt("doctorId");
			int patientId = rs.getInt("patientId");
			Date visitDate = rs.getTimestamp("visitTime");
			String desc = rs.getString("description");
			return new ReturnVisit(id, doctorId, patientId, visitDate, desc);
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
