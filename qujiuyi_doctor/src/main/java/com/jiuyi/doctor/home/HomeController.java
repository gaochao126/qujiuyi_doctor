package com.jiuyi.doctor.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午2:47:15
 *
 * @Description 关于首页显示
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class HomeController extends ControllerBase {

	private static final String CMD = "home_";
	private static final String CMD_LOAD_INFO = CMD + "info";
	private static final String CMD_EVALUATION = CMD + "evaluation"; // 评价信息
	private static final String CMD_SYSTEM_MSG = CMD + "system_msg";// 系统消息

	@Autowired
	private HomeManager manager;

	public HomeController() throws Exception {
		super(CMD);
	}

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult loadHomeInfo(@TokenUser Doctor doctor) {
		return manager.loadHomeInfo(doctor);
	}

	@RequestMapping(CMD_EVALUATION)
	public ServerResult loadEvaluation(@TokenUser Doctor doctor, @Param("page") Integer page) {
		if (page < 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		return manager.loadEvaluation(doctor, page);
	}

	@RequestMapping(CMD_SYSTEM_MSG)
	public ServerResult loadSystemMsg(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadSystemMsg(doctor, page, pageSize);
	}
}
