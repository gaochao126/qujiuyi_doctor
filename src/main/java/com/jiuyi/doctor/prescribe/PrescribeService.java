package com.jiuyi.doctor.prescribe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.prescribe.model.Prescribe;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午2:36:47
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PrescribeService {

	@Autowired
	PrescribeDao dao;

	@Autowired
	PrescribeManager manager;

	public List<Prescribe> loadUnhandlePresribe(Doctor doctor) {
		List<Prescribe> prescribes = dao.loadUnhandlePrescribe(doctor, 0, 100);
		return prescribes;
	}

	public int loadDoctorPrescribeStatus(Doctor doctor) {
		return manager.loadInfoBase(doctor).getStatus();
	}

}
