package com.jiuyi.doctor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.ServerResult;

@Service
public class ConfigManager extends ManagerBase<Doctor, DoctorConfig> {

	@Autowired
	private ConfigDao dao;

	protected ServerResult loadInfo(Doctor doctor) {
		return new ServerResult(loadInfoBase(doctor));
	}

	protected ServerResult closePush(Doctor doctor) {
		DoctorConfig doctorConfig = loadInfoBase(doctor);
		doctorConfig.closePush();
		dao.closePush(doctor);
		return new ServerResult();
	}

	protected ServerResult openPush(Doctor doctor) {
		DoctorConfig doctorConfig = loadInfoBase(doctor);
		doctorConfig.openPush();
		dao.openPush(doctor);
		return new ServerResult();
	}

	@Override
	protected DoctorConfig constructInfo(Doctor doctor) {
		DoctorConfig doctorConfig = dao.selectDoctorConfig(doctor);
		return doctorConfig != null ? doctorConfig : new DoctorConfig(0);
	}

}
