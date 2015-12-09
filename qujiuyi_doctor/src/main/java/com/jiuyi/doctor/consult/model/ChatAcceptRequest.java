package com.jiuyi.doctor.consult.model;

import java.util.HashMap;
import java.util.Map;

import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.httpclient.AbsHttpClient.IRequestEntity;

/**
 * @Author: xutaoyang @Date: 下午2:43:59
 *
 * @Description 医生同意接诊后，需要把信息同步到聊天服务器。
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ChatAcceptRequest implements IRequestEntity {

	private String cmd;
	private Doctor doctor;
	private MapObject params = new MapObject();

	public ChatAcceptRequest(String cmd, Doctor doctor, Consult consult) {
		this.doctor = doctor;
		this.cmd = cmd;
		params.put("sender", doctor.getId());
		params.put("senderName", doctor.getName());
		params.put("senderHead", doctor.getHeadPath());
		params.put("hospital", doctor.getHospital());
		params.put("title", DoctorTitle.getTitleNameById(doctor.getTitleId()));
		params.put("receiver", consult.getPatientId());
		params.put("serviceId", consult.getId());
		params.put("consultType", consult.getType());
	}

	public ChatAcceptRequest(String cmd, Doctor doctor, Consult consult, boolean accpetStatus) {
		this(cmd, doctor, consult);
		params.put("acceptStatus", accpetStatus ? 1 : 2);
	}

	@Override
	public Map<String, ? extends Object> genEntity() {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("cmd", cmd);
		res.put("token", this.doctor.getAccess_token());
		res.put("params", this.params);
		return res;
	}
}
