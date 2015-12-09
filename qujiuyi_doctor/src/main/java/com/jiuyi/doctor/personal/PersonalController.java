package com.jiuyi.doctor.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午11:29:49
 *
 * @Description 私人医生相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class PersonalController extends ControllerBase {

	private static final String CMD = "personal_"; // 私人医生服务信息
	private static final String CMD_PRIVATE_SERVICE_INFO = CMD + "info"; // 私人医生服务信息
	private static final String CMD_SET_PRIVATE_SERVICE = CMD + "set"; // 设置私人医生服务
	private static final String CMD_SET_TO_READ = CMD + "set_to_read";

	@Autowired
	PersonalManager manager;

	@RequestMapping(CMD_PRIVATE_SERVICE_INFO)
	public ServerResult handleLoadPrivateServiceInfo(@TokenUser Doctor doctor) {
		return manager.loadPrivateServiceInfo(doctor);
	}

	@RequestMapping(CMD_SET_PRIVATE_SERVICE)
	public ServerResult handleUpdatePrivateServiceInfo(@TokenUser Doctor doctor, @Param("service") PersonalDoctor privateService) {
		return manager.updatePrivateServiceInfo(doctor, privateService);
	}

	/**
	 * 把所有新的私人患者申请记录设为已读
	 * 
	 * @param doctor
	 * @return
	 */
	@RequestMapping(CMD_SET_TO_READ)
	public ServerResult handleUpdatePrivateServiceInfo(@TokenUser Doctor doctor) {
		return manager.setToRead(doctor);
	}
}
