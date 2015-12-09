package com.jiuyi.doctor.clinic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.clinic.model.Clinic;
import com.jiuyi.doctor.clinic.model.DoctorClinic;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午3:38:44
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class ClinicDao extends DbBase {

	private static final String SELECT_CLINIC_BY_DOCTOR = "SELECT c.* FROM `t_clinic` c,`t_doctor_clinic` dc WHERE c.`id`=dc.`clinicId` AND dc.`doctorId`=?";
	private static final String SELECT_CLINIC_DOCTORIDS = "SELECT * FROM `t_doctor_clinic` WHERE `clinicId`=?";

	private static final String NEW_CLINIC = "INSERT `t_clinic`(`name`,`slogan`) VALUE(?,?)";
	private static final String INSERT_DOCTOR_CLINIC = "INSERT `t_doctor_clinic`(`doctorId`,`clinicId`,`position`) VALUE(?,?,?)";

	private static final String UPDATE_SLOGAN = "UPDATE `t_clinic` SET `slogan`=? WHERE `id`=?";

	protected int newClinic(final Doctor doctor, final Clinic clinic) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(NEW_CLINIC, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, clinic.getName());
				ps.setString(2, clinic.getSlogan());
				return ps;
			}
		};
		jdbc.update(psc, holder);
		return holder.getKey().intValue();
	}

	protected Clinic loadClinic(Doctor doctor) {
		try {
			return jdbc.queryForObject(SELECT_CLINIC_BY_DOCTOR, new Object[] { doctor.getId() }, Clinic.builder);
		} catch (DataAccessException e) {
			return null;
		}
	}

	protected List<DoctorClinic> loadDoctorClinic(Clinic clinic) {
		return jdbc.query(SELECT_CLINIC_DOCTORIDS, new Object[] { clinic.getId() }, DoctorClinic.builder);
	}

	protected void addDoctor2Clinic(Doctor doctor, Clinic clinic, DoctorPosition position) {
		jdbc.update(INSERT_DOCTOR_CLINIC, new Object[] { doctor.getId(), clinic.getId(), position.ordinal() });
	}

	protected void updateSlogan(Clinic clinic, String slogan) {
		jdbc.update(UPDATE_SLOGAN, new Object[] { slogan, clinic.getId() });
	}

}
