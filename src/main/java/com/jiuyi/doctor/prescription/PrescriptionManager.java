/**
 * 
 */
package com.jiuyi.doctor.prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ServerResult;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class PrescriptionManager {

	private @Autowired PrescriptionDao dao;

	/**
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	protected ServerResult createPrescription(Doctor doctor, Integer patientId) {
		dao.createPrescription(doctor, patientId);
		return null;
	}

}
