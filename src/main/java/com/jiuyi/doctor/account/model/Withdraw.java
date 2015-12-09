package com.jiuyi.doctor.account.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 下午7:35:37
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Withdraw {

	private long id;
	private int doctorId;
	private int targetType;
	private String target;
	private String targetName;
	private BigDecimal money;
	private Date beginDate;
	private Date finishDate;

	// ===银行卡 需要
	private Integer bankId;
	private String accountCity;

	// ===状态===
	private int status;
	private String desc;// 备注

	public static final RowMapper<Withdraw> builder = new RowMapper<Withdraw>() {

		@Override
		public Withdraw mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			int doctorId = rs.getInt("doctorId");
			int targetType = rs.getInt("targetType");
			String target = rs.getString("target");
			String targetName = rs.getString("targetName");
			BigDecimal money = rs.getBigDecimal("money");
			Date beginDate = rs.getTimestamp("beginDate");
			Date finishDate = rs.getTime("finishDate");
			Integer bankId = rs.getInt("bankId");
			String accountCity = rs.getString("accountCity");
			int status = rs.getInt("status");
			String desc = rs.getString("desc");
			return new Withdraw(id, doctorId, targetType, target, targetName, money, beginDate, finishDate, bankId, accountCity, status, desc);
		}
	};

	public Withdraw() {
	}

	public Withdraw(long id, int doctorId, int targetType, String target, String targetName, BigDecimal money, Date beginDate, Date finishDate, Integer bankId, String accountCity, int status,
			String desc) {
		this.id = id;
		this.doctorId = doctorId;
		this.targetType = targetType;
		this.target = target;
		this.targetName = targetName;
		this.money = money;
		this.beginDate = beginDate;
		this.finishDate = finishDate;
		this.bankId = bankId;
		this.accountCity = accountCity;
		this.status = status;
		this.desc = desc;
	}

	public long getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getTargetType() {
		return targetType;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public Integer getBankId() {
		return bankId == null ? 0 : bankId;
	}

	public String getAccountCity() {
		return accountCity;
	}

	public int getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTarget() {
		return target;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public enum WithdrawStatus {
		/** 处理中 */
		PROCCESSING,
		/** 完成 */
		FINISH;
	}
}
