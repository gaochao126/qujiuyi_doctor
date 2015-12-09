package com.jiuyi.doctor.praise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.builder.SimpleDoctorBuilder;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.SimpleDoctor;
import com.jiuyi.frame.base.DbBase;

@Repository
public class PraiseDao extends DbBase {

	private static final String SELECT_PRAISED_NUM = "SELECT `praisedNum` FROM `t_doctor` WHERE `id`=?";
	private static final String SELECT_PRAISED = "SELECT COUNT(*) FROM `t_doctor_praise` WHERE `doctorId`=? AND `praiseDoctorId`=?";
	private static final String SELECT_UN_PRAISED_DOCTOR = "SELECT * FROM `t_doctor` d JOIN (SELECT ROUND(RAND()*(MAX(id)-MIN(id)))+MIN(id) AS id FROM `t_doctor` WHERE `hospitalId`=?) tid ON d.id>=tid.id WHERE d.hospitalId=? AND d.`id`<>? AND d.`status`=3 AND NOT EXISTS(SELECT doctorId FROM t_doctor_praise p WHERE p.doctorId=? AND p.praiseDoctorId=d.id) LIMIT ?;";
	private static final String SELECT_UN_PRAISED_RAND_DOCTOR = "SELECT * FROM `t_doctor` d JOIN (SELECT ROUND(RAND()*(MAX(id)-MIN(id)))+MIN(id)-? AS id FROM `t_doctor`) tid ON d.id>=tid.id WHERE d.`id`<>? AND d.`status`=3 AND NOT EXISTS(SELECT doctorId FROM t_doctor_praise p WHERE p.doctorId=? AND p.praiseDoctorId=d.id) LIMIT ?";

	private static final String INSERT_PRAISED = "INSERT `t_doctor_praise`(`doctorId`,`praiseDoctorId`) VALUE(?,?)";
	private static final String DELETE_PRAISED = "DELETE FROM `t_doctor_praise` WHERE `doctorId`=? AND `praiseDoctorId`=?";
	private static final String UPDATE_PRAISED_NUM = "UPDATE `t_doctor` SET `praisedNum`=`praisedNum`+? WHERE `id`=?";

	@Autowired
	private SimpleDoctorBuilder simpleDoctorBuilder;

	protected void praise(Doctor doctor, Integer doctorId) {
		jdbc.update(INSERT_PRAISED, doctor.getId(), doctorId);
	}

	protected void cancelPraise(Doctor doctor, Integer doctorId) {
		jdbc.update(DELETE_PRAISED, doctor.getId(), doctorId);
	}

	protected boolean praised(Doctor doctor, Integer doctorId) {
		return queryForInteger(SELECT_PRAISED, doctor.getId(), doctorId) > 0;
	}

	protected int praisedNum(Integer doctorId) {
		return queryForInteger(SELECT_PRAISED_NUM, doctorId);
	}

	protected void incPraisedNum(Integer doctorId) {
		jdbc.update(UPDATE_PRAISED_NUM, 1, doctorId);
	}

	protected void decPraisedNum(Integer doctorId) {
		jdbc.update(UPDATE_PRAISED_NUM, -1, doctorId);
	}

	protected List<SimpleDoctor> unPraisedDoctor(Doctor doctor, int limit) {
		return jdbc.query(SELECT_UN_PRAISED_DOCTOR, new Object[] { doctor.getHospitalId(), doctor.getHospitalId(), doctor.getId(), doctor.getId(), limit }, simpleDoctorBuilder);
	}

	protected List<SimpleDoctor> unPraisedDoctorRand(Doctor doctor, int limit) {
		return jdbc.query(SELECT_UN_PRAISED_RAND_DOCTOR, new Object[] { limit, doctor.getId(), doctor.getId(), limit }, simpleDoctorBuilder);
	}
}
