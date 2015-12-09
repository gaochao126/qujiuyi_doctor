package com.jiuyi.doctor.user.update;

import com.jiuyi.doctor.user.UserDAO;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午3:44:36
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UpdateUserInfo {

	private String dbCol;
	private UserDAO dao;

	public UpdateUserInfo(String dbCol, UserDAO dao) {
		this.dbCol = dbCol;
		this.dao = dao;
	}

	public ResultConst check(Doctor doctor, Object value) {
		if (StringUtil.isNullOrEmpty(value.toString())) {
			return ResultConst.PARAM_ERROR;
		}
		return ResultConst.SUCCESS;
	}

	public Doctor updateCol(Doctor doctor, Object value) {
		return dao.updateSingleCol(doctor, dbCol, value);
	}
}
