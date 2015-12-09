package com.jiuyi.doctor.personal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午11:29:58
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PersonalManager extends ManagerBase<Doctor, PersonalDoctorWrapper> {

	@Autowired
	private PersonalDAO dao;

	protected ServerResult loadPrivateServiceInfo(Doctor doctor) {
		return new ServerResult("service", loadInfoBase(doctor));
	}

	/** 更新私人医生服务设置 */
	protected ServerResult updatePrivateServiceInfo(Doctor doctor, PersonalDoctor personalDoctor) {
		int type = personalDoctor.getType();
		int status = personalDoctor.getStatus();
		int price = personalDoctor.getPrice();
		if (!PersonalDocterType.checkType(type) || price < 0 || !ServiceStatus.checkId(status)) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		/* 设置默认价格 */
		if (price == 0) {
			personalDoctor.setPrice(PersonalDocterType.getPriceByType(type));
		}
		PersonalDoctorWrapper wrapper = loadInfoBase(doctor);
		wrapper.update(doctor, personalDoctor);
		dao.updateDoctorPersonal(doctor, personalDoctor);
		return new ServerResult();
	}

	/** 更新开关状态 */
	protected void updateStatus(Doctor doctor, PersonalDoctor personalDoctor) {
		PersonalDoctorWrapper wrapper = loadInfoBase(doctor);
		wrapper.updateStatus(doctor, personalDoctor);
		dao.updateDoctorPersonalStatus(doctor, personalDoctor.getType(), personalDoctor.getStatus(), PersonalDocterType.getPriceByType(personalDoctor.getType()));
	}

	/** 更新价格 */
	protected void updatePrice(Doctor doctor, PersonalDoctor personalDoctor) {
		PersonalDoctorWrapper wrapper = loadInfoBase(doctor);
		wrapper.updatePrice(doctor, personalDoctor);
		dao.updateDoctorPersonalPrice(doctor, personalDoctor.getType(), personalDoctor.getPrice());
	}

	@Override
	protected PersonalDoctorWrapper constructInfo(Doctor doctor) {
		List<PersonalDoctor> personalDoctorList = dao.loadPersonalDoctorList(doctor);
		return new PersonalDoctorWrapper(personalDoctorList);
	}

	/***
	 * 把医生所有新的私人患者申请设为已读
	 * 
	 * @param doctor
	 */
	protected ServerResult setToRead(Doctor doctor) {
		dao.setToRead(doctor);
		return new ServerResult();
	}

	protected int loadUnreadNewPersnalCount(Doctor doctor) {
		return dao.loadUnreadNewPersnalCount(doctor);
	}

}
