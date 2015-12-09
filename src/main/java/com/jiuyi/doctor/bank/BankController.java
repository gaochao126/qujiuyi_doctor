package com.jiuyi.doctor.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午3:22:32
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class BankController extends ControllerBase {

	private static final String CMD = "bank_";
	private static final String CMD_LOAD_ALL = "bank_all";

	public BankController() throws Exception {
		super(CMD);
	}

	@Autowired
	BankManager manager;

	@RequestMapping(CMD_LOAD_ALL)
	public ServerResult allBank(@TokenUser Doctor doctor) {
		return manager.loadAllBank();
	}

}
