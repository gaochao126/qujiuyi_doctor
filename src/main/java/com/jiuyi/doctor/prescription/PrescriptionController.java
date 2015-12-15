/**
 * 
 */
package com.jiuyi.doctor.prescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ServerResult;

/**
 * 处方相关
 * 
 * @author xutaoyang
 *
 */
@RestController
public class PrescriptionController {

	private @Autowired PrescriptionManager manager;

	private static final String CMD = "prescription_";
	private static final String CMD_CREATE = CMD + "create";

	/**
	 * 创建处方，等待患者同意
	 * 
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	@RequestMapping(CMD_CREATE)
	public ServerResult createPrescription(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.createPrescription(doctor, patientId);
	}
}
