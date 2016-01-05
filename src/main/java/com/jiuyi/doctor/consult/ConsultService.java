package com.jiuyi.doctor.consult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.consult.model.DoctorChat;
import com.jiuyi.doctor.consult.model.Consult;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午5:38:39
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ConsultService {

	@Autowired
	ConsultDao dao;

	@Autowired
	ConsultManager manager;

	public List<Consult> loadUnFinishConsult(Doctor doctor) {
		return manager.loadUnFinishedChatList(doctor);
	}

	/** 未处理的咨询请求 */
	public List<Consult> loadUnhandledChat(Doctor doctor) {
		return manager.loadUnhandledChatList(doctor, 1, Integer.MAX_VALUE);
	}

	/** 医生图文咨询配置 */
	public DoctorChat loadDoctorChat(Doctor doctor) {
		return manager.loadInfoBase(doctor);
	}

	public void updateDoctorChatStatus(Doctor doctor, ServiceStatus serviceStatus) {
		manager.updateDoctorChatStatus(doctor, serviceStatus);
	}

	public void updateDoctorChatPrice(Doctor doctor, int price) {
		manager.updateDoctorChatPrice(doctor, price);
	}

	public int loadDoctorChatStatus(Doctor doctor) {
		return loadDoctorChat(doctor).getStatus();
	}

	/**
	 * 注册的时候开通默认的咨询设置
	 * 
	 * @param doctor
	 */
	public void openDefaultConsult(Doctor doctor) {
		manager.openDefaulConsult(doctor);
	}
}
