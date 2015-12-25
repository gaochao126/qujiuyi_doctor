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
import com.jiuyi.frame.util.StringUtil;

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
		String headPath = StringUtil.isNullOrEmpty(headFileName) ? "" : dbConfig.getConfig("doctor.head.path") + headFileName;
		String idCardFileName = rs.getString("idCardPath");
		String idCardPath = StringUtil.isNullOrEmpty(idCardFileName) ? "" : dbConfig.getConfig("doctor.idcard.path") + idCardFileName;
		String titleCardFileName = rs.getString("titleCardPath");
		String titleCardPath = StringUtil.isNullOrEmpty(titleCardFileName) ? "" : dbConfig.getConfig("doctor.titlecard.path") + titleCardFileName;
		String licenseCardFileName = rs.getString("licenseCardPath");
		String licenseCardPath = StringUtil.isNullOrEmpty(licenseCardFileName) ? "" : dbConfig.getConfig("doctor.licensecard.path") + licenseCardFileName;
		int status = rs.getInt("status");
		int score = rs.getInt("recommendScore");
		int titleId = rs.getInt("titleId");
		String skill = rs.getString("skill");
		String experience = rs.getString("experience");
		String hospital = hospitalService.getNameById(hospitalId);
		String department = departmentService.getNameById(departmentId);
		String position = rs.getString("position");
		String graduationSchool = rs.getString("graduationSchool");
		int praisedNum = rs.getInt("praisedNum");
		String qrCodeImg = rs.getString("qrCodeImg");
		Doctor doctor = new Doctor(id, phone, name, hospital, department, headPath, idCardPath, titleCardPath, licenseCardPath, status, score, titleId, skill, experience, position, graduationSchool,
				praisedNum, qrCodeImg);
		doctor.setHospitalId(hospitalId);
		return doctor;

	}

}
