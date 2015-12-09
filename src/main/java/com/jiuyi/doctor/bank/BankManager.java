package com.jiuyi.doctor.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.reload.IReloader;
import com.jiuyi.frame.reload.ReloadManager;

/**
 * @Author: xutaoyang @Date: 下午3:22:49
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class BankManager implements IReloader {

	@Autowired
	BankDao dao;

	@Autowired
	ReloadManager reloadManager;

	private List<Bank> bankList = new ArrayList<>();
	private Map<Integer, Bank> id_bank = new HashMap<>();

	@PostConstruct
	public void init() {
		reloadManager.registerReloader("bank", this);
		reload();
	}

	/** 所有支持的银行 */
	protected ServerResult loadAllBank() {
		return new ServerResult("list", bankList);
	}

	/** 是否支持该银行 **/
	protected boolean containBankId(Integer bankId) {
		return this.id_bank.containsKey(bankId);
	}

	@Override
	public void reload() {
		bankList = dao.loadAllBank();
		Map<Integer, Bank> temp = new HashMap<>();
		for (Bank bank : bankList) {
			temp.put(bank.id, bank);
		}
		this.id_bank = temp;
	}

}
