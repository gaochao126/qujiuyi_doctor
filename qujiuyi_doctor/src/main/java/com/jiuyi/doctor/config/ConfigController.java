package com.jiuyi.doctor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ServerResult;

@RestController
public class ConfigController {

	private static final String CMD = "config_";
	private static final String CMD_LOAD_INFO = CMD + "info";
	private static final String CMD_CLOSE_PUSH = CMD + "close_push";
	private static final String CMD_OPEN_PUSH = CMD + "open_push";

	@Autowired
	private ConfigManager manager;

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult loadInfo(@TokenUser Doctor doctor) {
		return manager.loadInfo(doctor);
	}

	@RequestMapping(CMD_CLOSE_PUSH)
	public ServerResult closePush(@TokenUser Doctor doctor) {
		return manager.closePush(doctor);
	}

	@RequestMapping(CMD_OPEN_PUSH)
	public ServerResult openPush(@TokenUser Doctor doctor) {
		return manager.openPush(doctor);
	}

}
