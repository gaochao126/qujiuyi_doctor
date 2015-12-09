package com.jiuyi.doctor.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ResultConst;

/**
 * @Author: xutaoyang @Date: 下午3:43:10
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class BankService {

	@Autowired
	BankManager manager;

	public ResultConst checkBankId(Integer bankId) {
		return manager.containBankId(bankId) ? ResultConst.SUCCESS : ResultConst.BANK_NOT_SUPPORT;
	}

}
