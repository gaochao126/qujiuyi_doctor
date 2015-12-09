package com.jiuyi.doctor.account.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 下午5:20:02
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AccountDetail implements ISerializableObj {

	private long id;
	private int doctorId;
	private String src;
	private int srcType;
	private int type;
	private BigDecimal money;
	private Date date;

	public static final RowMapper<AccountDetail> builder = new RowMapper<AccountDetail>() {

		@Override
		public AccountDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			int doctorId = rs.getInt("doctorId");
			String src = rs.getString("src");
			int srcType = rs.getInt("srcType");
			int type = rs.getInt("type");
			BigDecimal money = rs.getBigDecimal("money");
			Date date = rs.getTimestamp("date");
			return new AccountDetail(id, doctorId, src, srcType, type, money, date);
		}
	};

	public AccountDetail(int doctorId, String src, int srcType, int type, BigDecimal money) {
		this.doctorId = doctorId;
		this.src = src;
		this.srcType = srcType;
		this.type = type;
		this.money = money;
	}

	public AccountDetail(long id, int doctorId, String src, int srcType, int type, BigDecimal money, Date date) {
		this.id = id;
		this.doctorId = doctorId;
		this.src = src;
		this.srcType = srcType;
		this.type = type;
		this.money = money;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getSrcType() {
		return srcType;
	}

	public int getType() {
		return type;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public Date getDate() {
		return date;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setSrcType(int srcType) {
		this.srcType = srcType;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("src", this.src); // 来源ID
		res.put("srcType", this.srcType);// 来源类型
		res.put("type", this.type);// 1收入 2支出
		res.put("money", this.money.doubleValue());
		res.put("date", DateUtil.date2Str(this.date));
		return res;
	}

}
