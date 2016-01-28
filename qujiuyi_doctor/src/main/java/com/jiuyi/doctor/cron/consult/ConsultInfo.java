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

	private int orderId;
	private int doctorId;
	private BigDecimal money;

	public ConsultInfo(int orderId, int doctorId, BigDecimal money) {
		this.orderId = orderId;
		this.doctorId = doctorId;
		this.money = money;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public static final RowMapper<ConsultInfo> builder = new RowMapper<ConsultInfo>() {
		@Override
		public ConsultInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			int orderId = rs.getInt("orderId");
			int doctorId = rs.getInt("doctorId");
			BigDecimal money = rs.getBigDecimal("totalAmount");
			return new ConsultInfo(orderId, doctorId, money);
		}
	};

}
