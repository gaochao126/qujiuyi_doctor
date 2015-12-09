package com.jiuyi.doctor.appointment;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.appointment.model.Appointment;
import com.jiuyi.doctor.appointment.model.AppointmentStatus;
import com.jiuyi.doctor.appointment.model.AppointmentTime;
import com.jiuyi.doctor.appointment.model.AppointmentTimeWrapper;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午10:20:12
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class AppointmentDao extends DbBase {

	private static final String SELECT = "SELECT * FROM `t_doctor_appointment` WHERE `doctorId`=?";
	private static final String SELECT_TIME_LIST = "SELECT * FROM `t_doctor_appointment_detail` WHERE `doctorId`=?";
	private static final String SELECT_APPOINTMENT_BY_ID = "SELECT * FROM `t_appointment` WHERE `id`=?";
	private static final String SELECT_APPOINTMENT_BY_DATE = "SELECT a.*,p.`headPortrait`,p.`name` FROM `t_appointment` a,`t_patient` p WHERE p.`id`=a.`patientId` AND `appointmentDate`=? AND `doctorId`=?";
	private static final String SELECT_APPOINTMENT_BY_STATUS = "SELECT a.*,p.`headPortrait`,p.`name` FROM `t_appointment` a,`t_patient` p WHERE p.`id`=a.`patientId` AND `status`=? AND `doctorId`=?";

	private static final String UPDATE_STATUS = "INSERT `t_doctor_appointment`(`doctorId`,`status`) VALUE(?,?) ON DUPLICATE KEY UPDATE `status`=?";
	private static final String UPDATE_PRICE = "INSERT `t_doctor_appointment`(`doctorId`,`price`) VALUE(?,?) ON DUPLICATE KEY UPDATE `price`=?";
	private static final String UPDATE_LIMIT_DESEASE = "INSERT `t_doctor_appointment`(`doctorId`,`limitDesease`) VALUE(?,?) ON DUPLICATE KEY UPDATE `limitDesease`=?";
	private static final String UPDATE_MESSAGE = "INSERT `t_doctor_appointment`(`doctorId`,`message`) VALUE(?,?) ON DUPLICATE KEY UPDATE `message`=?";
	private static final String UPDATE_APPOINTMENT_STATUS = "UPDATE `t_appointment` SET `status`=? WHERE `id`=?";
	private static final String REFUSE_APPOINTMENT = "UPDATE `t_appointment` SET `status`=?,`refuseReason`=? WHERE `id`=?";

	private static final String UPDATE_NUMBER = "INSERT `t_doctor_appointment_detail`(`doctorId`,`weekday`,`timezone`,`number`) VALUE(?,?,?,?) ON DUPLICATE KEY UPDATE `number`=?";
	private static final String UPDATE_TYPE = "INSERT `t_doctor_appointment_detail`(`doctorId`,`weekday`,`timezone`,`type`) VALUE(?,?,?,?) ON DUPLICATE KEY UPDATE `type`=?";

	protected AppointmentTimeWrapper loadAppointmentInfo(Doctor doctor) {
		AppointmentTimeWrapper info = queryForObject(SELECT, new Object[] { doctor.getId() }, AppointmentTimeWrapper.builder);
		if (info == null) {
			return null;
		}
		List<AppointmentTime> timeList = jdbc.query(SELECT_TIME_LIST, new Object[] { doctor.getId() }, AppointmentTime.builder);
		info.setAppointmentTimeList(timeList);
		return info;
	}

	protected void updateStatus(Doctor doctor, ServiceStatus serviceStatus) {
		jdbc.update(UPDATE_STATUS, doctor.getId(), serviceStatus.ordinal(), serviceStatus.ordinal());
	}

	protected void updatePrice(Doctor doctor, int price) {
		jdbc.update(UPDATE_PRICE, doctor.getId(), price, price);
	}

	protected void updateLimitDesease(Doctor doctor, String limitDesease) {
		jdbc.update(UPDATE_LIMIT_DESEASE, doctor.getId(), limitDesease, limitDesease);
	}

	protected void updateMessage(Doctor doctor, String message) {
		jdbc.update(UPDATE_MESSAGE, doctor.getId(), message, message);
	}

	protected void updateNumber(Doctor doctor, AppointmentTime appointmentTime) {
		jdbc.update(UPDATE_NUMBER, doctor.getId(), appointmentTime.getWeekday(), appointmentTime.getTimeZone(), appointmentTime.getNumber(), appointmentTime.getNumber());
	}

	protected void updateType(Doctor doctor, AppointmentTime appointmentTime) {
		jdbc.update(UPDATE_TYPE, doctor.getId(), appointmentTime.getWeekday(), appointmentTime.getTimeZone(), appointmentTime.getType(), appointmentTime.getType());
	}

	/** 预约相关 */
	protected Appointment loadAppointmentsById(Doctor doctor, Long id) {
		return queryForObject(SELECT_APPOINTMENT_BY_ID, new Object[] { id }, Appointment.simple_builder);
	}

	protected List<Appointment> loadAppointmentsByDate(Doctor doctor, Date date) {
		return jdbc.query(SELECT_APPOINTMENT_BY_DATE, new Object[] { date, doctor.getId() }, Appointment.builder);
	}

	protected List<Appointment> loadAppointmentsByStatus(Doctor doctor, AppointmentStatus status) {
		return jdbc.query(SELECT_APPOINTMENT_BY_STATUS, new Object[] { status.ordinal(), doctor.getId() }, Appointment.builder);
	}

	protected void updateAppointmentStatus(Doctor doctor, Long id, AppointmentStatus status) {
		jdbc.update(UPDATE_APPOINTMENT_STATUS, new Object[] { status.ordinal(), id });
	}

	protected void refuse(Doctor doctor, Long id, AppointmentStatus status, String reason) {
		jdbc.update(REFUSE_APPOINTMENT, new Object[] { status.ordinal(), reason, id });
	}

}
