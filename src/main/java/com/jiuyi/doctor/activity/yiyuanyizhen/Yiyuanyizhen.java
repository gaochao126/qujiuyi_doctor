package com.jiuyi.doctor.activity.yiyuanyizhen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午5:02:47
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Yiyuanyizhen implements ISerializableObj {

	private int doctorId;
	private int number;
	private int status;
	private Date openDate;

	public static final RowMapper<Yiyuanyizhen> builder = new RowMapper<Yiyuanyizhen>() {
		@Override
		public Yiyuanyizhen mapRow(ResultSet rs, int rowNum) throws SQLException {
			int doctorId = rs.getInt("doctorId");
			int number = rs.getInt("number");
			int status = rs.getInt("status");
			Date openDate = rs.getDate("openDate");
			return new Yiyuanyizhen(doctorId, number, status, openDate);
		}
	};

	public Yiyuanyizhen(int number, int status) {
		this.number = number;
		this.status = status;
	}

	public Yiyuanyizhen(int doctorId, int number, int status, Date openDate) {
		this.doctorId = doctorId;
		this.number = number;
		this.status = status;
		this.openDate = openDate;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getNumber() {
		return number;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("number", this.number);
		res.put("status", this.status);
		return res;
	}

}
