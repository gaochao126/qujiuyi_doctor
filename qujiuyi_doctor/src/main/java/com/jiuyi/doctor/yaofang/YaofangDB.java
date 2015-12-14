package com.jiuyi.doctor.yaofang;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.jiuyi.frame.base.DbBase;

public class YaofangDB extends DbBase {
	@Autowired
	@Override
	public void setDataSource(DataSource yaofangDataSource) {

	}

	@Autowired
	@Override
	public void setJdbc(JdbcTemplate yaofangJdbc) {
		this.jdbc = yaofangJdbc;
	}

	@Autowired
	@Override
	public void setJdbc(NamedParameterJdbcTemplate yaofangNamedJdbc) {
		this.namedJdbc = yaofangNamedJdbc;
	}
}
