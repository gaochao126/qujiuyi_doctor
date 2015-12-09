package com.jiuyi.doctor.user.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.hospitals.DepartmentService;
import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.hospitals.model.Department;
import com.jiuyi.doctor.hospitals.model.DoctorTitle;
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
public class RecognizeDoctorBuilder implements RowMapper<RecognizeDoctor> {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	DBConfig dbConfig;

	@Override
	public RecognizeDoctor mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int hospitalId = rs.getInt("hospitalId");
		int departmentId = rs.getInt("departmentId");// 这个是线下的科室，需要转换为线上的科室
		Department standardDepartment = departmentService.getStandardDepartmentId(hospitalId, departmentId);
		String officePhone = "";
		String headFileName = rs.getString("head");
		String headPath = StringUtil.isNullOrEmpty(headFileName) ? "" : dbConfig.getConfig("doctor.head.path") + headFileName;

		int status = 0;
		int titleId = rs.getInt("titleId");

		String skill = rs.getString("skill");
		String experience = rs.getString("experience");

		String hospital = hospitalService.getNameById(hospitalId);
		String title = DoctorTitle.getTitleNameById(titleId);
		String position = rs.getString("position");

		return new RecognizeDoctor(id, name, hospitalId, hospital, standardDepartment.getId(), standardDepartment.getName(), officePhone, headPath, status, titleId, title, skill, experience,
				position);
	}
}
