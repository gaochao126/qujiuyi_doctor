package com.jiuyi.doctor.cron.consult;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 上午10:01:45
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ConsultOrder {

	public final String consultId;
	public final String orderNumber;
	public final int patientId;
	public final int doctorId;
	public final String doctorName;
	public final int couponId;
	public final BigDecimal payAmount;
	public final BigDecimal balance;

	public ConsultOrder(String consultId, String orderNumber, int patientId, int doctorId, String doctorName, int couponId, BigDecimal payAmount, BigDecimal balance) {
		this.consultId = consultId;
		this.orderNumber = orderNumber;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.couponId = couponId;
		this.payAmount = payAmount;
		this.balance = balance;
	}

	public static final RowMapper<ConsultOrder> builder = new RowMapper<ConsultOrder>() {
		@Override
		public ConsultOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			String consultId = rs.getString("id");
			String orderNumber = rs.getString("orderNumber");
			int patientId = rs.getInt("patientId");
			int doctorId = rs.getInt("doctorId");
			String doctorName = rs.getString("doctorName");
			int couponId = rs.getInt("couponId");
			BigDecimal payAmount = rs.getBigDecimal("payAmount");
			BigDecimal balance = rs.getBigDecimal("balance");
			return new ConsultOrder(consultId, orderNumber, patientId, doctorId, doctorName, couponId, payAmount, balance);
		}
	};
}
