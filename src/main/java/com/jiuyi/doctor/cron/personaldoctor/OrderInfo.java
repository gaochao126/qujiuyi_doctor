package com.jiuyi.doctor.cron.personaldoctor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 上午9:39:43
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class OrderInfo {

	public final String orderNumber;
	public final int doctorId;
	public final BigDecimal money;

	public OrderInfo(String orderNumber, int doctorId, BigDecimal money) {
		this.orderNumber = orderNumber;
		this.doctorId = doctorId;
		this.money = money;
	}

	public static final RowMapper<OrderInfo> builder = new RowMapper<OrderInfo>() {
		@Override
		public OrderInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			String orderNumber = rs.getString("orderNumber");
			int doctorId = rs.getInt("doctorId");
			BigDecimal money = rs.getBigDecimal("totalAmount");
			return new OrderInfo(orderNumber, doctorId, money);
		}
	};
}
