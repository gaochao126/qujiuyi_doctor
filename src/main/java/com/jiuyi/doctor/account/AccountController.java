package com.jiuyi.doctor.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.account.model.Withdraw;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午10:42:37
 *
 * @Description 医生，诊所的账户
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class AccountController extends ControllerBase {

	private static final String CMD_PRE = "account_";
	private static final String CMD_CLINIC_ACCOUNT = CMD_PRE + "clinic"; // 诊所账户
	private static final String CMD_ACCOUNT_INFO = CMD_PRE + "info";// 我的账户信息
	private static final String CMD_TRANSFER_ACCOUNT = CMD_PRE + "transfer";// 转账
	private static final String CMD_DETAIL_ACCOUNT = CMD_PRE + "detail";// 账户明细（收支）
	private static final String CMD_LOAD_SALT = CMD_PRE + "salt";// 获取随机字符串
	private static final String CMD_WITHDRAW_CASH = CMD_PRE + "withdraw";// 提现
	private static final String CMD_WITHDRAW_HISTORY = CMD_PRE + "withdraw_his";// 提现记录
	private static final String CMD_HAS_WITHDRAW_PASSWORD = CMD_PRE + "have_passwd";// 是否设置了提现密码
	private static final String CMD_SET_WITHDRAW_PASSWORD = CMD_PRE + "set_passwd";// 设置提现密码

	@Autowired
	private AccountManager manager;

	@RequestMapping(CMD_CLINIC_ACCOUNT)
	public ServerResult loadClinicAccount(@TokenUser Doctor doctor) {
		return manager.loadClinicAccount(doctor);
	}

	@RequestMapping(CMD_TRANSFER_ACCOUNT)
	public ServerResult handleTransferAccount(@TokenUser Doctor doctor) {
		return manager.handleTransferAccount(doctor);
	}

	@RequestMapping(CMD_ACCOUNT_INFO)
	public ServerResult loadMineAccount(@TokenUser Doctor doctor) {
		return manager.handleMineAccount(doctor);
	}

	@RequestMapping(CMD_DETAIL_ACCOUNT)
	public ServerResult loadAccountDetail(@TokenUser Doctor doctor, @Param("page") int page) {
		return manager.loadAccountDetail(doctor, page);
	}

	@RequestMapping(CMD_LOAD_SALT)
	public ServerResult loadSalt(@TokenUser Doctor doctor) {
		return manager.loadWithdrawSalt(doctor);
	}

	@RequestMapping(CMD_WITHDRAW_CASH)
	public ServerResult handleWithdrawCash(@TokenUser Doctor doctor, @Param("withdraw") Withdraw withdraw) {
		return manager.handleWithdrawCash(doctor, withdraw);
	}

	@RequestMapping(CMD_WITHDRAW_HISTORY)
	public ServerResult handleWithdrawHistory(@TokenUser Doctor doctor) {
		return manager.handleWithdrawHistory(doctor);
	}

	@RequestMapping(CMD_HAS_WITHDRAW_PASSWORD)
	public ServerResult hasWithdrawPassword(@TokenUser Doctor doctor) {
		return manager.hasPassword(doctor);
	}

	@RequestMapping(CMD_SET_WITHDRAW_PASSWORD)
	public ServerResult setWithdrawPassword(@TokenUser Doctor doctor, @Param("password") String password, @Param("code") String code) {
		return manager.setPassword(doctor, password, code);
	}
}
