package com.jiuyi.doctor.clinic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.appointment.AppointmentService;
import com.jiuyi.doctor.clinic.model.Clinic;
import com.jiuyi.doctor.clinic.model.DoctorClinic;
import com.jiuyi.doctor.consult.ConsultService;
import com.jiuyi.doctor.personal.PersonalService;
import com.jiuyi.doctor.prescribe.PrescribeService;
import com.jiuyi.doctor.user.UserService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午3:38:30
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ClinicManager extends ManagerBase<Doctor, Clinic> {

	@Autowired
	ClinicDao dao;

	@Autowired
	UserService userService;
	@Autowired
	AppointmentService appointmentService;
	@Autowired
	PrescribeService prescribeService;
	@Autowired
	PersonalService personalService;
	@Autowired
	ConsultService chatService;

	private static final String DEFAULT_SLOGAN = "心系群众，以爱服人 ";

	/** 医生注册完成后自动创建一个诊所 */
	protected ServerResult openClinic(Doctor doctor) {
		openNewClinic(doctor);
		return new ServerResult();
	}

	protected ServerResult loadDoctorClinicInfo(Doctor doctor) {
		ServerResult res = new ServerResult(loadInfoBase(doctor));
		res.put("head", doctor.getHeadPath());
		res.put("appointment", appointmentService.loadDoctorAppointmentStatus(doctor));
		res.put("prescribe", prescribeService.loadDoctorPrescribeStatus(doctor));
		res.put("personal", personalService.loadDoctorPersonalStatus(doctor));
		res.put("chat", chatService.loadDoctorChatStatus(doctor));
		return res;
	}

	/** 把医生加到指定诊所 */
	protected void addDoctorToClinic(Doctor doctor, Clinic clinic, DoctorPosition position) {
		DoctorClinic doctorClinic = new DoctorClinic(doctor.getId(), clinic.getId(), new Date(), position.ordinal());
		doctorClinic.setDoctor(doctor);
		clinic.addDoctor(doctorClinic, position);
		dao.addDoctor2Clinic(doctor, clinic, position);
	}

	/** 获取诊所的所有医生信息 */
	protected List<DoctorClinic> loadClinicDoctors(Clinic clinic) {
		if (!CollectionUtil.isNullOrEmpty(clinic.getDoctors())) {
			return clinic.getDoctors();
		}
		List<DoctorClinic> doctorsInClinic = dao.loadDoctorClinic(clinic);
		for (DoctorClinic doctorClinic : doctorsInClinic) {
			Doctor doctor = userService.getDoctorById(doctorClinic.getDoctorId());
			doctorClinic.setDoctor(doctor);
		}
		return doctorsInClinic;
	}

	/** 更新口号 */
	protected ServerResult updateSlogan(Doctor doctor, String slogan) {
		Clinic clinic = loadInfoBase(doctor);
		if (clinic == null || !clinic.isLeader(doctor)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.updateSlogan(clinic, slogan);
		clinic.setSlogan(slogan);
		return new ServerResult();
	}

	@Override
	protected Clinic constructInfo(Doctor doctor) {
		Clinic clinic = dao.loadClinic(doctor);
		if (clinic == null) {
			return openNewClinic(doctor);
		}
		List<DoctorClinic> doctors = loadClinicDoctors(clinic);
		clinic.setDoctors(doctors);
		return clinic;
	}

	private Clinic openNewClinic(Doctor doctor) {
		String name = doctor.getName() + doctor.getDepartment() + "工作室";
		Clinic clinic = new Clinic(name, DEFAULT_SLOGAN);
		int clinicId = dao.newClinic(doctor, clinic);
		clinic.setId(clinicId);
		addDoctorToClinic(doctor, clinic, DoctorPosition.LEADER);
		return clinic;
	}
}
