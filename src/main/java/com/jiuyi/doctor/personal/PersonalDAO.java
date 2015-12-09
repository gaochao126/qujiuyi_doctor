package com.jiuyi.doctor.personal;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午11:30:15
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class PersonalDAO extends DbBase {

	private static final String SELECT_UNREAD_NUM = "SELECT COUNT(*) FROM `t_personal_doctor` WHERE `doctorId`=? AND `readStatus`=0";
	private static final String SELECT_PRIVATE_SERVICE = "SELECT * FROM `t_doctor_private` WHERE `doctorId`=?";
	private static final String UPDATE_PRIVATE_SERVICE = "INSERT `t_doctor_private`(`doctorId`,`type`,`status`,`price`) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE `status`=?, `price`=?";
	private static final String UPDATE_READ_STATUS = "UPDATE `t_personal_doctor` SET `readStatus`=1 WHERE `doctorId`=?";

	protected List<PersonalDoctor> loadPersonalDoctorList(Doctor doctor) {
		return jdbc.query(SELECT_PRIVATE_SERVICE, new Object[] { doctor.getId() }, PersonalDoctor.builder);
	}

	protected void updateDoctorPersonalStatus(Doctor doctor, int type, int serviceStatus, int defaultPrice) {
		jdbc.update(UPDATE_PRIVATE_SERVICE, new Object[] { doctor.getId(), type, serviceStatus, defaultPrice, serviceStatus, defaultPrice });
	}

	protected void updateDoctorPersonalPrice(Doctor doctor, int type, int price) {
		int defaultStatus = ServiceStatus.CLOSED.ordinal();
		jdbc.update(UPDATE_PRIVATE_SERVICE, new Object[] { doctor.getId(), type, defaultStatus, price, defaultStatus, price });
	}

	protected void updateDoctorPersonal(Doctor doctor, PersonalDoctor personalDoctor) {
		int price = personalDoctor.getPrice();
		int status = personalDoctor.getStatus();
		jdbc.update(UPDATE_PRIVATE_SERVICE, doctor.getId(), personalDoctor.getType(), status, price, status, price);
	}

	protected int loadUnreadNewPersnalCount(Doctor doctor) {
		return queryForInteger(SELECT_UNREAD_NUM, new Object[] { doctor.getId() });
	}

	protected void setToRead(Doctor doctor) {
		jdbc.update(UPDATE_READ_STATUS, new Object[] { doctor.getId() });
	}
}
