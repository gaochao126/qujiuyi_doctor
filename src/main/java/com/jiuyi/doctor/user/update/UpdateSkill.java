package com.jiuyi.doctor.user.update;

import com.jiuyi.doctor.user.UserDAO;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ResultConst;

public class UpdateSkill extends UpdateUserInfo {

	private int maxLength;

	public UpdateSkill(String dbCol, UserDAO dao, int maxLength) {
		super(dbCol, dao);
		this.maxLength = maxLength;
	}

	@Override
	public ResultConst check(Doctor doctor, Object value) {
		ResultConst rc = super.check(doctor, value);
		if (!rc.isSuccess()) {
			return rc;
		}
		String skill = value.toString();
		if (skill == null || skill.length() > maxLength) {
			return ResultConst.DATA_TOO_LONG;
		}
		return ResultConst.SUCCESS;
	}
}
