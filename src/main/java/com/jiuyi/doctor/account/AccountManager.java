package com.jiuyi.doctor.account;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.account.model.AccountDetail;
import com.jiuyi.doctor.account.model.AccountDetailType;
import com.jiuyi.doctor.account.model.AccountInfo;
import com.jiuyi.doctor.account.model.Withdraw;
import com.jiuyi.doctor.bank.BankService;
import com.jiuyi.doctor.smsverify.SmsVerifyService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.donecounter.DoneCounter;
import com.jiuyi.frame.event.EventType;
import com.jiuyi.frame.event.annotations.ListenEvent;
import com.jiuyi.frame.event.events.EventLogout;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午3:45:07
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class AccountManager {

	@Autowired
	AccountDao dao;

	@Autowired
	BankService bankService;

	@Autowired
	DoneCounter doneCounter;

	@Autowired
	DBConfig dbConfig;

	@Autowired
	SmsVerifyService smsVerifyService;

	private BigDecimal zero = new BigDecimal(0);

	private static final int DETAIL_PAGE_SIZE = 10;

	public static final int INCOME_TYPE = 1;
	public static final int OUTCOME_TYPE = 2;

	public static final int TARGET_ZHIFUBAO = 1;
	public static final int TARGET_BANK = 2;

	private static final String TARGET_WITHDRAW = "account_withdraw";
	private ConcurrentHashMap<Integer, String> id_salt = new ConcurrentHashMap<>();

	@ListenEvent(EventType.USER_LOGOUT)
	public void onLogout(EventLogout event) {
		Doctor doctor = (Doctor) event.user;
		if (doctor != null) {
			this.id_salt.remove(doctor.getId());
		}
	}

	/** 我的账户信息 */
	protected ServerResult handleMineAccount(Doctor doctor) {
		AccountInfo accountInfo = loadAccount(doctor);
		ServerResult res = new ServerResult();
		res.putObject(accountInfo);
		return res;
	}

	/** 本来在注册的时候会自动创建一个账户，但是在直接插数据库或者批量导入的时候，没有新建账户，这里就判断为空则新建一个 */
	protected AccountInfo loadAccount(Doctor doctor) {
		AccountInfo accountInfo = dao.loadAccountInfo(doctor);
		if (accountInfo == null) {
			dao.newAccount(doctor);
			return new AccountInfo(doctor);
		}
		return accountInfo;
	}

	/** 医生账户明细 */
	protected ServerResult loadAccountDetail(Doctor doctor, int page) {
		if (page < 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		int startLimit = (page - 1) * DETAIL_PAGE_SIZE;
		List<AccountDetail> accountDetail = dao.loadAccountDetail(doctor, startLimit, DETAIL_PAGE_SIZE);
		return new ServerResult("list", accountDetail);
	}

	protected ServerResult loadWithdrawSalt(Doctor doctor) {
		String salt = StringUtil.getRandomStr(5);
		this.id_salt.put(doctor.getId(), salt);
		return new ServerResult("salt", salt);
	}

	/** 提现 */
	@Transactional(rollbackFor = Exception.class)
	protected ServerResult handleWithdrawCash(Doctor doctor, Withdraw withdraw) {
		if (withdraw.getTargetType() < 1 || withdraw.getTargetType() > 2) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		} else if (StringUtil.isNullOrEmpty(withdraw.getTarget()) || StringUtil.isNullOrEmpty(withdraw.getTargetName())) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}

		/** 判断银行 */
		if (withdraw.getTargetType() == TARGET_BANK) {
			if (StringUtil.isNullOrEmpty(withdraw.getAccountCity())) {
				return new ServerResult(ResultConst.NOT_SATISFY);
			}
			ResultConst checkBank = bankService.checkBankId(withdraw.getBankId());
			if (!checkBank.isSuccess()) {
				return new ServerResult(checkBank);
			}
		}

		/** 判断提现金额 */
		BigDecimal toWithdraw = withdraw.getMoney();
		if (toWithdraw.doubleValue() < 10) {
			return new ServerResult(ResultConst.WITHDRAW_MONEY_LT_TEN);
		}
		/** 判断余额 */
		BigDecimal balance = getBalance(doctor);
		if (balance.compareTo(zero) <= 0 || withdraw.getMoney().compareTo(balance) > 0) {
			return new ServerResult(ResultConst.MONEY_NOT_ENOUGH);
		}
		/** 每周只能提现一次 */
		int withdrawCountThisWeek = doneCounter.weekly.getCount(TARGET_WITHDRAW, doctor.getId());
		if (withdrawCountThisWeek >= dbConfig.getConfigInt("doctor.withdraw.weekly.limit")) {
			return new ServerResult(ResultConst.WITHDRAW_TIME_LIMIT);
		}

		/** 插入数据库 */
		dao.decBalance(doctor, withdraw.getMoney());
		long withdrawId = dao.addWithdraw(doctor, withdraw);
		dao.incTotalWithdraw(doctor, withdraw.getMoney());
		AccountDetail detail = new AccountDetail(doctor.getId(), String.valueOf(withdrawId), AccountDetailType.WITHDRAW.ordinal(), OUTCOME_TYPE, withdraw.getMoney());
		dao.addAccountDetail(doctor, detail);

		doneCounter.weekly.incCount(TARGET_WITHDRAW, doctor.getId());
		this.id_salt.remove(doctor.getId());
		return new ServerResult();
	}

	/** 余额 */
	protected BigDecimal getBalance(Doctor doctor) {
		return dao.getBalance(doctor);
	}

	/** 提现记录 */
	protected ServerResult handleWithdrawHistory(Doctor doctor) {
		return new ServerResult("withdraws", dao.withdrawHistory(doctor));
	}

	/** 是否设置了提现密码 */
	protected ServerResult hasPassword(Doctor doctor) {
		String password = dao.getPassword(doctor);
		return new ServerResult("havePwd", !StringUtil.isNullOrEmpty(password));
	}

	/** 设置提现密码 */
	protected ServerResult setPassword(Doctor doctor, String password, String code) {
		ServerResult checkPhoneCode = smsVerifyService.checkCode(doctor.getPhone(), code);
		if (!checkPhoneCode.isSuccess()) {
			return checkPhoneCode;
		}
		if (!StringUtil.isNullOrEmpty(dao.getPassword(doctor))) {// 暂时不能修改
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		dao.setPassword(doctor, password);
		return new ServerResult();
	}

	// ===以下暂未实现===
	/** 诊所账户信息 */
	protected ServerResult loadClinicAccount(Doctor doctor) {
		return new ServerResult();
	}

	/** 转账 */
	protected ServerResult handleTransferAccount(Doctor doctor) {
		return new ServerResult();
	}

}
