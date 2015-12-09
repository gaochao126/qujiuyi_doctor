package com.jiuyi.doctor.user.update;

import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.doctor.user.UserDAO;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ResultConst;

/**
 * @Author: xutaoyang @Date: 下午4:24:00
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UpdateTitle extends UpdateUserInfo {

	public UpdateTitle(String dbCol, UserDAO dao) {
		super(dbCol, dao);
	}

	public ResultConst check(Doctor doctor, Object value) {
		if (!super.check(doctor, value).isSuccess()) {
			return ResultConst.PARAM_ERROR;
		}
		Integer titleId = Integer.parseInt(value.toString());
		if (!DoctorTitle.checkId(titleId)) {
			return ResultConst.PARAM_ERROR;
		}
		return ResultConst.SUCCESS;
	}

}
