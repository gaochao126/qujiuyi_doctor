package com.jiuyi.doctor.appointment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.appointment.model.Appointment;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午2:46:52
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class AppointmentService {

	@Autowired
	AppointmentManager manager;

	public List<Appointment> loadUnhandleAppointment(Doctor doctor) {
		return manager.loadUnhandleAppointmentList(doctor);
	}

	public int loadDoctorAppointmentStatus(Doctor doctor) {
		return manager.loadInfoBase(doctor).getStatus();
	}
}
