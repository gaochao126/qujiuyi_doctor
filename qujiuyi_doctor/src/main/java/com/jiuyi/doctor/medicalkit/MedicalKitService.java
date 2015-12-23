/**
 * 
 */
package com.jiuyi.doctor.medicalkit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class MedicalKitService {

	private @Autowired MedicalKitManager manager;

	/**
	 * 规格是否在该医生的药箱中
	 * 
	 * @param doctor
	 * @param formatId
	 * @return
	 */
	public boolean isInKit(Doctor doctor, String formatId) {
		return manager.isInKit(doctor, formatId);
	}

}
