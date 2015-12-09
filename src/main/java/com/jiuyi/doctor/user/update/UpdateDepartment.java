package com.jiuyi.doctor.user.update;

import com.jiuyi.doctor.hospitals.DepartmentService;
import com.jiuyi.doctor.user.UserDAO;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午4:18:09
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UpdateDepartment extends UpdateUserInfo {

	DepartmentService departmentService;

	public UpdateDepartment(String dbCol, UserDAO dao, DepartmentService departmentService) {
		super(dbCol, dao);
		this.departmentService = departmentService;
	}

	@Override
	public Doctor updateCol(Doctor doctor, Object value) {
		Integer departmentId = departmentService.getIdByName(String.valueOf(value));
		return super.updateCol(doctor, departmentId);
	}

}
