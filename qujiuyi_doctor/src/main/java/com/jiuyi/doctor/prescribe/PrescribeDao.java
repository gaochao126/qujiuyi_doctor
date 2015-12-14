package com.jiuyi.doctor.prescribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.prescribe.model.DoctorPrescribe;
import com.jiuyi.doctor.prescribe.model.Medicine;
import com.jiuyi.doctor.prescribe.model.Prescribe;
import com.jiuyi.doctor.prescribe.model.PrescribeStatus;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午2:20:51
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class PrescribeDao extends DbBase {

	private static final String SELECT_PRESCRIBE = "SELECT * FROM `t_prescribe` WHERE `deleted`=0 AND `doctorId`=? AND `id`=?";
	private static final String SELECT_PRESCRIBE_BY_DOCTOR = "SELECT pres.*,patient.`headPortrait`,patient.`name` as patientName,patient.`gender` as patientGender,patient.`birthday` as patientAge FROM `t_prescribe` pres,`t_patient` patient WHERE patient.`id`=pres.`patientId` AND pres.`deleted`=0 AND `doctorId`=? ORDER BY pres.`createTime` DESC";
	private static final String SELECT_PRESCRIBE_BY_ID = "SELECT pres.*,patient.`headPortrait` as patientHead,patient.`name` as patientName,patient.`gender` as patientGender,patient.`birthday` as patientAge FROM `t_prescribe` pres,`t_patient` patient WHERE patient.`id`=pres.`patientId` AND pres.`deleted`=0 AND pres.`doctorId`=? AND pres.`id`=?";
	private static final String SELECT_PRESCRIBE_BY_STATUS = "SELECT pres.*,patient.`headPortrait` as patientHead,patient.`name` as patientName,patient.`gender` as patientGender,patient.`birthday` as patientAge FROM `t_prescribe` pres,`t_patient` patient WHERE patient.`id`=pres.`patientId` AND pres.`deleted`=0 AND pres.`status`=? AND pres.`doctorId`=? ORDER BY pres.`createTime` DESC LIMIT ?,?";
	private static final String SELECT_FINISHED_PRESCRIBE = "SELECT pres.*,patient.`headPortrait` as patientHead,patient.`name` as patientName,patient.`gender` as patientGender,patient.`birthday` as patientAge  FROM `t_prescribe` pres,`t_patient` patient WHERE patient.`id`=pres.`patientId` AND pres.`deleted`=0 AND pres.`status`<>? AND pres.`doctorId`=? ORDER BY pres.`createTime` DESC LIMIT ?,?";
	private static final String SELECT_DOCTOR_PRESCRIBE = "SELECT * FROM `t_doctor_prescribe` WHERE `doctorId`=?";
	private static final String SELECT_PRESCRIBE_DOCTORID_PRESCRIBEID = "SELECT count(`id`) FROM `t_prescribe` WHERE `doctorId`=? AND `deleted`=0 AND `id`=?";
	private static final String SELECT_PRESCRIBE_MEDICINES = "SELECT p.`quantity`,m.`code`,m.`name`,m.`price`,m.`image`,m.`unit` FROM `t_prescribe_detail` p,`t_medicine` m WHERE p.`medicineId`=m.`id` AND p.`prescribeId`=?";

	private static final String UPDATE_PRESCRIBE_STATUS = "UPDATE `t_prescribe` SET `status`=?,`respTime`=? WHERE `id`=?";
	private static final String ACCEPT_PRESCRIBE = "UPDATE `t_prescribe` SET `status`=?, `presListImage`=?,`respTime`=? WHERE `id`=?";
	private static final String REFUSE_PRESCRIBE = "UPDATE `t_prescribe` SET `status`=?, `refuseReason`=?,`respTime`=? WHERE `id`=?";
	private static final String UPDATE_DOCTOR_PRESCRIBE_STATUS = "INSERT `t_doctor_prescribe`(`doctorId`,`status`) VALUE(?,?) ON DUPLICATE KEY UPDATE `status`=?";
	private static final String DELETE_PRESCRIBE = "UPDATE 	`t_prescribe` SET `deleted`=1 WHERE `status`<>0 AND `doctorId`=? AND `id`=?";

	protected DoctorPrescribe loadDoctorPrescribe(Doctor doctor) {
		return queryForObjectDefaultBuilder(SELECT_DOCTOR_PRESCRIBE, new Object[] { doctor.getId() }, DoctorPrescribe.class);
	}

	protected void updateStatus(Doctor doctor, ServiceStatus serviceStatus) {
		jdbc.update(UPDATE_DOCTOR_PRESCRIBE_STATUS, doctor.getId(), serviceStatus.ordinal(), serviceStatus.ordinal());
	}

	protected Prescribe loadPrescribeById(Doctor doctor, int id) {
		return queryForObjectDefaultBuilder(SELECT_PRESCRIBE, new Object[] { doctor.getId(), id }, Prescribe.class);
	}

	protected Prescribe loadDetailPrescribeById(Doctor doctor, int id) {
		return queryForObjectDefaultBuilder(SELECT_PRESCRIBE_BY_ID, new Object[] { doctor.getId(), id }, Prescribe.class);
	}

	protected Boolean checkDoctorPrescribeId(Doctor doctor, int id) {
		return queryForInteger(SELECT_PRESCRIBE_DOCTORID_PRESCRIBEID, new Object[] { doctor.getId(), id }) > 0;
	}

	protected List<Prescribe> loadAllPrescribe(Doctor doctor) {
		return loadPrescribe(SELECT_PRESCRIBE_BY_DOCTOR, doctor.getId());
	}

	protected List<Prescribe> loadUnhandlePrescribe(Doctor doctor, Integer startRow, Integer pageSize) {
		return loadPrescribe(SELECT_PRESCRIBE_BY_STATUS, PrescribeStatus.UNHANDLE.ordinal(), doctor.getId(), startRow, pageSize);
	}

	protected List<Prescribe> loadAcceptedPrescribe(Doctor doctor) {
		return loadPrescribe(SELECT_PRESCRIBE_BY_STATUS, PrescribeStatus.ACCEPTED.ordinal(), doctor.getId());
	}

	protected List<Prescribe> loadFinishedPrescribe(Doctor doctor, Integer startRow, Integer pageSize) {
		return loadPrescribe(SELECT_FINISHED_PRESCRIBE, PrescribeStatus.UNHANDLE.ordinal(), doctor.getId(), startRow, pageSize);
	}

	protected void updatePrescribeStatus(Integer id, PrescribeStatus prescribeStatus) {
		jdbc.update(UPDATE_PRESCRIBE_STATUS, prescribeStatus.ordinal(), new Date(), id);
	}

	protected void acceptPrescribe(Integer id, String presListFileName) {
		jdbc.update(ACCEPT_PRESCRIBE, PrescribeStatus.ACCEPTED.ordinal(), presListFileName, new Date(), id);
	}

	protected void refusePrescribe(Integer id, String reason) {
		jdbc.update(REFUSE_PRESCRIBE, PrescribeStatus.REFUSED.ordinal(), reason, new Date(), id);
	}

	protected List<Medicine> loadPrescribeMedcines(Integer prescribeId) {
		return jdbc.query(SELECT_PRESCRIBE_MEDICINES, new Object[] { prescribeId }, Medicine.builder);
	}

	protected void deletePrescribes(Doctor doctor, Integer[] prescribeIds) {
		List<Object[]> args = new ArrayList<>(prescribeIds.length);
		for (Integer prescribeId : prescribeIds) {
			args.add(new Object[] { doctor.getId(), prescribeId });
		}
		jdbc.batchUpdate(DELETE_PRESCRIBE, args);
	}

	private List<Prescribe> loadPrescribe(String sql, Object... args) {
		return queryForList(sql, args, Prescribe.class);
	}

}
