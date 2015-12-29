package com.jiuyi.doctor.user.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.hospitals.DepartmentService;
import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.conf.DBConfig;

/**
 * @Author: xutaoyang @Date: 下午2:03:43
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class DoctorBuilder implements RowMapper<Doctor> {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	DBConfig dbConfig;

	@Override
	public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("id");
		String phone = rs.getString("phone");
		String name = rs.getString("name");
		// String email = rs.getString("email");
		int hospitalId = rs.getInt("hospitalId");
		int departmentId = rs.getInt("departmentId");
		String headFileName = rs.getString("head");
		String idCardFileName = rs.getString("idCardPath");
		String titleCardFileName = rs.getString("titleCardPath");
		String licenseCardFileName = rs.getString("licenseCardPath");
		int status = rs.getInt("status");
		int score = rs.getInt("recommendScore");
		int titleId = rs.getInt("titleId");
		String officePhone = rs.getString("officePhone");
		String skill = rs.getString("skill");
		String experience = rs.getString("experience");
		String hospital = hospitalService.getNameById(hospitalId);
		String department = departmentService.getNameById(departmentId);
		String position = rs.getString("position");
		String graduationSchool = rs.getString("graduationSchool");
		int praisedNum = rs.getInt("praisedNum");
		String qrCodeImg = rs.getString("qrCodeImg");
		int offlineId = rs.getInt("offlineId");
		int editStatus = rs.getInt("editStatus");
		Doctor doctor = new Doctor(id, phone, name, hospital, department, headFileName, idCardFileName, titleCardFileName, licenseCardFileName, status, score, titleId, skill, experience, position,
				graduationSchool, praisedNum, qrCodeImg);
		doctor.setHospitalId(hospitalId);
		doctor.setOfflineId(offlineId);
		doctor.setEditStatus(editStatus);
		doctor.setDepartmentId(departmentId);
		doctor.setOfficePhone(officePhone);
		return doctor;

	}

}
