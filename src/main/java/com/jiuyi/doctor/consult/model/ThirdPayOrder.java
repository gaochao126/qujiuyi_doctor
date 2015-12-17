/**
 * 
 */
package com.jiuyi.doctor.consult.model;

import java.math.BigDecimal;

/**
 * 三方支付订单
 * 
 * @author xutaoyang
 *
 */
public class ThirdPayOrder {

	private int id;
	private int patientId;
	private BigDecimal totalAmount;
	private int orderType;
	private int couponId;

	public int getId() {
		return id;
	}

	public int getPatientId() {
		return patientId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public int getOrderType() {
		return orderType;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

}
