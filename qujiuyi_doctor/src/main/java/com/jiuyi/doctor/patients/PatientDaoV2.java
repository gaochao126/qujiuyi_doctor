package com.jiuyi.doctor.patients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.patients.model.DoctorPatient;
import com.jiuyi.doctor.patients.model.DoctorPatientRelation;
import com.jiuyi.doctor.patients.model.Patient;
import com.jiuyi.doctor.patients.model.RelativePatient;
import com.jiuyi.doctor.patients.model.Tag;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午5:04:57
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class PatientDaoV2 extends DbBase {

	// @formatter:off
	private static final String SELECT_PERSONAL_PATIENTS_COUNT = "SELECT COUNT(*) FROM `t_personal_doctor` WHERE `doctorId`=?";

	private static final String SELECT_UNFAMILIAR_COUNT = "SELECT count(patientId) FROM t_doctor_remark_patient WHERE relation=? AND doctorId=?";

	private static final String SELECT_PATIENT = "SELECT * FROM `t_patient` WHERE `id`=?";

	private static final String SELECT_DOCTOR_PATIENT = "SELECT * FROM `t_doctor_remark_patient` WHERE `doctorId`=? AND `patientId`=?";

	private static final String SELECT_PATIENTS_BY_RELATION = "SELECT relation.patientId,p.name,p.gender,p.phone,p.headPortrait,p.birthday AS age,relation.relation,relation.note "
			+ "FROM `t_doctor_remark_patient` relation "
			+ "JOIN `t_patient` p ON relation.patientId =p.id "
			+ "WHERE relation.relation=? AND relation.doctorId=?";
	
	private static final String SELECT_PERSONAL_PATIENTS = "SELECT relation.patientId,p.name,p.gender,p.phone,p.headPortrait,p.birthday AS age,relation.relation,relation.note "
			+ "FROM `t_personal_doctor` personal "
			+ "JOIN `t_patient` p ON p.id=personal.patientId "
			+ "JOIN `t_doctor_remark_patient` relation ON relation.patientId=personal.patientId "
			+ "WHERE personal.doctorId=?";
	
	private static final String SELECT_SIMPLE_PATIENT_BY_TAG = "SELECT c.*,p.`name`,p.`gender`,p.`headPortrait`,p.`birthday` AS age,r.remark,r.note "
			+ "FROM `t_patient_tags` c "
			+ "JOIN `t_patient` p ON c.patientId =p.id "
			+ "LEFT JOIN `t_doctor_remark_patient` r ON r.patientId=c.patientId AND r.doctorId=? "
			+ "WHERE c.`tagId`=?;";
	
	private static final String SELECT_PATIENT_DETAIL = "SELECT p.id as patientId,p.*,r.remark,r.note,r.relation "
			+ "FROM `t_patient` p "
			+ "LEFT JOIN `t_doctor_remark_patient` r ON r.doctorId=? AND r.patientId=p.id "
			+ "WHERE p.id=?;";
	
	private static final String SELECT_DOCTOR_PATIENT_TYPE = "SELECT `relation` from `t_doctor_remark_patient` WHERE `doctorId`=? AND `patientId`=?";
	
	private static final String SELECT_PATIENT_BY_PHONE = "SELECT patient.id as patientId,patient.*,remark.remark,remark.relation "
			+ "FROM `t_patient` patient "
			+ "LEFT JOIN `t_doctor_remark_patient` remark ON remark.patientId=patient.id "
			+ "WHERE patient.phone=? AND remark.doctorId=?";

	private static final String SELECT_TAGS = "SELECT * FROM `t_doctor_tags` WHERE `doctorId`=?";
	private static final String SELECT_TAGS_PATIENTS = "SELECT * FROM `t_patient_tags` WHERE `tagId` IN (#tagIds#)";
	private static final String SELECT_RELATIVE_PATIENT = "SELECT * FROM `t_patient_relative` WHERE `patientId`=?";
	
	private static final String SELECT_PERSONAL_DOCTOR = "SELECT COUNT(*) FROM `t_personal_doctor` WHERE `expirationTime`>now() AND `doctorId`=? AND `patientId`=?";

	private static final String SEARCH_MY_PATIENTS = "SELECT patient.id as patientId,patient.*,remark.remark,remark.relation "
			+ "FROM `t_patient` patient "
			+ "LEFT JOIN `t_doctor_remark_patient` remark ON remark.patientId=patient.id "
			+ "WHERE (patient.name LIKE :key OR remark.remark LIKE :key) AND remark.doctorId=:doctorId";

	private static final String SEARCH_MY_PATIENTS_BY_RELATION = SEARCH_MY_PATIENTS + " AND remark.relation=:relation";
	
	private static final String SEARCH_MY_PATIENTS_BY_RELATIONS = SEARCH_MY_PATIENTS + " AND remark.relation IN(:relation)";
	
	private static final String INSERT_DOCTOR_PATIENT_RELATION = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`relation`,`src`) VALUE(?,?,?,?) ON DUPLICATE KEY UPDATE `relation`=?";

	private static final String UPDATE_REMARK = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`remark`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `remark`=?";
	private static final String UPDATE_NOTE = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`note`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `note`=?";
	private static final String UPDATE_RELATION = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`relation`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `relation`=?";

	private static final String ADD_TAG = "INSERT `t_doctor_tags`(`doctorId`,`name`) VALUE(?,?)";
	private static final String DEL_TAG = "DELETE FROM `t_doctor_tags` WHERE `id`=? AND `doctorId`=?";
	private static final String UPDATE_TAG_NAME = "UPDATE `t_doctor_tags` SET `name`=? WHERE `id`=?";

	private static final String INSERT_TAG_PATIENT = "INSERT `t_patient_tags`(`patientId`,`tagId`) VALUE(?,?)";
	private static final String REMOVE_TAG_PATIENT = "DELETE FROM `t_patient_tags` WHERE `patientId`=? AND `tagId`=?";
	// @formatter:on

	protected int loadUnfamiliarCount(Doctor doctor) {
		return queryForInteger(SELECT_UNFAMILIAR_COUNT, DoctorPatientRelation.UNFAMILIAR.ordinal(), doctor.getId());
	}

	protected DoctorPatient loadDoctorPatient(Doctor doctor, Integer patientId) {
		return queryForObjectDefaultBuilder(SELECT_DOCTOR_PATIENT, new Object[] { doctor.getId(), patientId }, DoctorPatient.class);
	}

	protected List<Patient> loadPatientsByRelation(Doctor doctor, DoctorPatientRelation relation) {
		return queryForListPage(SELECT_PATIENTS_BY_RELATION, new Object[] { relation.ordinal(), doctor.getId() }, Patient.class);
	}

	protected boolean isPersonal(Doctor doctor, int patientId) {
		return queryForInteger(SELECT_PERSONAL_DOCTOR, doctor.getId(), patientId) > 0;
	}

	protected void insertDoctorPatientRelation(Doctor doctor, DoctorPatient doctorPatient) {
		jdbc.update(INSERT_DOCTOR_PATIENT_RELATION, doctor.getId(), doctorPatient.getPatientId(), doctorPatient.getRelation(), doctorPatient.getSrc(), doctorPatient.getRelation());
	}

	protected List<Tag> loadTags(Doctor doctor) {
		return jdbc.query(SELECT_TAGS, new Object[] { doctor.getId() }, Tag.builder);
	}

	protected int addTag(final Doctor doctor, final String name) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(ADD_TAG, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, doctor.getId());
				ps.setString(2, name);
				return ps;
			}
		};
		jdbc.update(psc, holder);
		int id = holder.getKey().intValue();
		return id;
	}

	protected void removeTag(Doctor doctor, Integer tagId) {
		jdbc.update(DEL_TAG, new Object[] { tagId, doctor.getId() });
	}

	protected List<Patient> loadSimplePatientByTag(Doctor doctor, Integer tagId) {
		return queryForListPage(SELECT_SIMPLE_PATIENT_BY_TAG, new Object[] { doctor.getId(), tagId }, Patient.class);
	}

	protected void updateTagName(Doctor doctor, Integer tagId, String name) {
		jdbc.update(UPDATE_TAG_NAME, name, tagId);
	}

	protected void addTagPatients(Doctor doctor, Integer tagId, List<Integer> patientIds) {
		List<Object[]> batchArgs = new ArrayList<>(patientIds.size());
		for (Integer patientId : patientIds) {
			batchArgs.add(new Object[] { patientId, tagId });
		}
		jdbc.batchUpdate(INSERT_TAG_PATIENT, batchArgs);
	}

	protected void removeTagPatients(Doctor doctor, Integer tagId, List<Integer> patientIds) {
		List<Object[]> batchArgs = new ArrayList<>(patientIds.size());
		for (Integer patientId : patientIds) {
			batchArgs.add(new Object[] { patientId, tagId });
		}
		jdbc.batchUpdate(REMOVE_TAG_PATIENT, batchArgs);
	}

	protected Map<Integer, List<Integer>> loadTagPatients(List<Integer> tagIds) {
		String cmd = SELECT_TAGS_PATIENTS.replace("#tagIds#", StringUtil.joinArr(tagIds, ","));
		List<Map<String, Object>> tagPatientsList = jdbc.queryForList(cmd);
		Map<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
		for (Map<String, Object> tagPatients : tagPatientsList) {
			Integer tagId = (Integer) tagPatients.get("tagId");
			Integer patientId = (Integer) tagPatients.get("patientId");
			List<Integer> patientIds = res.get(tagId);
			if (patientIds == null) {
				patientIds = new ArrayList<>();
				res.put(tagId, patientIds);
			}
			patientIds.add(patientId);
		}
		return res;
	}

	protected void updateRemark(Doctor doctor, Integer patientId, String remark) {
		jdbc.update(UPDATE_REMARK, doctor.getId(), patientId, remark, remark);
	}

	protected void updateNote(Doctor doctor, Integer patientId, String note) {
		jdbc.update(UPDATE_NOTE, doctor.getId(), patientId, note, note);
	}

	protected void updateRelation(Doctor doctor, Integer patientId, Integer relation) {
		jdbc.update(UPDATE_RELATION, doctor.getId(), patientId, relation, relation);
	}

	protected int loadPersonalPatientCount(Doctor doctor) {
		return queryForInteger(SELECT_PERSONAL_PATIENTS_COUNT, doctor.getId());
	}

	/** 私人患者列表 */
	protected List<Patient> loadSimplePersonal(Doctor doctor) {
		return queryForListPage(SELECT_PERSONAL_PATIENTS, new Object[] { doctor.getId() }, Patient.class);
	}

	protected Patient loadPatientDetailInfo(Doctor doctor, Integer patientId) {
		return queryForObjectDefaultBuilder(SELECT_PATIENT_DETAIL, new Object[] { doctor.getId(), patientId }, Patient.class);
	}

	/** 移除患者的指定tag */
	protected void removePatientTags(Doctor doctor, Integer patientId, List<Integer> tagIds) {
		List<Object[]> args = new ArrayList<>(tagIds.size());
		for (Integer tagId : tagIds) {
			args.add(new Object[] { patientId, tagId });
		}
		jdbc.batchUpdate(REMOVE_TAG_PATIENT, args);
	}

	/** 给患者添加指定tag */
	protected void addPatientTags(Doctor doctor, Integer patientId, List<Integer> tagIds) {
		List<Object[]> args = new ArrayList<>(tagIds.size());
		for (Integer tagId : tagIds) {
			args.add(new Object[] { patientId, tagId });
		}
		jdbc.batchUpdate(INSERT_TAG_PATIENT, args);
	}

	protected Integer loadDoctorPatientType(Doctor doctor, Integer patientId) {
		return queryForInteger(SELECT_DOCTOR_PATIENT_TYPE, doctor.getId(), patientId);
	}

	/**
	 * @param patientId
	 * @return
	 */
	protected Patient loadPatient(Integer patientId) {
		return queryForObjectDefaultBuilder(SELECT_PATIENT, new Object[] { patientId }, Patient.class);
	}

	/**
	 * @param doctor
	 * @param phone
	 * @return
	 */
	protected Patient loadPatientByPhone(Doctor doctor, String phone) {
		return queryForObjectDefaultBuilder(SELECT_PATIENT_BY_PHONE, new Object[] { phone, doctor.getId() }, Patient.class);
	}

	protected List<RelativePatient> loadRelativePatients(Doctor doctor, int patientId) {
		return queryForList(SELECT_RELATIVE_PATIENT, new Object[] { patientId }, RelativePatient.class);
	}

	protected List<Patient> searchMyPatient(Doctor doctor, String key) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("key", "%" + key + "%");
		param.addValue("doctorId", doctor.getId());
		return queryForListPage(SEARCH_MY_PATIENTS, param, Patient.class);
	}

	protected List<Patient> searchMyPatientByRelation(Doctor doctor, String key, DoctorPatientRelation relation) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("key", "%" + key + "%");
		param.addValue("doctorId", doctor.getId());
		param.addValue("relation", relation.ordinal());
		return queryForListPage(SEARCH_MY_PATIENTS_BY_RELATION, param, Patient.class);
	}

	protected List<Patient> searchMyPatientByRelations(Doctor doctor, String key, DoctorPatientRelation... relations) {
		List<Integer> relationIds = new ArrayList<>(relations.length);
		for (DoctorPatientRelation relation : relations) {
			relationIds.add(relation.ordinal());
		}
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("key", "%" + key + "%");
		param.addValue("doctorId", doctor.getId());
		param.addValue("relation", relationIds);
		return queryForListPage(SEARCH_MY_PATIENTS_BY_RELATIONS, param, Patient.class);
	}
}
