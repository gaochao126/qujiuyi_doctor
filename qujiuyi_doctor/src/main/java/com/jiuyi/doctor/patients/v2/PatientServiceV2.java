package com.jiuyi.doctor.patients.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.v2.model.ContactSrc;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午5:05:11
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PatientServiceV2 {

	@Autowired
	private PatientManagerV2 manager;

	/** 加入常用联系人 */
	public void addToContact(Doctor doctor, Integer patientId, ContactSrc contactSrc) {
		manager.moveInContacts(doctor, patientId, contactSrc);
	}

	/** 加入陌生联系人 */
	public void addToUnfamiliar(Doctor doctor, Integer patientId) {
		manager.moveInUnfamiliar(doctor, patientId);
	}

	/** 加黑名单 */
	public void moveInBlacklist(Doctor doctor, Integer patientId) {
		manager.moveInBlacklist(doctor, patientId);
	}

	/**
	 * @param patientId
	 * @return
	 */
	public Patient loadPatient(int patientId) {
		return manager.loadPatient(patientId);
	}

}
