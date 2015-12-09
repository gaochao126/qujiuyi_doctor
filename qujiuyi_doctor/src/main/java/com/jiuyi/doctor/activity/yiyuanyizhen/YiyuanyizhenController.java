package com.jiuyi.doctor.activity.yiyuanyizhen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:52:03
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class YiyuanyizhenController extends ControllerBase {

	private static final String CMD = "yiyuan_";
	private static final String CMD_LOAD_INFO = CMD + "info";
	private static final String CMD_OPEN = CMD + "open";
	private static final String CMD_UPDATE = CMD + "update";
	private static final String CMD_CLOSE = CMD + "close";

	@Autowired
	YiyuanyizhenManager manager;

	public YiyuanyizhenController() throws Exception {
		super(CMD);
	}

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult loadInfo(@TokenUser Doctor doctor) {
		return manager.loadInfo(doctor);
	}

	@RequestMapping(CMD_OPEN)
	public ServerResult openYiyuan(@TokenUser Doctor doctor) {
		return manager.openYiyuan(doctor);
	}

	@RequestMapping(CMD_UPDATE)
	public ServerResult updateYiyuan(@TokenUser Doctor doctor, @Param("config") Yiyuanyizhen yiyuanyizhen) {
		return manager.updateYiyuan(doctor, yiyuanyizhen);
	}

	@RequestMapping(CMD_CLOSE)
	public ServerResult closeYiYuan(@TokenUser Doctor doctor) {
		return manager.closeYiYuan(doctor);
	}
}
