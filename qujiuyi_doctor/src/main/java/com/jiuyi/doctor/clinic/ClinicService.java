package com.jiuyi.doctor.clinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午3:38:09
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ClinicService {

	@Autowired
	ClinicManager manager;

	public void openClinic(Doctor doctor) {
		if (manager.loadInfoBase(doctor) != null) {
			return;
		}
		manager.openClinic(doctor);
	}

}
