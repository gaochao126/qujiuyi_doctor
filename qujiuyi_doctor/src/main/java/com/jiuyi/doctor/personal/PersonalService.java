package com.jiuyi.doctor.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 上午11:30:04
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PersonalService {

	@Autowired
	private PersonalManager manager;

	public PersonalDoctorWrapper loadPersonalDoctorInfo(Doctor doctor) {
		return manager.loadInfoBase(doctor);
	}

	public void updateStatus(Doctor doctor, PersonalDoctor personalDoctor) {
		manager.updateStatus(doctor, personalDoctor);
	}

	public void updatePrice(Doctor doctor, PersonalDoctor personalDoctor) {
		manager.updatePrice(doctor, personalDoctor);
	}

	public int loadDoctorPersonalStatus(Doctor doctor) {
		return loadPersonalDoctorInfo(doctor).getStatus().ordinal();
	}

	/**
	 * 未读的新的私人患者数量
	 * 
	 * @param doctor
	 * @return
	 */
	public int loadUnreadNewPersnalCount(Doctor doctor) {
		return manager.loadUnreadNewPersnalCount(doctor);
	}
}
