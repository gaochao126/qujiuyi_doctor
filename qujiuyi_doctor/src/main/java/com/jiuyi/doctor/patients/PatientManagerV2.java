package com.jiuyi.doctor.patients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.model.DoctorPatient;
import com.jiuyi.doctor.patients.model.DoctorPatientRelation;
import com.jiuyi.doctor.patients.model.DoctorPatientSrc;
import com.jiuyi.doctor.patients.model.Patient;
import com.jiuyi.doctor.patients.model.Tag;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午5:04:44
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PatientManagerV2 {

	@Autowired
	private PatientDaoV2 dao;

	@Autowired
	private TagManager tagManager;

	/** 首页信息 */
	protected ServerResult loadAll(Doctor doctor) {
		ServerResult res = new ServerResult();
		List<Patient> contacts = dao.loadPatientsByRelation(doctor, DoctorPatientRelation.CONTACT);// 常用联系人
		Integer unfamiliarCount = dao.loadUnfamiliarCount(doctor);// 陌生患者数量
		res.put("number", contacts.size() + unfamiliarCount);
		res.put("unfamiliar", unfamiliarCount);
		res.putObjects("contacts", contacts);
		return res;
	}

	/** 陌生患者，包括患者关注了医生 & 购买过服务的 */
	protected ServerResult loadUnfamiliarPatients(Doctor doctor) {
		return new ServerResult("list", dao.loadPatientsByRelation(doctor, DoctorPatientRelation.UNFAMILIAR), true);
	}

	/** 黑名单 */
	protected ServerResult loadBlacklistPatients(Doctor doctor) {
		return new ServerResult("list", dao.loadPatientsByRelation(doctor, DoctorPatientRelation.BLACKLIST), true);
	}

	/** 把患者加入常用联系人 */
	protected ServerResult moveInContacts(Doctor doctor, Integer patientId) {
		DoctorPatient doctorPatient = new DoctorPatient(doctor.getId(), patientId, DoctorPatientSrc.MANUAL.id, DoctorPatientRelation.CONTACT.ordinal());
		addDoctorPatient(doctor, doctorPatient);
		return new ServerResult();
	}

	/** 加陌生人 */
	protected ServerResult moveInUnfamiliar(Doctor doctor, Integer patientId) {
		if (isPersonal(doctor, patientId)) {
			return new FailResult("对不起，不能将私人患者移到陌生人列表！");
		}
		DoctorPatient doctorPatient = new DoctorPatient(doctor.getId(), patientId, DoctorPatientSrc.MANUAL.id, DoctorPatientRelation.UNFAMILIAR.ordinal());
		addDoctorPatient(doctor, doctorPatient);
		return new ServerResult();
	}

	/** 加黑名单 */
	protected ServerResult moveInBlacklist(Doctor doctor, Integer patientId) {
		if (isPersonal(doctor, patientId)) {
			return new FailResult("对不起，不能将私人患者移到黑名单！");
		}
		DoctorPatient doctorPatient = new DoctorPatient(doctor.getId(), patientId, DoctorPatientSrc.MANUAL.id, DoctorPatientRelation.BLACKLIST.ordinal());
		addDoctorPatient(doctor, doctorPatient);
		return new ServerResult();
	}

	/** 移除黑名单 */
	protected ServerResult removeBlacklist(Doctor doctor, Integer patientId) {
		return moveInUnfamiliar(doctor, patientId);
	}

	/** 设置备注名 */
	protected ServerResult remark(Doctor doctor, Integer patientId, String remark) {
		dao.updateRemark(doctor, patientId, remark);
		return new ServerResult();
	}

	/** 设置备注 */
	protected ServerResult note(Doctor doctor, Integer patientId, String note) {
		dao.updateNote(doctor, patientId, note);
		return new ServerResult();
	}

	/** 私人患者数量 */
	protected int getPersonalPatientCount(Doctor doctor) {
		return dao.loadPersonalPatientCount(doctor);
	}

	/** 患者详细信息 */
	protected ServerResult patientDetailInfo(Doctor doctor, Integer patientId) {
		ServerResult res = new ServerResult();
		Patient patient = loadPatient(doctor, patientId);
		if (patient == null) {
			return new FailResult("患者不存在");
		}
		List<Tag> allTags = tagManager.loadInfoBase(doctor).getTags();
		List<Tag> patientTags = tagManager.loadPatientTags(doctor, patientId);
		res.putObjects("allTags", allTags);
		patient.setTags(patientTags);
		res.putObject(patient);
		return res;
	}

	protected Patient loadPatient(Doctor doctor, Integer patientId) {
		return dao.loadPatientDetailInfo(doctor, patientId);
	}

	protected Patient loadPatient(Integer patientId) {
		return dao.loadPatient(patientId);
	}

	/**
	 * @param doctor
	 * @param doctorPatient
	 */
	protected void addDoctorPatient(Doctor doctor, DoctorPatient doctorPatient) {
		dao.insertDoctorPatientRelation(doctor, doctorPatient);
	}

	/**
	 * 判断是不是私人患者
	 * 
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	protected boolean isPersonal(Doctor doctor, int patientId) {
		return dao.isPersonal(doctor, patientId);
	}

	/**
	 * 搜索我的患者
	 * 
	 * @param doctor
	 * @param key
	 * @param type
	 * @return
	 */
	protected ServerResult searchMyPatient(Doctor doctor, String key, Integer type) {
		if (type < 0 || type > 3) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		List<Patient> patients = null;
		if (type == 0) {
			patients = dao.searchMyPatient(doctor, key);
		} else {
			patients = dao.searchMyPatientByRelation(doctor, key, DoctorPatientRelation.values()[type]);
		}
		return new ServerResult("list", patients, true);
	}
}
