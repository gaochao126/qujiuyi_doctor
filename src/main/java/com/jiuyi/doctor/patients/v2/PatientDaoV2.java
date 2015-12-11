package com.jiuyi.doctor.patients.v2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.patients.v2.model.ContactSrc;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.patients.v2.model.Tag;
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

	private static final String SELECT_PERSONAL_PATIENTS_COUNT = "SELECT COUNT(*) FROM `t_personal_doctor` WHERE `doctorId`=?";
	private static final String SELECT_UNFAMILIAR_COUNT = "SELECT count(patientId) FROM t_doctor_unfamiliar_patient WHERE doctorId=? ";

	private static final String SELECT_SIMPLE_PATIENT = "SELECT c.*,p.name,p.gender,p.headPortrait,p.birthday as age,r.remark,r.note FROM `#tableName#` c join `t_patient` p ON c.patientId =p.id LEFT JOIN `t_doctor_remark_patient` r ON r.patientId=c.patientId AND r.doctorId=c.doctorId WHERE c.`#where#`=?;";
	private static final String SELECT_CONTACTS = SELECT_SIMPLE_PATIENT.replace("#tableName#", "t_doctor_contacts").replace("#where#", "doctorId");
	private static final String SELECT_UNFAMILIAR = SELECT_SIMPLE_PATIENT.replace("#tableName#", "t_doctor_unfamiliar_patient").replace("#where#", "doctorId");
	private static final String SELECT_BLACKLIST = SELECT_SIMPLE_PATIENT.replace("#tableName#", "t_doctor_blacklist_patient").replace("#where#", "doctorId");
	private static final String SELECT_PERSONAL_PATIENTS = SELECT_SIMPLE_PATIENT.replace("#tableName#", "t_personal_doctor").replace("#where#", "doctorId");
	private static final String SELECT_SIMPLE_PATIENT_BY_TAG = "SELECT c.*,p.`name`,p.`gender`,p.`headPortrait`,p.`birthday` AS age,r.remark,r.note FROM `t_patient_tags` c JOIN `t_patient` p ON c.patientId =p.id LEFT JOIN `t_doctor_remark_patient` r ON r.patientId=c.patientId AND r.doctorId=? WHERE c.`tagId`=?;";
	private static final String SELECT_SIMPLE_PATIENT_BATCH = "SELECT p.`id`,p.`name`,p.`gender`,p.`headPortrait`,p.`birthday` AS age FROM `t_patient` p WHERE p.`id` IN (#patientIds#)";
	private static final String SELECT_PATIENT_DETAIL = "SELECT p.*,r.remark,r.note,r.relation as type FROM `t_patient` p LEFT JOIN `t_doctor_remark_patient` r ON r.doctorId=? AND r.patientId=p.id WHERE p.id=?;";
	private static final String SELECT_DOCTOR_PATIENT_TYPE = "SELECT `relation` from `t_doctor_remark_patient` WHERE `doctorId`=? AND `patientId`=?";

	private static final String SELECT_TAGS = "SELECT * FROM `t_doctor_tags` WHERE `doctorId`=?";
	private static final String SELECT_TAGS_PATIENTS = "SELECT * FROM `t_patient_tags` WHERE `tagId` IN (#tagIds#)";
	private static final String SELECT_CONTACT_SRC = "SELECT `src` FROM `t_doctor_contacts` WHERE `doctorId`=? AND `patientId`=?";

	private static final String ADD_CONTACT = "INSERT `t_doctor_contacts`(`doctorId`,`patientId`,`src`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `src`=?";
	private static final String ADD_UNFAMILIAR = "INSERT `t_doctor_unfamiliar_patient`(`doctorId`,`patientId`) VALUE(?,?) ON DUPLICATE KEY UPDATE id=id";
	private static final String ADD_BLACKLIST = "INSERT `t_doctor_blacklist_patient`(`doctorId`,`patientId`) VALUE(?,?) ON DUPLICATE KEY UPDATE id=id";

	private static final String UPDATE_REMARK = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`remark`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `remark`=?";
	private static final String UPDATE_NOTE = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`note`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `note`=?";
	private static final String UPDATE_RELATION = "INSERT `t_doctor_remark_patient`(`doctorId`,`patientId`,`relation`) VALUE(?,?,?) ON DUPLICATE KEY UPDATE `relation`=?";

	private static final String REMOVE_CONTACT = "DELETE FROM `t_doctor_contacts` WHERE `doctorId`=? AND `patientId`=?";
	private static final String REMOVE_UNFAMILIAR = "DELETE FROM `t_doctor_unfamiliar_patient` WHERE `doctorId`=? AND `patientId`=?";
	private static final String REMOVE_BLACKLIST = "DELETE FROM `t_doctor_blacklist_patient` WHERE `doctorId`=? AND `patientId`=?";

	private static final String ADD_TAG = "INSERT `t_doctor_tags`(`doctorId`,`name`) VALUE(?,?)";
	private static final String DEL_TAG = "DELETE FROM `t_doctor_tags` WHERE `id`=? AND `doctorId`=?";
	private static final String UPDATE_TAG_NAME = "UPDATE `t_doctor_tags` SET `name`=? WHERE `id`=?";

	private static final String INSERT_TAG_PATIENT = "INSERT `t_patient_tags`(`patientId`,`tagId`) VALUE(?,?)";
	private static final String REMOVE_TAG_PATIENT = "DELETE FROM `t_patient_tags` WHERE `patientId`=? AND `tagId`=?";

	protected int loadUnfamiliarCount(Doctor doctor) {
		return queryForInteger(SELECT_UNFAMILIAR_COUNT, doctor.getId());
	}

	protected List<Patient> loadContacts(Doctor doctor) {
		return queryForList(SELECT_CONTACTS, new Object[] { doctor.getId() }, Patient.class);
	}

	protected List<Patient> loadUnfamiliar(Doctor doctor) {
		return queryForList(SELECT_UNFAMILIAR, new Object[] { doctor.getId() }, Patient.class);
	}

	protected List<Patient> loadBlacklist(Doctor doctor) {
		return queryForList(SELECT_BLACKLIST, new Object[] { doctor.getId() }, Patient.class);
	}

	protected Integer loadContactSrc(Doctor doctor, Integer patientId) {
		return queryForInteger(SELECT_CONTACT_SRC, doctor.getId(), patientId);
	}

	protected void addUnfamiliar(Doctor doctor, Integer patientId) {
		jdbc.update(ADD_UNFAMILIAR, doctor.getId(), patientId);
	}

	protected void addBlacklist(Doctor doctor, Integer patientId) {
		jdbc.update(ADD_BLACKLIST, doctor.getId(), patientId);
	}

	protected void addToContacts(Doctor doctor, Integer patientId, ContactSrc contactSrc) {
		jdbc.update(ADD_CONTACT, doctor.getId(), patientId, contactSrc.id, contactSrc.id);
	}

	protected void removeUnfamiliar(Doctor doctor, Integer patientId) {
		jdbc.update(REMOVE_UNFAMILIAR, doctor.getId(), patientId);
	}

	protected void removeContact(Doctor doctor, Integer patientId) {
		jdbc.update(REMOVE_CONTACT, doctor.getId(), patientId);
	}

	protected void removeBlacklist(Doctor doctor, Integer patientId) {
		jdbc.update(REMOVE_BLACKLIST, doctor.getId(), patientId);
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

	protected List<Patient> loadSimplePatient(List<Integer> patientIds) {
		String cmd = SELECT_SIMPLE_PATIENT_BATCH.replace("#patientIds#", StringUtil.joinArr(patientIds, ","));
		return queryForList(cmd, Patient.class);
	}

	protected List<Patient> loadSimplePatientByTag(Doctor doctor, Integer tagId) {
		return queryForList(SELECT_SIMPLE_PATIENT_BY_TAG, new Object[] { doctor.getId(), tagId }, Patient.class);
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
		return queryForList(SELECT_PERSONAL_PATIENTS, new Object[] { doctor.getId() }, Patient.class);
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
}
