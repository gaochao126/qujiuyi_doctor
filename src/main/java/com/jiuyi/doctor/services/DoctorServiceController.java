package com.jiuyi.doctor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:20:21
 *
 * @Description 医生服务，费用相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class DoctorServiceController extends ControllerBase {

	public DoctorServiceController() throws Exception {
		super("service");
	}

	private static final String CMD = "service_";
	private static final String CMD_LOAD_INFO = CMD + "info"; // 服务主界面信息

	@Autowired
	DoctorServiceManager manager;

	@RequestMapping(CMD_LOAD_INFO)
	@ResponseBody
	public ServerResult handleLoadInfo(@TokenUser Doctor doctor) {
		return manager.handleLoadInfo(doctor);
	}

}
