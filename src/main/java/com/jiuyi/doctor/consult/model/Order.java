package com.jiuyi.doctor.consult.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 上午11:38:15
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Order {

	private String orderNumber;
	private int patientId;
	private int serviceType;
	private int couponId;
	private int status;
	private BigDecimal payAmount; // 使用银行付款
	private BigDecimal balance;// 使用余额付款
	private BigDecimal totalAmount;// 总共款项之和

	public static final RowMapper<Order> builder = new RowMapper<Order>() {
		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			String orderNumber = rs.getString("orderNumber");
			int patientId = rs.getInt("patientId");
			int serviceType = rs.getInt("serviceType");
			int couponId = rs.getInt("couponId");
			int status = rs.getInt("status");
			BigDecimal payAmount = rs.getBigDecimal("payAmount");
			BigDecimal balance = rs.getBigDecimal("balance");
			BigDecimal totalAmount = rs.getBigDecimal("totalAmount");
			return new Order(orderNumber, patientId, serviceType, couponId, status, payAmount, balance, totalAmount);
		}
	};

	public Order(String orderNumber, int patientId, int serviceType, int couponId, int status, BigDecimal payAmount, BigDecimal balance, BigDecimal totalAmount) {
		this.orderNumber = orderNumber;
		this.patientId = patientId;
		this.serviceType = serviceType;
		this.couponId = couponId;
		this.status = status;
		this.payAmount = payAmount;
		this.balance = balance;
		this.totalAmount = totalAmount;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getServiceType() {
		return serviceType;
	}

	public int getCouponId() {
		return couponId;
	}

	public BigDecimal getPayAmount() {
		return payAmount == null ? new BigDecimal(0) : payAmount;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getBalance() {
		return balance == null ? new BigDecimal(0) : balance;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
