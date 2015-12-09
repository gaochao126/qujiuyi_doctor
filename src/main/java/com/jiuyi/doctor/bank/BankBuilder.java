package com.jiuyi.doctor.bank;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.jiuyi.frame.conf.DBConfig;

/**
 * @Author: xutaoyang @Date: 下午3:29:43
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class BankBuilder implements RowMapper<Bank> {

	@Autowired
	DBConfig dbConfig;

	@Override
	public Bank mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("bankId");
		String name = rs.getString("bankName");
		String logo = rs.getString("logoUrl");
		String logoUrl = dbConfig.getConfig("patient.bank.logoUrl.basePath") + logo;
		return new Bank(id, name, logoUrl);
	}
}
