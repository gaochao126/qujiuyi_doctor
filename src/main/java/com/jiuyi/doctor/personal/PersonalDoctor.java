package com.jiuyi.doctor.personal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午3:47:07
 *
 * @Description 私人医生服务信息
 * 
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class PersonalDoctor implements ISerializableObj {

	private int id;
	private int doctorId;
	private int type;
	private int status;
	private int price;
	private Date updateDate;

	public PersonalDoctor() {
	}

	public PersonalDoctor(int doctorId, int type, int status, int price) {
		this.doctorId = doctorId;
		this.type = type;
		this.status = status;
		this.price = price;
	}

	public PersonalDoctor(int id, int doctorId, int type, int status, int price, Date updateDate) {
		this.id = id;
		this.doctorId = doctorId;
		this.type = type;
		this.status = status;
		this.price = price;
		this.updateDate = updateDate;
	}

	public static final RowMapper<PersonalDoctor> builder = new RowMapper<PersonalDoctor>() {
		@Override
		public PersonalDoctor mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int doctorId = rs.getInt("doctorId");
			int type = rs.getInt("type");
			int status = rs.getInt("status");
			int price = rs.getInt("price");
			Date updateDate = rs.getTimestamp("updateDate");
			return new PersonalDoctor(id, doctorId, type, status, price, updateDate);
		}
	};

	public int getId() {
		return id;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getType() {
		return type;
	}

	public int getPrice() {
		return price;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPrice(int price) {
		this.price = price;
		this.updateDate = new Date();
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
		res.put("type", this.type);
		res.put("status", this.status);
		res.put("price", this.price);
		return res;
	}

}
