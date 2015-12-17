/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

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
			+ "WHERE pres.doctorId=:doctorId AND status in (:status) LIMIT :startIndex,:size";// where end

	private static final String SEARCH_PRESCRIPTION = "SELECT pres.*,patient.name as patientName,patient.headPortrait as patientHead "// select
			+ "FROM `t_prescription` pres " // 处方表
			+ "LEFT JOIN `t_patient` patient ON patient.id=pres.patientId " // 患者表
			+ "WHERE pres.doctorId=:doctorId AND `relativeName` LIKE :key AND `status` in (:status)";// where end

	private static final String COUNT_PRESCRIPTION_BY_STATUS = "SELECT COUNT(*) FROM `t_prescription` WHERE doctorId=:doctorId AND status in (:status)";

	private static final String INSERT_SIMPLE_PRESCRIPTION = "INSERT `t_prescription`(`id`,`number`,`doctorId`,`patientId`,`createTime`,`updateTime`,`status`) VALUES(:id,:number,:doctorId,:patientId,:createTime,:updateTime,:status)";

	private static final String INSERT_PRESCRIPTION = "INSERT `t_prescription`"
			+ "(`id`,`number`,`doctorId`,`patientId`,`relativeId`,`relativeName`,`relativeAge`,`relativeGender`,`allergies`,`illness`,`diagnosis`,`createTime`,`updateTime`,`status`,`price`) "
			+ "VALUES(:id,:number,:doctorId,:patientId,:relativeId,:relativeName,:relativeAge,:relativeGender,:allergies,:illness,:diagnosis,:createTime,:updateTime,:status,:price) ";

	private static final String UPDATE_PRESCRIPTION = "UPDATE `t_prescription` SET " 
			+ "relativeId=:relativeId," 
			+ "relativeName=:relativeName," 
			+ "relativeAge=:relativeAge," 
			+ "relativeGender=:relativeGender," 
			+ "allergies=:allergies,"
			+ "illness=:illness," 
			+ "diagnosis=:diagnosis," 
			+ "updateTime=:updateTime," 
			+ "price=:price," 
			+ "status=:status "
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
	protected List<Prescription> searchPrescription(Doctor doctor, String key, List<Integer> toSelectStatus) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("doctorId", doctor.getId());
		params.addValue("status", toSelectStatus);
		params.addValue("key", "%" + key + "%");
		return queryForList(SEARCH_PRESCRIPTION, params, Prescription.class);
	}

}
