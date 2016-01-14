package com.jiuyi.doctor.patients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.model.DoctorPatient;
import com.jiuyi.doctor.patients.model.Patient;
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

	private @Autowired PatientManagerV2 manager;

	/** 添加医患关系 */
	public void addDoctorPatient(Doctor doctor, DoctorPatient doctorPatient) {
		manager.addDoctorPatient(doctor, doctorPatient);
	}

	/**
	 * @param patientId
	 * @return
	 */
	public Patient loadPatient(int patientId) {
		return manager.loadPatient(patientId);
	}

}
