package com.jiuyi.doctor.account.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午5:19:49
 *
 * @Description 账户概要信息
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AccountInfo implements ISerializableObj {

	private int id;

	private int doctorId;

	/** 余额 */
	private BigDecimal balance;

	/** 即将到账 */
	private BigDecimal coming;

	/** 累计收入 */
	private BigDecimal totalIncome;

	/** 累计提现 */
	private BigDecimal totalWithdraw;

	public static final RowMapper<AccountInfo> builder = new RowMapper<AccountInfo>() {

		@Override
		public AccountInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int doctorId = rs.getInt("doctorId");
			BigDecimal balance = rs.getBigDecimal("balance");
			BigDecimal coming = rs.getBigDecimal("coming");
			BigDecimal totalIncome = rs.getBigDecimal("totalIncome");
			BigDecimal totalWithdraw = rs.getBigDecimal("totalWithdraw");
			return new AccountInfo(id, doctorId, balance, coming, totalIncome, totalWithdraw);
		}
	};

	public AccountInfo(Doctor doctor) {
		this.doctorId = doctor.getId();
		this.balance = new BigDecimal(0);
		this.coming = new BigDecimal(0);
		this.totalIncome = new BigDecimal(0);
		this.totalWithdraw = new BigDecimal(0);
	}

	public AccountInfo(int id, int doctorId, BigDecimal balance, BigDecimal coming, BigDecimal totalIncome, BigDecimal totalWithdraw) {
		this.id = id;
		this.doctorId = doctorId;
		this.balance = balance;
		this.coming = coming;
		this.totalIncome = totalIncome;
		this.totalWithdraw = totalWithdraw;
	}

	public int getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public BigDecimal getComing() {
		return coming;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public BigDecimal getTotalWithdraw() {
		return totalWithdraw;
	}

	public void setTotalWithdraw(BigDecimal totalWithdraw) {
		this.totalWithdraw = totalWithdraw;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setComing(BigDecimal coming) {
		this.coming = coming;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("balance", this.balance.doubleValue());
		res.put("coming", this.coming.doubleValue());
		res.put("totalIncome", this.totalIncome.doubleValue());
		res.put("totalWithDraw", this.totalWithdraw.doubleValue());
		return res;
	}

}
