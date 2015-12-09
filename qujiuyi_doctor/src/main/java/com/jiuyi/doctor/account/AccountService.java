package com.jiuyi.doctor.account;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.account.model.AccountDetail;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午3:44:55
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class AccountService {

	@Autowired
	AccountDao dao;

	@Autowired
	AccountManager manager;

	/** 总收入 */
	public BigDecimal getTotalIncome(Doctor doctor) {
		BigDecimal totalIncome = dao.getTotalIncome(doctor);
		return totalIncome != null ? totalIncome : new BigDecimal(0);
	}

	/** 分分钟给你开个户 */
	public void openAccount(Doctor doctor) {
		if (manager.loadAccount(doctor) != null) {
			return;
		}
		dao.newAccount(doctor);
	}

	/** 增加即将到账 */
	public void incComing(Doctor doctor, BigDecimal add) {
		dao.incComing(doctor, add);
	}

	/** 增加余额 */
	public void incBalance(Doctor doctor, BigDecimal add) {
		dao.incBalance(doctor, add);
	}

	/** 即将到账转余额 */
	public void coming2Balance(Doctor doctor, BigDecimal transfer) {
		dao.coming2Balance(doctor, transfer);
	}

	/** 添加一笔收支详情 */
	public void addAccountDetail(Doctor doctor, AccountDetail accountDetail) {
		dao.addAccountDetail(doctor, accountDetail);
	}
}
