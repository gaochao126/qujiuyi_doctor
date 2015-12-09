package com.jiuyi.doctor.user.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.doctor.user.model.SimpleDoctor;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.util.StringUtil;

@Component
public class SimpleDoctorBuilder implements RowMapper<SimpleDoctor> {
	@Autowired
	private HospitalService hospitalService;

	@Autowired
	private DBConfig dbConfig;

	@Override
	public SimpleDoctor mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int hospitalId = rs.getInt("hospitalId");
		String headFileName = rs.getString("head");
		String headPath = StringUtil.isNullOrEmpty(headFileName) ? "" : dbConfig.getConfig("doctor.head.path") + headFileName;
		int titleId = rs.getInt("titleId");
		String hospital = hospitalService.getNameById(hospitalId);
		String title = DoctorTitle.getTitleNameById(titleId);
		return new SimpleDoctor(id, name, headPath, title, hospital);
	}

}
