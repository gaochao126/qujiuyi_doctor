/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.chatserver.SystemMsg;
import com.jiuyi.doctor.chatserver.UserType;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.util.IdGenerator;
import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.ObjectUtil;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class PrescriptionManager {

	private @Autowired PrescriptionDao dao;

	private @Autowired ChatServerService chatServerService;

	/**
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	protected ServerResult createPrescription(Doctor doctor, Integer patientId) {
		Prescription prescription = new Prescription();
		prescription.setId(IdGenerator.genId());
		prescription.setDoctorId(doctor.getId());
		prescription.setPatientId(patientId);
		prescription.setCreateTime(new Date());
		prescription.setUpdateTime(new Date());
		prescription.setMedicineTakeStatus(0);
		prescription.setStatus(PrescriptionStatus.CREATED.ordinal());
		prescription.setNumber(genPresNumber(doctor, prescription));

		dao.createPrescription(doctor, prescription);

		String summary = String.format("%s医生发起了一个处方，请您确认", doctor.getName());
		SystemMsg systemMsg = new SystemMsg(UserType.PATIENT, patientId, summary, prescription, summary);
		chatServerService.postMsg(systemMsg);
		return new ServerResult();
	}

	/**
	 * @param doctor
	 * @param prescription
	 * @param medicines
	 * @return
	 */
	@Transactional
	protected ServerResult prescribe(Doctor doctor, Prescription prescription, List<PrescriptionMedicine> medicines) {
		ServerResult res = ObjectUtil.validateResult(prescription);
		if (!res.isSuccess()) {
			return res;
		}
		if (CollectionUtil.isNullOrEmpty(medicines)) {
			return new FailResult("请至少选择一种药品");
		}
		ServerResult checkMed = ObjectUtil.validateList(medicines);
		if (!checkMed.isSuccess()) {
			return checkMed;
		}
		Prescription old = dao.loadPrescription(doctor, prescription.getId());
		if (old == null) {
			return new FailResult("该处方不存在");
		}
		if (PrescriptionStatus.statusCanPrescribe(old.getStatus())) {
			return new FailResult("该状态下不能开处方");
		}

		for (PrescriptionMedicine medicine : medicines) {
			medicine.setPrescriptionId(prescription.getId());
		}

		prescription.setStatus(PrescriptionStatus.PRESCRIBED.ordinal());
		dao.deletePrescriptionMedicines(doctor, prescription);
		dao.updatePrescription(doctor, prescription);
		dao.insertMedicines(prescription, medicines);
		String summary = String.format("%s医生为您开了一个处方，正等待您确认付款", doctor.getName());
		SystemMsg systemMsg = new SystemMsg(UserType.PATIENT, prescription.getPatientId(), summary, prescription, summary);
		chatServerService.postMsg(systemMsg);
		return new ServerResult();
	}

	/**
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @param type
	 * @return
	 */
	protected ServerResult loadList(Doctor doctor, int page, int pageSize, int type) {
		if (type < 0 || type > 2 || page < 1 || pageSize < 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}

		List<Integer> toSelectStatus = new ArrayList<>();
		if (type == 0) {
			/* 等待确认列 */
			toSelectStatus.add(PrescriptionStatus.CREATED.ordinal());
		} else if (type == 1) {
			/* 处理中 */
			toSelectStatus.add(PrescriptionStatus.PATIENT_CONFIRMED.ordinal());
			toSelectStatus.add(PrescriptionStatus.PRESCRIBED.ordinal());
		} else {
			/* 历史记录 */
			toSelectStatus.add(PrescriptionStatus.PATIENT_CANCEL.ordinal());
			toSelectStatus.add(PrescriptionStatus.CANCEL_PRESCRIBE.ordinal());
			toSelectStatus.add(PrescriptionStatus.CANCEL_PAY.ordinal());
			toSelectStatus.add(PrescriptionStatus.PAYEDM.ordinal());
			toSelectStatus.add(PrescriptionStatus.EXPIRED.ordinal());
		}
		List<Prescription> prescriptions = dao.loadByStatus(doctor, toSelectStatus, page, pageSize);
		Integer count = dao.countByStatus(doctor, toSelectStatus);
		ServerResult res = new ServerResult();
		res.putObjects("list", prescriptions);
		res.put("count", count);
		return res;
	}

	/**
	 * @param doctor
	 * @param id
	 * @return
	 */
	protected ServerResult loadDetail(Doctor doctor, String id) {
		Prescription prescription = dao.loadPrescriptionDetail(doctor, id);
		if (prescription == null) {
			return new ServerResult();
		}
		ServerResult res = new ServerResult();
		res.putObject(prescription);
		res.put("medicines", dao.loadPrescriptionMedicines(id));
		return res;
	}

	/**
	 * @param doctor
	 * @param key
	 * @return
	 */
	protected ServerResult searchHistory(Doctor doctor, String key) {
		ServerResult res = new ServerResult();
		List<Integer> toSelectStatus = new ArrayList<>();
		toSelectStatus.add(PrescriptionStatus.PATIENT_CANCEL.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PRESCRIBE.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PAY.ordinal());
		toSelectStatus.add(PrescriptionStatus.PAYEDM.ordinal());
		toSelectStatus.add(PrescriptionStatus.EXPIRED.ordinal());
		List<Prescription> prescriptions = dao.searchPrescription(doctor, key, toSelectStatus);
		res.putObjects("list", prescriptions);
		return res;
	}

	/**
	 * 生成处方编号
	 * 
	 * @param doctor
	 * @param prescription
	 * @return
	 */
	private static String genPresNumber(Doctor doctor, Prescription prescription) {
		char head = (char) (Math.random() * 26 + 65);
		String number = String.format("%c%d%d%d", head, doctor.getId(), prescription.getRelativeId(), (int) (Math.random() * 900 + 100));
		return number;
	}

}
