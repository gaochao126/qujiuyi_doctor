package com.jiuyi.doctor.patients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午5:23:23
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PatientService {

	@Autowired
	PatientDAO dao;

	@Autowired
	PatientManager manager;

	/** 是不是私人患者 */
	public boolean isPersonalPatient(Doctor doctor, int patientId) {
		return dao.isPersonalPatient(doctor.getId(), patientId);
	}

	/** 在不在患者群里 */
	public boolean hasPatient(Doctor doctor, int patientId) {
		return dao.hasPatient(doctor, patientId);
	}

	/**
	 * 添加患者
	 * 
	 * @param doctor
	 * @param patientId
	 */
	public void addPatient(Doctor doctor, int patientId) {
		manager.addPatient(doctor, patientId);
	}
}
