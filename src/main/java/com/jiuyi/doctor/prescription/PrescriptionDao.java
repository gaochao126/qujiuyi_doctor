/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * 
 * @author xutaoyang
 *
 */
@Repository
public class PrescriptionDao extends DbBase {

	private static final String SELECT_PRESCRIPTION = "SELECT * FROM `t_prescription` WHERE `doctorId`=? AND `id`=?";
	private static final String SELECT_PRESCRIPTION_MEDS = "SELECT * FROM `t_prescription_detail` WHERE `prescriptionId`=?";
	private static final String SELECT_PRESCRIPTION_DETAIL = "SELECT pres.*,patient.name as patientName,patient.headPortrait as patientHead " // select
			+ "FROM `t_prescription` pres " // 处方表
			+ "LEFT JOIN `t_patient` patient ON patient.id=pres.patientId " // 患者表
			+ "WHERE pres.`doctorId`=? AND pres.`id`=?";
	private static final String SELECT_PRESCRIPTION_BY_STATUS = "SELECT pres.*,patient.name as patientName,patient.headPortrait as patientHead "// select
			+ "FROM `t_prescription` pres " // 处方表
			+ "LEFT JOIN `t_patient` patient ON patient.id=pres.patientId " // 患者表
			+ "WHERE pres.doctorId=:doctorId AND `status` IN (:status) LIMIT :startIndex,:size";// where end

	private static final String SELECT_PRESCRIPTION_BY_PATIENT_AND_STATUS = "SELECT pres.*,patient.name as patientName,patient.headPortrait as patientHead "// select
			+ "FROM `t_prescription` pres " // 处方表
			+ "LEFT JOIN `t_patient` patient ON patient.id=pres.patientId " // 患者表
			+ "WHERE pres.doctorId=:doctorId AND pres.`patientId`=:patientId AND `status` IN (:status)";// where

	private static final String COUNT_PRESCRIPTION_BY_STATUS = "SELECT COUNT(*) FROM `t_prescription` WHERE doctorId=:doctorId AND `status` in (:status)";

	private static final String SELECT_PATIENTS_BY_STATUS = "SELECT pres.id,pres.patientId,patient.name AS name,patient.headPortrait,patient.gender,remark.remark "//
			+ "FROM `t_prescription` pres "//
			+ "JOIN `t_patient` patient ON pres.`patientId`=patient.id "//
			+ "LEFT JOIN `t_doctor_remark_patient` remark ON remark.doctorId=pres.doctorId AND remark.patientId=pres.patientId " + "WHERE pres.`status` IN (:status) AND pres.doctorId=:doctorId "
			+ "GROUP BY pres.patientId " + "LIMIT :startIndex,:pageSize ";

	private static final String SERACH_PATIENTS_BY_STATUS = "SELECT pres.id,pres.patientId,patient.name AS name,patient.headPortrait,patient.gender,remark.remark " //
			+ "FROM `t_prescription` pres "//
			+ "JOIN `t_patient` patient ON pres.`patientId`=patient.id "//
			+ "LEFT JOIN `t_doctor_remark_patient` remark ON remark.doctorId=pres.doctorId AND remark.patientId=pres.patientId "
			+ "WHERE pres.`status` IN (:status) AND pres.doctorId=:doctorId AND (patient.name LIKE :key OR remark.remark LIKE :key) " + "GROUP BY pres.patientId ";

	private static final String COUNT_HANDLING_PRES = "SELECT COUNT(*) FROM `t_prescription` WHERE `status`=? AND `doctorId`=?";

	private static final String COUNT_PATIENTS_BY_STATUS = "SELECT COUNT(DISTINCT(pres.patientId)) FROM `t_prescription` pres WHERE pres.`status` IN (:status) AND pres.doctorId=:doctorId;";

	private static final String INSERT_SIMPLE_PRESCRIPTION = "INSERT `t_prescription`(`id`,`number`,`doctorId`,`patientId`,`createTime`,`updateTime`,`status`) VALUES(:id,:number,:doctorId,:patientId,:createTime,:updateTime,:status)";

	private static final String INSERT_PRESCRIPTION = "INSERT `t_prescription`"
			+ "(`id`,`number`,`doctorId`,`patientId`,`relativeId`,`relativeName`,`relativeAge`,`relativeGender`,`allergies`,`illness`,`diagnosis`,`createTime`,`updateTime`,`status`,`price`) "
			+ "VALUES(:id,:number,:doctorId,:patientId,:relativeId,:relativeName,:relativeAge,:relativeGender,:allergies,:illness,:diagnosis,:createTime,:updateTime,:status,:price) ";

	private static final String UPDATE_PRESCRIPTION = "UPDATE `t_prescription` SET " + "relativeId=:relativeId," + "relativeName=:relativeName," + "relativeAge=:relativeAge,"
			+ "relativeGender=:relativeGender," + "allergies=:allergies," + "illness=:illness," + "diagnosis=:diagnosis," + "updateTime=:updateTime," + "price=:price," + "`status`=:status "
			+ "WHERE `id`=:id;";

	private static final String INSERT_PRESCRIPTION_DETAIL = "INSERT `t_prescription_detail`(prescriptionId,medicineId,formatId,number,instructions) VALUE(:prescriptionId,:medicineId,:formatId,:number,:instructions)";

	private static final String DELETE_OLD_PRES_MEDS = "DELETE FROM `t_prescription_detail` WHERE `prescriptionId`=?";

	/**
	 * @param doctor
	 * @param patientId
	 */
	protected void createPrescription(Doctor doctor, Prescription prescription) {
		namedJdbc.update(INSERT_SIMPLE_PRESCRIPTION, new BeanPropertySqlParameterSource(prescription));
	}

	protected Prescription loadPrescription(Doctor doctor, String id) {
		return queryForObjectDefaultBuilder(SELECT_PRESCRIPTION, new Object[] { doctor.getId(), id }, Prescription.class);
	}

	protected Prescription loadPrescriptionDetail(Doctor doctor, String id) {
		return queryForObjectDefaultBuilder(SELECT_PRESCRIPTION_DETAIL, new Object[] { doctor.getId(), id }, Prescription.class);
	}

	/**
	 * @param doctor
	 * @param prescription
	 */
	protected void deletePrescriptionMedicines(Doctor doctor, Prescription prescription) {
		jdbc.update(DELETE_OLD_PRES_MEDS, prescription.getId());
	}

	/**
	 * @param doctor
	 * @param prescription
	 */
	protected void insertPrescription(Doctor doctor, Prescription prescription) {
		namedJdbc.update(INSERT_PRESCRIPTION, new BeanPropertySqlParameterSource(prescription));
	}

	/**
	 * @param doctor
	 * @param prescription
	 */
	protected void updatePrescription(Doctor doctor, Prescription prescription) {
		namedJdbc.update(UPDATE_PRESCRIPTION, new BeanPropertySqlParameterSource(prescription));
	}

	/**
	 * @param medicines
	 */
	protected void insertMedicines(Prescription prescription, List<PrescriptionMedicine> medicines) {
		namedJdbc.batchUpdate(INSERT_PRESCRIPTION_DETAIL, SqlParameterSourceUtils.createBatch(medicines.toArray(new PrescriptionMedicine[medicines.size()])));
	}

	/**
	 * @param id
	 * @return
	 */
	protected List<PrescriptionMedicine> loadPrescriptionMedicines(String id) {
		return queryForList(SELECT_PRESCRIPTION_MEDS, new Object[] { id }, PrescriptionMedicine.class);
	}

	/**
	 * @param doctor
	 * @param toSelectStatus
	 * @return
	 */
	protected List<Prescription> loadByStatus(Doctor doctor, List<Integer> toSelectStatus, int page, int pageSize) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("doctorId", doctor.getId());
		params.addValue("status", toSelectStatus);
		params.addValue("startIndex", startIndex(page, pageSize));
		params.addValue("size", pageSize);
		return queryForList(SELECT_PRESCRIPTION_BY_STATUS, params, Prescription.class);
	}

	/**
	 * @param doctor
	 * @param toSelectStatus
	 * @return
	 */
	protected List<Prescription> loadByPatientAndStatus(Doctor doctor, Integer patientId, List<Integer> toSelectStatus) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("doctorId", doctor.getId());
		params.addValue("patientId", patientId);
		params.addValue("status", toSelectStatus);
		return queryForList(SELECT_PRESCRIPTION_BY_PATIENT_AND_STATUS, params, Prescription.class);
	}

	/**
	 * @param doctor
	 * @param toSelectStatus
	 * @return
	 */
	protected Integer countByStatus(Doctor doctor, List<Integer> toSelectStatus) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("doctorId", doctor.getId());
		params.addValue("status", toSelectStatus);
		Integer count = namedJdbc.queryForObject(COUNT_PRESCRIPTION_BY_STATUS, params, Integer.class);
		return count != null ? count : 0;
	}

	/**
	 * @param doctor
	 * @param key
	 * @return
	 */
	protected List<Patient> searchPrescription(Doctor doctor, String key, List<Integer> toSelectStatus) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("doctorId", doctor.getId());
		params.addValue("status", toSelectStatus);
		params.addValue("key", "%" + key + "%");
		return queryForList(SERACH_PATIENTS_BY_STATUS, params, Patient.class);
	}

	/**
	 * @param doctor
	 * @param toSelectStatus
	 * @param page
	 * @param pageSize
	 * @return
	 */
	protected List<Patient> loadPatientsByPresStatus(Doctor doctor, List<Integer> toSelectStatus, int page, int pageSize) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("doctorId", doctor.getId());
		paramSource.addValue("status", toSelectStatus);
		paramSource.addValue("startIndex", startIndex(page, pageSize));
		paramSource.addValue("pageSize", pageSize);
		return queryForList(SELECT_PATIENTS_BY_STATUS, paramSource, Patient.class);
	}

	/**
	 * @param doctor
	 * @param toSelectStatus
	 * @return
	 */
	protected Integer countPatientsByPresStatus(Doctor doctor, List<Integer> toSelectStatus) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("doctorId", doctor.getId());
		paramSource.addValue("status", toSelectStatus);
		return namedJdbc.queryForObject(COUNT_PATIENTS_BY_STATUS, paramSource, Integer.class);
	}

	/**
	 * @param doctor
	 * @return
	 */
	protected Integer loadHandlingCount(Doctor doctor) {
		return queryForInteger(COUNT_HANDLING_PRES, PrescriptionStatus.PRESCRIBED.ordinal(), doctor.getId());
	}

}
