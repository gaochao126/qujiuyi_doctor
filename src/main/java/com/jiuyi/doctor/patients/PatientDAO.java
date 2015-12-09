package com.jiuyi.doctor.patients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.patients.model.PatientGroup;
import com.jiuyi.doctor.patients.model.PatientGroupsBuilder;
import com.jiuyi.doctor.patients.model.PatientInfo;
import com.jiuyi.doctor.patients.model.ReturnVisit;
import com.jiuyi.doctor.patients.model.ReturnVisitBuilder;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.util.JDBCUtil;

/**
 * @Author: xutaoyang @Date: 下午6:32:34
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class PatientDAO extends DbBase {

	private static final String SELECT_PATIENT_COUNT = "SELECT COUNT(`id`) FROM `t_patient`;";
	private static final String SELECT_PATIENT_GROUP = "SELECT dp.patientId, pg.groupId FROM t_doctor_patient dp, t_patient_group pg WHERE pg.patientId = dp.patientId AND pg.groupId in (select id from t_doctor_group where doctorId=?) AND dp.doctorId=?";
	private static final String SELECT_PATIENT = "SELECT * FROM `t_patient` p WHERE p.`id`=?";
	private static final String PERSONAL_PATIENT = "SELECT count(`id`) FROM `t_personal_doctor` WHERE `expirationTime`>now() AND `doctorId`=? AND `patientId`=?";
	private static final String SELECT_PERSONAL_PATIENTS_ALL = "SELECT p.`id`, p.`nickname`,p.`gender`,p.`birthday`,p.`headPortrait`,p.`phone` FROM `t_patient` p, `t_personal_doctor` dp WHERE p.`id`=dp.`patientId` AND dp.`doctorId`=?";
	private static final String SELECT_PATIENTS = "SELECT p.`id`, p.`nickname`,p.`gender`,p.`birthday`,p.`headPortrait`,p.`phone` FROM `t_patient` p, `t_doctor_patient` dp WHERE p.`id`=dp.`patientId` AND dp.`doctorId`=?";
	private static final String SELECT_DOCTOR_GROUPS = "SELECT `id`,`name` FROM `t_doctor_group` WHERE `doctorId`=?";
	private static final String SELECT_ALL_RETURN_VISIT = "SELECT * FROM `t_return_visit` WHERE `doctorId`=? AND `visitTime`>now()";
	// private static final String SELECT_RETURN_VISIT =
	// "SELECT `id`,`patientId`,`visitTime`,`description` FROM `t_return_visit` WHERE `doctorId`=? AND `patientId`=?";
	private static final String SELECT_SEARCH_PATIENT = "SELECT `id`,`nickname`,`phone`,`gender`,`headPortrait`,`address`,`allergicHistory` FROM `t_patient` WHERE `phone`=? OR `nickname` LIKE ? LIMIT 30";
	private static final String SELECT_DOCTER_PATIENT = "SELECT COUNT(`patientId`) FROM `t_doctor_patient` WHERE `doctorId`=? AND `patientId`=?";
	private static final String RECOMMEND_PATIENT = "SELECT * FROM `t_patient` LIMIT ?,?";
	private static final String PATIENT_EXIST = "SELECT COUNT(`id`) FROM `t_patient` WHERE `id`=?";
	private static final String PATIENT_EXIST_IN_GROUP = "SELECT COUNT(`id`) FROM `t_patient_group` WHERE `patientId`=? AND `groupId`=?";

	private static final String ADD_PATIENT = "REPLACE INTO `t_doctor_patient`(`doctorId`,`patientId`) VALUES(?,?)";
	// private static final String GROUP_OF_PATIENT =
	// "SELECT `groupId` FROM `t_patient_group` WHERE `patientId`=?";
	private static final String ADD_PATIENT_GROUP = "INSERT INTO `t_doctor_group`(`doctorId`,`name`) VALUES(?,?)";
	private static final String DELETE_PATIENT_GROUP = "DELETE FROM `t_doctor_group` WHERE `doctorId`=? AND `id`=?";
	private static final String UPATE_PATIENT_GROUP = "UPDATE `t_doctor_group` SET `name`=? WHERE `doctorId`=? AND `id`=?";

	private static final String MOVE_IN = "INSERT INTO `t_patient_group`(`patientId`,`groupId`) VALUES(?,?)";
	private static final String MOVE_OUT = "DELETE FROM `t_patient_group` WHERE `patientId`=? AND `groupId`=?";

	private static final String ADD_RETURN_VISIT = "INSERT INTO `t_return_visit`(`doctorId`,`patientId`,`visitTime`,`description`) VALUES(?,?,?,?)";
	private static final String DEL_RETURN_VISIT = "DELETE FROM `t_return_visit` WHERE `id`=? AND `doctorId`=?";

	private PatientGroupsBuilder patientGroupsBuilder = new PatientGroupsBuilder();
	private ReturnVisitBuilder returnVisitBuilder = new ReturnVisitBuilder();

	public Integer loadPatientCount() {
		return queryForInteger(SELECT_PATIENT_COUNT);
	}

	protected List<PatientInfo> loadPatients(Doctor doctor) {
		List<PatientInfo> res = new ArrayList<>();
		res.addAll(loadCommonPatients(doctor));
		res.addAll(loadPersonalPatients(doctor));
		return res;
	}

	protected List<PatientInfo> loadCommonPatients(Doctor doctor) {
		List<PatientInfo> patients = loadDoctorPatients(doctor, SELECT_PATIENTS);
		for (PatientInfo patientInfo : patients) {
			patientInfo.setType(PatientGroupManager.TYPE_COMMON);
		}
		return patients;
	}

	protected List<PatientInfo> loadPersonalPatients(Doctor doctor) {
		List<PatientInfo> privatePatients = loadDoctorPatients(doctor, SELECT_PERSONAL_PATIENTS_ALL);
		for (PatientInfo patientInfo : privatePatients) {
			patientInfo.setType(PatientGroupManager.TYPE_PRIVATE);
		}
		return privatePatients;
	}

	protected List<PatientInfo> loadStrangePatient(Doctor doctor) {
		return null;
	}

	protected List<ReturnVisit> loadAllReturnvisits(Doctor doctor) {
		return jdbc.query(SELECT_ALL_RETURN_VISIT, new Object[] { doctor.getId() }, ReturnVisit.builder);
	}

	protected Map<Integer, List<ReturnVisit>> loadPatientReturnVisit(Doctor doctor) {
		return jdbc.query(SELECT_ALL_RETURN_VISIT, new Object[] { doctor.getId() }, returnVisitBuilder);
	}

	protected boolean isPersonalPatient(int doctorId, int patientId) {
		return queryForInteger(PERSONAL_PATIENT, new Object[] { doctorId, patientId }) > 0;
	}

	protected Map<Integer, List<Integer>> loadPatientGroup(Doctor doctor) {
		return jdbc.query(SELECT_PATIENT_GROUP, new Object[] { doctor.getId(), doctor.getId() }, patientGroupsBuilder);
	}

	protected PatientInfo loadPatient(int patientId) {
		return queryForObject(SELECT_PATIENT, new Object[] { patientId }, PatientInfo.simpleBuilder);
	}

	protected List<PatientInfo> searchPatient(String key) {

		return jdbc.query(SELECT_SEARCH_PATIENT, new Object[] { key, "%" + key + "%" }, PatientInfo.simpleBuilder);
	}

	/** patientId 是否在doctor的患者群里面 */
	protected boolean hasPatient(Doctor doctor, int patientId) {
		return jdbc.queryForObject(SELECT_DOCTER_PATIENT, new Object[] { doctor.getId(), patientId }, Integer.class) > 0;
	}

	protected boolean patientExist(int patientId) {
		return queryForInteger(PATIENT_EXIST, new Object[] { patientId }) > 0;
	}

	protected void addPatient(Doctor doctor, int patientId) {
		jdbc.update(ADD_PATIENT, doctor.getId(), patientId);
	}

	protected List<PatientGroup> loadGroup(Doctor doctor) {
		return jdbc.query(SELECT_DOCTOR_GROUPS, new Object[] { doctor.getId() }, PatientGroup.builder);
	}

	protected int addGroup(final Doctor doctor, final String groupName) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(ADD_PATIENT_GROUP, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, doctor.getId());
				ps.setString(2, groupName);
				return ps;
			}
		};
		jdbc.update(psc, holder);
		int id = holder.getKey().intValue();
		return id;
	}

	protected void deleteGroup(Doctor doctor, Integer[] groupIds) {
		List<Object[]> args = new ArrayList<Object[]>(groupIds.length);
		for (Integer groupId : groupIds) {
			args.add(new Object[] { doctor.getId(), groupId });
		}
		jdbc.batchUpdate(DELETE_PATIENT_GROUP, args);
	}

	protected void updateGroup(Doctor doctor, int groupId, String newGroupName) {
		jdbc.update(UPATE_PATIENT_GROUP, new Object[] { newGroupName, doctor.getId(), groupId });
	}

	protected void moveIn(Doctor doctor, int groupId, int patientId) {
		jdbc.update(MOVE_IN, new Object[] { patientId, groupId });
	}

	protected void moveOut(Doctor doctor, int groupId, int patientId) {
		jdbc.update(MOVE_OUT, new Object[] { patientId, groupId });
	}

	protected boolean patientExistInGroup(int patientId, int groupId) {
		return queryForInteger(PATIENT_EXIST_IN_GROUP, new Object[] { patientId, groupId }) > 0;
	}

	protected void moveInBatch(Doctor doctor, int groupId, Integer[] patientIds) {
		jdbc.batchUpdate(MOVE_IN, JDBCUtil.toBatchArgs(patientIds, groupId));
	}

	protected void moveOutBatch(Doctor doctor, int groupId, Integer[] patientIds) {
		jdbc.batchUpdate(MOVE_OUT, JDBCUtil.toBatchArgs(patientIds, groupId));
	}

	protected int addReturnVisit(final Doctor doctor, final int patientId, final Date visitDate, final String desc) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(ADD_RETURN_VISIT, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, doctor.getId());
				ps.setInt(2, patientId);
				ps.setDate(3, new java.sql.Date(visitDate.getTime()));
				ps.setString(4, desc);
				return ps;
			}
		};
		jdbc.update(psc, holder);
		int id = holder.getKey().intValue();
		return id;
	}

	protected void delReturnVisit(Doctor doctor, int id) {
		jdbc.update(DEL_RETURN_VISIT, new Object[] { id, doctor.getId() });
	}

	public List<PatientInfo> recommendPatient(Doctor doctor) {
		String sql = "select * from `t_patient` limit 10";
		return jdbc.query(sql, PatientInfo.simpleBuilder);
	}

	public List<PatientInfo> recommendPatient(Doctor doctor, int start, int size) {
		return jdbc.query(RECOMMEND_PATIENT, new Object[] { start, size }, PatientInfo.simpleBuilder);
	}

	private List<PatientInfo> loadDoctorPatients(Doctor doctor, String sql) {
		return jdbc.query(sql, new Object[] { doctor.getId() }, PatientInfo.builder);
	}
}
