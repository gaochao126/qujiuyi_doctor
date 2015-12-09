package com.jiuyi.doctor.bank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午3:22:55
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class BankDao extends DbBase {

	@Autowired
	BankBuilder bankBuilder;

	private static final String SELECT = "SELECT * FROM `t_bank` WHERE `status`=1";

	protected List<Bank> loadAllBank() {
		return jdbc.query(SELECT, bankBuilder);
	}
}
