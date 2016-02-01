/**
 * 
 */
package com.jiuyi.doctor.smsverify;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * 
 * @author xutaoyang
 *
 */
@Repository
public class SmsDao extends DbBase {

	private static final String INSERT = "INSERT `t_sms_whitelist` VALUE(?)";
	private static final String SELECT_ALL = "SELECT * FROM `t_sms_whitelist`";
	private static final String SELECT = "SELECT COUNT(*) FROM `t_sms_whitelist` WHERE `phone`=?";
	private static final String DELETE = "DELETE FROM `t_sms_whitelist` WHERE `phone`=?";
	private static final String CLEAR = "TRUNCATE `t_sms_whitelist`";

	protected void insert(String phone) {
		jdbc.update(INSERT, phone);
	}

	protected List<String> selectAll() {
		return jdbc.queryForList(SELECT_ALL, String.class);
	}

	protected boolean inWhiteList(String phone) {
		return queryForInteger(SELECT, phone) > 0;
	}

	protected void delete(String phone) {
		jdbc.update(DELETE, phone);
	}

	protected void clear() {
		jdbc.update(CLEAR);
	}
}
