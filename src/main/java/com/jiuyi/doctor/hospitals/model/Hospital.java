package com.jiuyi.doctor.hospitals.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午4:26:47
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Hospital implements ISerializableObj {

	private int id;
	private String name;

	private int catagoryId;
	private String catagoryName;

	public static final RowMapper<Hospital> simple_builder = new RowMapper<Hospital>() {
		@Override
		public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			return new Hospital(id, name);
		}
	};

	public static final RowMapper<Hospital> builder = new RowMapper<Hospital>() {
		@Override
		public Hospital mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int catagoryId = rs.getInt("city");
			String catagoryName = rs.getString("cityName");
			return new Hospital(id, name, catagoryId, catagoryName);
		}
	};

	public Hospital(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Hospital(int id, String name, int catagoryId, String catagoryName) {
		this.id = id;
		this.name = name;
		this.catagoryId = catagoryId;
		this.catagoryName = catagoryName;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCatagoryId() {
		return catagoryId;
	}

	public String getCatagoryName() {
		return catagoryName;
	}

	public void setCatagoryId(int catagoryId) {
		this.catagoryId = catagoryId;
	}

	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		return res;
	}

}
