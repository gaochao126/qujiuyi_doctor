package com.jiuyi.doctor.hospitals.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午6:09:45
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Catagory implements ISerializableObj {

	public final int id;
	public final String name;

	public static final RowMapper<Catagory> builder = new RowMapper<Catagory>() {
		@Override
		public Catagory mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("cityId");
			String name = rs.getString("cityName");
			return new Catagory(id, name);
		}
	};

	public Catagory(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		return res;
	}

}
