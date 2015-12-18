/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.chatserver.SystemMsg;
import com.jiuyi.doctor.chatserver.UserType;
import com.jiuyi.doctor.patients.v2.PatientServiceV2;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.util.IdGenerator;
import com.jiuyi.doctor.yaofang.YaofangService;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;
import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.ObjectUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class PrescriptionManager {

	private @Autowired PrescriptionDao dao;

	private @Autowired YaofangService yaofangService;

	private @Autowired ChatServerService chatServerService;

	private @Autowired PatientServiceV2 patientService;

	/**
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	protected ServerResult createPrescription(Doctor doctor, Integer patientId) {
		if (true) {
			return new ServerResult();
		}
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
		ServerResult res = checkPrescription(prescription, medicines);
		if (!res.isSuccess()) {
			return res;
		}

		/** 价格信息 需要从大药房数据库取，库存验证 */
		List<String> formatIds = getFormatIds(medicines);
		List<FormatMedicine> formatMedicines = yaofangService.loadFormatMeds(formatIds);
		ServerResult checkStock = checkMedicines(formatIds, formatMedicines);
		if (!checkStock.isSuccess()) {
			return checkStock;
		}
		String id = IdGenerator.genId();
		for (PrescriptionMedicine medicine : medicines) {
			medicine.setPrescriptionId(id);
		}
		prescription.setId(id);
		prescription.setDoctorId(doctor.getId());
		prescription.setCreateTime(new Date());
		prescription.setUpdateTime(new Date());
		prescription.setMedicineTakeStatus(0);
		prescription.setType(PrescriptionType.COMMON.ordinal());
		prescription.setPrice(calcTotalPrice(formatMedicines));
		prescription.setNumber(genPresNumber(doctor, prescription));
		prescription.setStatus(PrescriptionStatus.PRESCRIBED.ordinal());
		dao.insertPrescription(doctor, prescription);
		dao.insertMedicines(prescription, medicines);
		String summary = String.format("%s医生为您开了一个处方，正等待您确认付款", doctor.getName());
		SystemMsg systemMsg = new SystemMsg(UserType.PATIENT, prescription.getPatientId(), summary, prescription, summary);
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
	protected ServerResult updatePrescription(Doctor doctor, Prescription prescription, List<PrescriptionMedicine> medicines) {
		ServerResult res = checkPrescription(prescription, medicines);
		if (!res.isSuccess()) {
			return res;
		}
		if (StringUtil.isNullOrEmpty(prescription.getId())) {
			return new FailResult("参数id不能为空");
		}
		Prescription old = dao.loadPrescription(doctor, prescription.getId());
		if (old == null) {
			return new FailResult("该处方不存在");
		}
		if (!PrescriptionStatus.statusCanPrescribe(old.getStatus())) {
			return new FailResult("对不起，该状态下不能修改处方！");
		}

		/** 计算价格，库存验证 */
		List<String> formatIds = getFormatIds(medicines);
		List<FormatMedicine> formatMedicines = yaofangService.loadFormatMeds(formatIds);
		ServerResult checkStock = checkMedicines(formatIds, formatMedicines);
		if (!checkStock.isSuccess()) {
			return checkStock;
		}
		for (PrescriptionMedicine medicine : medicines) {
			medicine.setPrescriptionId(prescription.getId());
		}
		prescription.setUpdateTime(new Date());
		prescription.setPrice(calcTotalPrice(formatMedicines));
		prescription.setStatus(PrescriptionStatus.PRESCRIBED.ordinal());
		dao.deletePrescriptionMedicines(doctor, prescription);
		dao.updatePrescription(doctor, prescription);
		dao.insertMedicines(prescription, medicines);
		String summary = String.format("%s医生重新为您开了一个处方，正等待您确认付款", doctor.getName());
		SystemMsg systemMsg = new SystemMsg(UserType.PATIENT, prescription.getPatientId(), summary, prescription, summary);
		chatServerService.postMsg(systemMsg);
		return new ServerResult();
	}

	/**
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @return
	 */
	protected ServerResult loadHandlingList(Doctor doctor, int page, int pageSize) {
		List<Integer> statusList = Arrays.asList(PrescriptionStatus.PRESCRIBED.ordinal());
		List<Prescription> prescriptions = dao.loadByStatus(doctor, statusList, page, pageSize);
		Integer count = dao.loadHandlingCount(doctor);
		ServerResult res = new ServerResult();
		res.putObjects("list", prescriptions);
		res.put("count", count);
		return res;
	}

	/**
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @param type
	 * @return
	 */
	protected ServerResult loadHistory(Doctor doctor, int page, int pageSize) {
		List<Integer> toSelectStatus = new ArrayList<>();
		toSelectStatus.add(PrescriptionStatus.PATIENT_CANCEL.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PRESCRIBE.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PAY.ordinal());
		toSelectStatus.add(PrescriptionStatus.PAYEDM.ordinal());
		toSelectStatus.add(PrescriptionStatus.EXPIRED.ordinal());
		List<Patient> prescriptions = dao.loadPatientsByPresStatus(doctor, toSelectStatus, page, pageSize);
		Integer count = dao.countPatientsByPresStatus(doctor, toSelectStatus);
		count = count != null ? count : 0;
		ServerResult res = new ServerResult();
		res.putObjects("list", prescriptions);
		res.put("count", count);
		return res;
	}

	/**
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @param type
	 * @return
	 */
	protected ServerResult loadPatientHistory(Doctor doctor, int patientId) {
		List<Integer> toSelectStatus = new ArrayList<>();
		/* 历史记录 */
		toSelectStatus.add(PrescriptionStatus.PATIENT_CANCEL.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PRESCRIBE.ordinal());
		toSelectStatus.add(PrescriptionStatus.CANCEL_PAY.ordinal());
		toSelectStatus.add(PrescriptionStatus.PAYEDM.ordinal());
		toSelectStatus.add(PrescriptionStatus.EXPIRED.ordinal());
		List<Prescription> prescriptions = dao.loadByPatientAndStatus(doctor, patientId, toSelectStatus);
		ServerResult res = new ServerResult();
		res.putObjects("list", prescriptions);
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
		List<PrescriptionMedicine> prescriptionMedicines = dao.loadPrescriptionMedicines(id);
		List<String> formatIds = getFormatIds(prescriptionMedicines);

		/* 从大药房数据库拿到的药品和规格的详细信息 */
		List<FormatMedicine> formatMedicines = yaofangService.loadFormatMeds(formatIds);

		/* PrescriptionMedicine 里面只有药品和规格的id，需要组合详细信息返回给客户端 */
		List<MapObject> medicineInfos = new ArrayList<>();
		for (PrescriptionMedicine pm : prescriptionMedicines) {
			FormatMedicine formatMedicine = getFormatMedicine(pm, formatMedicines);
			MapObject mo = formatMedicine.serializeToMapObject();
			mo.put("number", pm.getNumber());
			mo.put("instructions", pm.getInstructions());
			medicineInfos.add(mo);
		}
		ServerResult res = new ServerResult();
		res.putObject(prescription);
		res.put("medicines", medicineInfos);
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
		List<Patient> prescriptions = dao.searchPrescription(doctor, key, toSelectStatus);
		res.putObjects("list", prescriptions);
		return res;
	}

	/**
	 * 获取处方药品列表中的规格id，根据这些id可以到大药房数据库拿到药品规格信息，因为是跨库的，所以不能做join
	 * 
	 * @param prescriptionMedicines
	 * @return
	 */
	private List<String> getFormatIds(List<PrescriptionMedicine> prescriptionMedicines) {
		if (CollectionUtil.isNullOrEmpty(prescriptionMedicines)) {
			return new ArrayList<>();
		}
		List<String> res = new ArrayList<>(prescriptionMedicines.size());
		for (PrescriptionMedicine pm : prescriptionMedicines) {
			res.add(pm.getFormatId());
		}
		return res;
	}

	/**
	 * 获取处方中一个药品对应的规格详情
	 * 
	 * @param prescriptionMedicine
	 * @param formatMedicines
	 * @return
	 */
	private FormatMedicine getFormatMedicine(PrescriptionMedicine prescriptionMedicine, List<FormatMedicine> formatMedicines) {
		for (FormatMedicine fm : formatMedicines) {
			if (fm.getId().equals(prescriptionMedicine.getFormatId())) {
				return fm;
			}
		}
		return new FormatMedicine();
	}

	/**
	 * 计算药单价格
	 * 
	 * @param prescriptionMedicine
	 * @param formatMedicines
	 * @return
	 */
	private BigDecimal calcTotalPrice(List<FormatMedicine> formatMedicines) {
		BigDecimal res = new BigDecimal(0);
		for (FormatMedicine fm : formatMedicines) {
			res.add(fm.getPrice());
		}
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

	/***
	 * 检查处方参数的合法性
	 * 
	 * @param prescription
	 * @param medicines
	 * @return
	 */
	private ServerResult checkPrescription(Prescription prescription, List<PrescriptionMedicine> medicines) {
		ServerResult res = ObjectUtil.validateResult(prescription);
		if (!res.isSuccess()) {
			return res;
		}
		if (CollectionUtil.isNullOrEmpty(medicines)) {
			return new FailResult("请至少选择一种药品");
		}
		if (medicines.size() > 20) {
			return new FailResult("药品种类过多，请去掉不必要的药品");
		}
		ServerResult checkMed = ObjectUtil.validateList(medicines);
		if (!checkMed.isSuccess()) {
			return checkMed;
		}
		Patient patient = patientService.loadPatient(prescription.getPatientId());
		if (patient == null) {
			return new FailResult("该患者不存在或已注销~");
		}
		return new ServerResult();
	}

	/**
	 * 检测规格id合法性，库存
	 * 
	 * @param formatMedicines
	 * @return
	 */
	private ServerResult checkMedicines(List<String> formatIds, List<FormatMedicine> formatMedicines) {
		/* 检测规格id是否存在 */
		for (String formatId : formatIds) {
			boolean formatIdExist = false;
			for (FormatMedicine fm : formatMedicines) {
				if (formatId.equals(fm.getId())) {
					formatIdExist = true;
				}
			}
			if (!formatIdExist) {
				return new FailResult(String.format("id为《%s》的药品规格不存在", formatId));
			}
		}

		/* 检测库存 */
		for (FormatMedicine fm : formatMedicines) {
			if (fm.getStock() < 1) {
				return new FailResult(String.format("%s《%s》库存不足，请选用其他药品或规格", fm.getName(), fm.getFormat()));
			}
		}
		return new ServerResult();
	}

}
