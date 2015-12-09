package com.jiuyi.doctor.config;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

@Repository
public class ConfigDao extends DbBase {

	private static final String SELECT_DOCOTR_CONFIG = "SELECT * FROM `t_doctor_config` WHERE `doctorId`=?";
	private static final String UPDATE_DOCTOR_PUSH = "INSERT `t_doctor_config`(`doctorId`,`pushStatus`) VALUES(?,?) ON DUPLICATE KEY UPDATE `pushStatus`=?";

	protected DoctorConfig selectDoctorConfig(Doctor doctor) {
		return super.queryForObjectDefaultBuilder(SELECT_DOCOTR_CONFIG, new Object[] { doctor.getId() }, DoctorConfig.class);
	}

	protected void closePush(Doctor doctor) {
		jdbc.update(UPDATE_DOCTOR_PUSH, doctor.getId(), 1, 1);
	}

	protected void openPush(Doctor doctor) {
		jdbc.update(UPDATE_DOCTOR_PUSH, doctor.getId(), 0, 0);
	}

}
