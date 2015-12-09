package com.jiuyi.doctor.cron.consult;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 下午1:34:02
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ConsultInfo {

	public final String orderNumber;
	public final int doctorId;
	public final BigDecimal money;

	public ConsultInfo(String orderNumber, int doctorId, BigDecimal money) {
		this.orderNumber = orderNumber;
		this.doctorId = doctorId;
		this.money = money;
	}

	public static final RowMapper<ConsultInfo> builder = new RowMapper<ConsultInfo>() {
		@Override
		public ConsultInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String orderNumber = rs.getString("orderNumber");
			int doctorId = rs.getInt("doctorId");
			BigDecimal money = rs.getBigDecimal("totalAmount");
			return new ConsultInfo(orderNumber, doctorId, money);
		}
	};

}
