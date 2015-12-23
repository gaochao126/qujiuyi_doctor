package com.jiuyi.doctor.medicalkit;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

@Repository
public class MedicalKitDao extends DbBase {

	private static final String CHECK_COLLECT = "SELECT COUNT(*) FROM `t_doctor_medicine_collection` WHERE `formatId`=? AND `doctorId`=?";//
	private static final String SELECT_MY_LIST = "SELECT `formatId` FROM `t_doctor_medicine_collection` WHERE `doctorId`=? LIMIT ?,?";

	private static final String INSERT_COLLECT = "INSERT `t_doctor_medicine_collection`(`doctorId`,`formatId`) VALUE(?,?)";
	private static final String DELETE_COLLECT = "DELETE FROM `t_doctor_medicine_collection` WHERE `formatId`=? AND `doctorId`=?";

	protected int checkCollectExist(Doctor doctor, String formatId) {
		return queryForInteger(CHECK_COLLECT, formatId, doctor.getId());
	}

	protected void collect(Doctor doctor, String formatId) {
		jdbc.update(INSERT_COLLECT, doctor.getId(), formatId);
	}

	protected List<String> loadMyList(Doctor doctor, int page, int pageSize) {
		return jdbc.queryForList(SELECT_MY_LIST, String.class, doctor.getId(), startIndex(page, pageSize), pageSize);
	}

	/**
	 * @param doctor
	 * @param formatId
	 */
	protected void deleteCollect(Doctor doctor, String formatId) {
		jdbc.update(DELETE_COLLECT, formatId, doctor.getId());
	}

}
