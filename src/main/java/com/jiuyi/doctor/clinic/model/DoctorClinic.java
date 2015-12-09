package com.jiuyi.doctor.clinic.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 上午9:52:25
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DoctorClinic implements ISerializableObj {

	private int doctorId;
	private int clinicId;
	private Date joinDate;
	private int position;
	private Doctor doctor;

	public static final RowMapper<DoctorClinic> builder = new RowMapper<DoctorClinic>() {
		@Override
		public DoctorClinic mapRow(ResultSet rs, int rowNum) throws SQLException {
			int doctorId = rs.getInt("doctorId");
			int clinicId = rs.getInt("clinicId");
			Date joinDate = rs.getDate("joinDate");
			int position = rs.getInt("position");
			return new DoctorClinic(doctorId, clinicId, joinDate, position);
		}
	};

	public DoctorClinic(int doctorId, int clinicId, Date joinDate, int position) {
		this.doctorId = doctorId;
		this.clinicId = clinicId;
		this.joinDate = joinDate;
		this.position = position;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getClinicId() {
		return clinicId;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public int getPosition() {
		return position;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("position", this.position);
		res.put("doctorId", this.doctorId);
		res.put("name", this.doctor.getName());
		res.put("title", DoctorTitle.getTitleNameById(this.doctor.getTitleId()));
		res.put("hospital", this.doctor.getHospital());
		return res;
	}

}
