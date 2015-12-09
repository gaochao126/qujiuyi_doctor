package com.jiuyi.doctor.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.appointment.model.AppointmentTime;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午10:19:50
 *
 * @Description 预约相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class AppointmentController extends ControllerBase {

	private static final String CMD = "appointment_";

	/** 设置预约信息相关 */
	private static final String CMD_LOAD_INFO = CMD + "info";// 获取信息
	private static final String CMD_LOAD_INFO_BY_WEEKDAY = CMD + "day_info";// 获取某一天的信息
	private static final String CMD_OPEN = CMD + "open";// 开启服务
	private static final String CMD_CLOSE = CMD + "close";// 关闭服务
	private static final String CMD_UPDATE_PRICE = CMD + "set_price";// 设置价格
	private static final String CMD_UPDATE_LIMIT_DESEASE = CMD + "set_limit_desease";// 限制疾病
	private static final String CMD_UPDATE_MESSAGE = CMD + "set_message";// 设置给患者的留言
	private static final String CMD_SET_NUMBER = CMD + "set_number";// 设置名额
	private static final String CMD_SET_TYPE = CMD + "set_type";// 设置加号类型

	/** 处理预约相关 */
	private static final String CMD_GET_UNHANDLE_APPOINTMENT = CMD + "load_unhandle";// 未处理的预约信息
	private static final String CMD_GET_APPOINTMENT_BY_DATE = CMD + "load_by_date";// 查询指定日期的预约
	private static final String CMD_ACCEPT_APPOINTMENT = CMD + "accept";// 接收预约
	private static final String CMD_REFUSE_APPOINTMENT = CMD + "refuse";// 拒绝预约

	@Autowired
	private AppointmentManager manager;

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult handleLoadInfo(@TokenUser Doctor doctor) {
		return manager.loadInfo(doctor);
	}

	@RequestMapping(CMD_LOAD_INFO_BY_WEEKDAY)
	public ServerResult handleLoadDayInfo(@TokenUser Doctor doctor, @Param("weekday") Integer weekday) {
		return manager.loadDayInfo(doctor, weekday);
	}

	@RequestMapping(CMD_OPEN)
	public ServerResult handleOpenService(@TokenUser Doctor doctor) {
		return manager.openService(doctor);
	}

	@RequestMapping(CMD_CLOSE)
	public ServerResult handleCloseService(@TokenUser Doctor doctor) {
		return manager.closeService(doctor);
	}

	@RequestMapping(CMD_UPDATE_PRICE)
	public ServerResult handleUpdatePrice(@TokenUser Doctor doctor, @Param("price") int price) {
		return manager.updatePrice(doctor, price);
	}

	@RequestMapping(CMD_UPDATE_LIMIT_DESEASE)
	public ServerResult handleUpdateLimitDesease(@TokenUser Doctor doctor, @Param("limitDesease") String limitDesease) {
		return manager.updateLimitDesease(doctor, limitDesease);
	}

	@RequestMapping(CMD_UPDATE_MESSAGE)
	public ServerResult handleUpdateMessage(@TokenUser Doctor doctor, @Param("message") String message) {
		return manager.updateMessage(doctor, message);
	}

	@RequestMapping(CMD_SET_NUMBER)
	public ServerResult handleSetNumber(@TokenUser Doctor doctor, @Param("info") AppointmentTime appointmentTime) {
		return manager.setNumber(doctor, appointmentTime);
	}

	@RequestMapping(CMD_SET_TYPE)
	public ServerResult handleSetType(@TokenUser Doctor doctor, @Param("info") AppointmentTime appointmentTime) {
		return manager.setType(doctor, appointmentTime);
	}

	@RequestMapping(CMD_GET_UNHANDLE_APPOINTMENT)
	public ServerResult handleLoadUnhandleAppointments(@TokenUser Doctor doctor) {
		return manager.loadUnhandleAppointments(doctor);
	}

	@RequestMapping(CMD_GET_APPOINTMENT_BY_DATE)
	public ServerResult handleLoadAppointmentsByDate(@TokenUser Doctor doctor, @Param("date") String date) {
		return manager.loadAppointmentsByDate(doctor, date);
	}

	@RequestMapping(CMD_ACCEPT_APPOINTMENT)
	public ServerResult handleAcceptAppointment(@TokenUser Doctor doctor, @Param("id") Long appointmentId) {
		return manager.acceptAppointment(doctor, appointmentId);
	}

	@RequestMapping(CMD_REFUSE_APPOINTMENT)
	public ServerResult handleRefuseAppointment(@TokenUser Doctor doctor, @Param("id") Long appointmentId, @Param("reason") String reason) {
		return manager.refuseAppointment(doctor, appointmentId, reason);
	}
}
