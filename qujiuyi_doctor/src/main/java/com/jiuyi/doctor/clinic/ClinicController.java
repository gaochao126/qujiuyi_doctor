package com.jiuyi.doctor.clinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午1:56:43
 *
 * @Description 诊所相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class ClinicController extends ControllerBase {

	private static final String CMD = "clinic_";
	private static final String CMD_NEW_CLINIC = CMD + "open";
	private static final String CMD_UPDATE_INFO = CMD + "update";
	private static final String CMD_LOAD_INFO = CMD + "info";
	private static final String CMD_INVITE_DOCTOR = CMD + "invite";

	@Autowired
	ClinicManager manager;

	public ClinicController() throws Exception {
		super(CMD);
	}

	@RequestMapping(CMD_NEW_CLINIC)
	public ServerResult handleNewClinic(@TokenUser Doctor doctor) {
		return new ServerResult();
	}

	@RequestMapping(CMD_UPDATE_INFO)
	public ServerResult handleUpdateInfo(@TokenUser Doctor doctor, @Param("slogan") String slogan) {
		return manager.updateSlogan(doctor, slogan);
	}

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult handleLoadInfo(@TokenUser Doctor doctor) {
		return manager.loadDoctorClinicInfo(doctor);
	}

	@RequestMapping(CMD_INVITE_DOCTOR)
	public ServerResult handleInviteDoctor(@TokenUser Doctor doctor) {
		return new ServerResult();
	}
}
