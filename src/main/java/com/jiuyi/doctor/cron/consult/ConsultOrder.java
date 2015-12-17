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
	public final int orderId;
	public final int patientId;
	public final int doctorId;
	public final String doctorName;
	public final int couponId;
	public final BigDecimal totalAmount;

	public ConsultOrder(String consultId, int orderId, int patientId, int doctorId, String doctorName, int couponId, BigDecimal totalAmount) {
		this.consultId = consultId;
		this.orderId = orderId;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.couponId = couponId;
		this.totalAmount = totalAmount;
	}

	public static final RowMapper<ConsultOrder> builder = new RowMapper<ConsultOrder>() {
		@Override
		public ConsultOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			String consultId = rs.getString("id");
			int orderId = rs.getInt("orderId");
			int patientId = rs.getInt("patientId");
			int doctorId = rs.getInt("doctorId");
			String doctorName = rs.getString("name");
			int couponId = rs.getInt("couponId");
			BigDecimal totalAmount = rs.getBigDecimal("totalAmount");
			return new ConsultOrder(consultId, orderId, patientId, doctorId, doctorName, couponId, totalAmount);
		}
	};
}
