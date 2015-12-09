package com.jiuyi.doctor.appointment.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 上午11:12:26
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Appointment implements ISerializableObj {

	public long id;
	public int doctorId;
	public int patientId;
	public String patientHead;
	public String patientName;

	public String name;
	public int gender;
	public int age;
	public String phone;
	public String uid;

	public String symptoms;
	public String symptomsImages;
	public Date appointmentDate;
	public int timeZone;
	public Timestamp createDate;
	public int status;
	public int visitStatus;
	public String evaluation;
	public int satisfaction;

	public static RowMapper<Appointment> simple_builder = new RowMapper<Appointment>() {
		@Override
		public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			int doctorId = rs.getInt("doctorId");
			int patientId = rs.getInt("patientId");
			String name = rs.getString("name");
			String phone = rs.getString("phone");
			String uid = rs.getString("uid");
			String symptoms = rs.getString("symptoms");
			String symptomsImages = rs.getString("symptomsImages");
			Date appointmentDate = rs.getDate("appointmentDate");
			int timeZone = rs.getInt("timeZone");
			Timestamp createDate = rs.getTimestamp("createDate");
			int status = rs.getInt("status");
			int visitStatus = rs.getInt("visitStatus");

			String evaluation = rs.getString("evaluation");
			int satisfaction = rs.getInt("satisfaction");

			return new Appointment(id, doctorId, patientId, name, phone, uid, symptoms, symptomsImages, appointmentDate, timeZone, createDate, status, visitStatus, evaluation, satisfaction);
		}
	};

	public static RowMapper<Appointment> builder = new RowMapper<Appointment>() {
		@Override
		public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Appointment appointment = simple_builder.mapRow(rs, rowNum);
			String patientHead = DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + rs.getString("headPortrait");
			String patientName = rs.getString("name");
			int gender = rs.getInt("gender");
			Date birthday = rs.getDate("birthday");
			int age = DateUtil.getYearGap(birthday);
			appointment.patientHead = patientHead;
			appointment.patientName = patientName;
			appointment.gender = gender;
			appointment.age = age;
			return appointment;
		}
	};

	public Appointment(long id, int doctorId, int patientId, String name, String phone, String uid, String symptoms, String symptomsImages, Date appointmentDate, int timeZone, Timestamp createDate,
			int status, int visitStatus, String evaluation, int satisfaction) {
		this.id = id;
		this.doctorId = doctorId;
		this.patientId = patientId;
		this.name = name;
		this.phone = phone;
		this.uid = uid;
		this.symptoms = symptoms;
		this.symptomsImages = symptomsImages;
		this.appointmentDate = appointmentDate;
		this.timeZone = timeZone;
		this.createDate = createDate;
		this.status = status;
		this.visitStatus = visitStatus;
		this.evaluation = evaluation;
		this.satisfaction = satisfaction;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("patientId", this.patientId);
		res.put("patientName", this.patientName);
		res.put("patientHead", this.patientHead);
		res.put("name", this.name);
		res.put("gender", this.gender);
		res.put("age", this.age);
		res.put("phone", this.phone);
		res.put("uid", this.uid);
		res.put("symptoms", this.symptoms);
		res.put("symptomsImages", this.symptomsImages);
		res.put("appointmentDate", this.appointmentDate);
		res.put("timeZone", this.timeZone);
		res.put("createTime", DateUtil.date2Str(this.createDate));
		res.put("status", this.status);
		res.put("visitStatus", this.visitStatus);
		res.put("evaluation", this.evaluation);
		res.put("satisfaction", this.satisfaction);
		return res;
	}

}
