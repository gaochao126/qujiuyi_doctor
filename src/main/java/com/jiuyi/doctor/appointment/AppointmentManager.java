package com.jiuyi.doctor.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.appointment.model.Appointment;
import com.jiuyi.doctor.appointment.model.AppointmentStatus;
import com.jiuyi.doctor.appointment.model.AppointmentTime;
import com.jiuyi.doctor.appointment.model.AppointmentTimeWrapper;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午10:20:04
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class AppointmentManager extends ManagerBase<Doctor, AppointmentTimeWrapper> {

	@Autowired
	private AppointmentDao dao;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	protected ServerResult loadInfo(Doctor doctor) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		return new ServerResult(info);
	}

	/** 获取某一天的配置信息 */
	protected ServerResult loadDayInfo(Doctor doctor, Integer weekday) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		List<AppointmentTime> res = info.loadDailyAppointmentTimes(weekday);
		return new ServerResult("list", res, true);
	}

	/** 开启服务 */
	protected ServerResult openService(Doctor doctor) {
		updateServiceStatus(doctor, ServiceStatus.OPENED);
		return new ServerResult();
	}

	/** 关闭服务 */
	protected ServerResult closeService(Doctor doctor) {
		updateServiceStatus(doctor, ServiceStatus.CLOSED);
		return new ServerResult();
	}

	/** 修改价格 */
	protected ServerResult updatePrice(Doctor doctor, int price) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		if (info.getStatus() == ServiceStatus.CLOSED.ordinal()) {
			return new ServerResult(ResultConst.SERVICE_NOT_OPEN);
		}
		info.setPrice(price);
		dao.updatePrice(doctor, price);
		return new ServerResult();
	}

	/** 设置限定疾病 */
	protected ServerResult updateLimitDesease(Doctor doctor, String limitDesease) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		if (info.getStatus() == ServiceStatus.CLOSED.ordinal()) {
			return new ServerResult(ResultConst.SERVICE_NOT_OPEN);
		} else if (limitDesease.length() > 50) {
			return new ServerResult(ResultConst.DATA_TOO_LONG);
		}
		info.setLimitDesease(limitDesease);
		dao.updateLimitDesease(doctor, limitDesease);
		return new ServerResult();
	}

	/** 修改给患者的留言 */
	protected ServerResult updateMessage(Doctor doctor, String message) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		if (info.getStatus() == ServiceStatus.CLOSED.ordinal()) {
			return new ServerResult(ResultConst.SERVICE_NOT_OPEN);
		} else if (message.length() > 100) {
			return new ServerResult(ResultConst.DATA_TOO_LONG);
		}
		info.setMessage(message);
		dao.updateMessage(doctor, message);
		return new ServerResult();
	}

	/** 设置名额 */
	protected ServerResult setNumber(Doctor doctor, AppointmentTime appointmentTime) {
		int weekday = appointmentTime.getWeekday();
		int timeZone = appointmentTime.getTimeZone();
		int number = appointmentTime.getNumber();
		if (weekday < 1 || weekday > 7 || timeZone < 0 || timeZone > 2 || number < -1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		if (info.getStatus() == ServiceStatus.CLOSED.ordinal()) {
			return new ServerResult(ResultConst.SERVICE_NOT_OPEN);
		}
		info.setNumber(appointmentTime);
		dao.updateNumber(doctor, appointmentTime);
		return new ServerResult();
	}

	/** 设置加号类型 */
	protected ServerResult setType(Doctor doctor, AppointmentTime appointmentTime) {
		int weekday = appointmentTime.getWeekday();
		int timeZone = appointmentTime.getTimeZone();
		String type = appointmentTime.getType();
		if (weekday < Calendar.SUNDAY || weekday > Calendar.SATURDAY || timeZone < 0 || timeZone > 2) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		if (type.length() > 10) {
			return new ServerResult(ResultConst.DATA_TOO_LONG);
		}
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		if (info.getStatus() == ServiceStatus.CLOSED.ordinal()) {
			return new ServerResult(ResultConst.SERVICE_NOT_OPEN);
		}
		info.setType(appointmentTime);
		dao.updateType(doctor, appointmentTime);
		return new ServerResult();
	}

	/** 未处理的预约 */
	protected ServerResult loadUnhandleAppointments(Doctor doctor) {
		return new ServerResult("list", loadUnhandleAppointmentList(doctor));
	}

	protected List<Appointment> loadUnhandleAppointmentList(Doctor doctor) {
		return dao.loadAppointmentsByStatus(doctor, AppointmentStatus.UNHANDLE);
	}

	/** 获取指定日期的预约 */
	protected ServerResult loadAppointmentsByDate(Doctor doctor, String dateStr) {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		return new ServerResult("list", dao.loadAppointmentsByDate(doctor, date));
	}

	/** 回应预约（接收或者拒绝） */
	protected ServerResult respAppointment(Doctor doctor, Long appointmentId, Boolean accept) {
		Appointment appointment = dao.loadAppointmentsById(doctor, appointmentId);
		if (appointment == null || appointment.doctorId != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (appointment.status != AppointmentStatus.UNHANDLE.ordinal()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.updateAppointmentStatus(doctor, appointmentId, accept ? AppointmentStatus.ACCEPT : AppointmentStatus.REFUSE);
		return new ServerResult();
	}

	/** 接收 */
	protected ServerResult acceptAppointment(Doctor doctor, Long appointmentId) {
		Appointment appointment = dao.loadAppointmentsById(doctor, appointmentId);
		if (appointment == null || appointment.doctorId != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (appointment.status != AppointmentStatus.UNHANDLE.ordinal()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.updateAppointmentStatus(doctor, appointmentId, AppointmentStatus.ACCEPT);
		return new ServerResult();
	}

	/** 拒绝 */
	protected ServerResult refuseAppointment(Doctor doctor, Long appointmentId, String reason) {
		Appointment appointment = dao.loadAppointmentsById(doctor, appointmentId);
		if (appointment == null || appointment.doctorId != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (appointment.status != AppointmentStatus.UNHANDLE.ordinal()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.refuse(doctor, appointmentId, AppointmentStatus.REFUSE, reason);
		return new ServerResult();
	}

	@Override
	protected AppointmentTimeWrapper constructInfo(Doctor doctor) {
		AppointmentTimeWrapper res = dao.loadAppointmentInfo(doctor);
		return res != null ? res : new AppointmentTimeWrapper();
	}

	private void updateServiceStatus(Doctor doctor, ServiceStatus serviceStatus) {
		AppointmentTimeWrapper info = loadInfoBase(doctor);
		info.setStatus(serviceStatus.ordinal());
		dao.updateStatus(doctor, serviceStatus);
	}

}
