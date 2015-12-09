package com.jiuyi.doctor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.consult.ConsultService;
import com.jiuyi.doctor.consult.model.DoctorChat;
import com.jiuyi.doctor.personal.PersonalDoctorWrapper;
import com.jiuyi.doctor.personal.PersonalService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:20:41
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DoctorServiceManager {

	@Autowired
	PersonalService personalService;

	@Autowired
	ConsultService chatService;

	/** 医生服务信息 */
	public ServerResult handleLoadInfo(Doctor doctor) {
		PersonalDoctorWrapper personalDoctorInfo = personalService.loadPersonalDoctorInfo(doctor);
		DoctorChat doctorChat = chatService.loadDoctorChat(doctor);
		ServerResult res = new ServerResult();
		res.put("chat", doctorChat.getStatus());
		res.put("personal", personalDoctorInfo.getStatus().ordinal());
		return res;
	}

}
