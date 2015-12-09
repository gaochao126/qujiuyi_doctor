package com.jiuyi.doctor.patients.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.patients.v2.model.ContactSrc;
import com.jiuyi.doctor.patients.v2.model.DoctorPatientType;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.patients.v2.model.SimplePatient;
import com.jiuyi.doctor.patients.v2.model.Tag;
import com.jiuyi.doctor.user.model.Doctor;
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

	/*
	 * @Autowired private CacheService cacheService;
	 */

	/** 首页信息 */
	protected ServerResult loadAll(Doctor doctor) {
		ServerResult res = new ServerResult();
		List<SimplePatient> contacts = dao.loadContacts(doctor);// 常用联系人
		Integer unfamiliarCount = dao.loadUnfamiliarCount(doctor);// 陌生患者数量
		res.put("number", contacts.size() + unfamiliarCount);
		res.put("unfamiliar", unfamiliarCount);
		res.putObjects("contacts", contacts);
		return res;
	}

	/** 陌生患者，包括患者关注了医生 & 购买过服务的 */
	protected ServerResult loadUnfamiliarPatients(Doctor doctor) {
		return new ServerResult("list", dao.loadUnfamiliar(doctor), true);
	}

	/** 黑名单 */
	protected ServerResult loadBlacklistPatients(Doctor doctor) {
		return new ServerResult("list", dao.loadBlacklist(doctor), true);
	}

	/** 把患者加入常用联系人 */
	@Transactional(rollbackFor = Exception.class)
	protected ServerResult moveInContacts(Doctor doctor, Integer patientId) {
		removeExistRelation(doctor, patientId);
		dao.addToContacts(doctor, patientId, ContactSrc.COMMON);
		setDoctorPatientType(doctor, patientId, DoctorPatientType.CONTACT);
		return new ServerResult();
	}

	/** 加陌生人 */
	@Transactional(rollbackFor = Exception.class)
	protected ServerResult moveInUnfamiliar(Doctor doctor, Integer patientId) {
		ResultConst rc = removeExistRelation(doctor, patientId);
		if (!rc.isSuccess()) {
			return new ServerResult(rc);
		}
		dao.addUnfamiliar(doctor, patientId);
		setDoctorPatientType(doctor, patientId, DoctorPatientType.UNFAMILIAR);
		return new ServerResult();
	}

	/** 加黑名单 */
	@Transactional(rollbackFor = Exception.class)
	protected ServerResult moveInBlacklist(Doctor doctor, Integer patientId) {
		ResultConst rc = removeExistRelation(doctor, patientId);
		if (!rc.isSuccess()) {
			return new ServerResult(rc);
		}
		dao.addBlacklist(doctor, patientId);
		setDoctorPatientType(doctor, patientId, DoctorPatientType.BLACKLIST);
		return new ServerResult();
	}

	/** 移除黑名单 */
	protected ServerResult removeBlacklist(Doctor doctor, Integer patientId) {
		DoctorPatientType type = getDoctorPatientType(doctor, patientId);
		if (!DoctorPatientType.BLACKLIST.equals(type)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.removeBlacklist(doctor, patientId);
		setDoctorPatientType(doctor, patientId, DoctorPatientType.NONE);
		return new ServerResult();
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

	/** 设置医生与患者之间的关系（常用联系人，陌生人，黑名单等） */
	private void setDoctorPatientType(Doctor doctor, Integer patientId, DoctorPatientType doctorPatientType) {
		if (!DoctorPatientType.NONE.equals(doctorPatientType)) {
			dao.updateRelation(doctor, patientId, doctorPatientType.ordinal());
		}
	}

	private DoctorPatientType getDoctorPatientType(Doctor doctor, Integer patientId) {
		Integer typeId = dao.loadDoctorPatientType(doctor, patientId);
		return DoctorPatientType.getTypeById(typeId);
	}

	/** 先移除先前的医生和患者关系，如果医生和患者之间是私人关系，则不能移除 */
	private ResultConst removeExistRelation(Doctor doctor, Integer patientId) {
		DoctorPatientType doctorPatientType = getDoctorPatientType(doctor, patientId);
		switch (doctorPatientType) {
		case CONTACT:
			int contactSrc = dao.loadContactSrc(doctor, patientId);
			if (contactSrc == ContactSrc.PERSONAL_PATIENT.id) {
				return ResultConst.PERSONAL_PATIENT;
			}
			dao.removeContact(doctor, patientId);
			return ResultConst.SUCCESS;
		case UNFAMILIAR:
			dao.removeUnfamiliar(doctor, patientId);
			return ResultConst.SUCCESS;
		case BLACKLIST:
			dao.removeBlacklist(doctor, patientId);
			return ResultConst.SUCCESS;
		default:
			return ResultConst.SUCCESS;
		}
	}

	protected void moveInContacts(Doctor doctor, Integer patientId, ContactSrc contactSrc) {
		removeExistRelation(doctor, patientId);
		dao.addToContacts(doctor, patientId, contactSrc);
		setDoctorPatientType(doctor, patientId, DoctorPatientType.CONTACT);
	}

}
