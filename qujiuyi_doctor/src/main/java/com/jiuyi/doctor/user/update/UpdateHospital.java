package com.jiuyi.doctor.user.update;

import com.jiuyi.doctor.hospitals.HospitalService;
import com.jiuyi.doctor.user.UserDAO;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午3:59:05
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UpdateHospital extends UpdateUserInfo {

	private HospitalService hospitalService;

	public UpdateHospital(String dbCol, UserDAO dao, HospitalService hospitalService) {
		super(dbCol, dao);
		this.hospitalService = hospitalService;
	}

	@Override
	public Doctor updateCol(Doctor doctor, Object value) {
		int hospitalId = hospitalService.getIdByName(String.valueOf(value));
		return super.updateCol(doctor, hospitalId);
	}
}
