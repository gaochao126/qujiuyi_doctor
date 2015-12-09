package com.jiuyi.doctor.activity.yiyuanyizhen;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午5:00:21
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class YiyuanyizhenDao extends DbBase {

	private static final String SELECT = "SELECT * FROM `t_yiyuanyizhen` WHERE `doctorId`=?";
	private static final String INSERT = "REPLACE INTO `t_yiyuanyizhen`(`doctorId`,`number`,`status`) VALUE(?,?,?)";
	private static final String UPDATE = "INSERT `t_yiyuanyizhen`(`doctorId`,`number`) VALUE(?,?) ON DUPLICATE KEY UPDATE `number`=?";
	private static final String DELETE = "DELETE FROM `t_yiyuanyizhen` WHERE `doctorId`=?";
	private static final String UPDATE_STATUS = "UPDATE `t_yiyuanyizhen` SET `status`=? WHERE `doctorId`=?";
	private static final String INC_NUMBER = "UPDATE `t_yiyuanyizhen` SET `number`=`number`+1 WHERE `doctorId`=?";

	protected Yiyuanyizhen load(Doctor doctor) {
		return queryForObject(SELECT, new Object[] { doctor.getId() }, Yiyuanyizhen.builder);
	}

	protected void open(Doctor doctor) {
		jdbc.update(INSERT, new Object[] { doctor.getId(), YiyuanyizhenManager.DEFAULT_NUMBER, YiyuanyizhenManager.STATUS_OPEN });
	}

	protected void update(Doctor doctor, Yiyuanyizhen yiyuanyizhen) {
		jdbc.update(UPDATE, new Object[] { doctor.getId(), yiyuanyizhen.getNumber(), yiyuanyizhen.getNumber() });
	}

	protected void delete(Doctor doctor) {
		jdbc.update(DELETE, new Object[] { doctor.getId() });
	}

	protected void updateStatus(Doctor doctor, int status) {
		jdbc.update(UPDATE_STATUS, new Object[] { status, doctor.getId() });
	}

	protected void addNumber(int doctorId) {
		jdbc.update(INC_NUMBER, new Object[] { doctorId });
	}
}
