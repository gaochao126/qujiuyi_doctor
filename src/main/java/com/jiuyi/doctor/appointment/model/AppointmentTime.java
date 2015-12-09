package com.jiuyi.doctor.appointment.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 上午10:20:42
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AppointmentTime implements ISerializableObj {

	private int weekday;
	private int timeZone;
	private int number;
	private String type;

	public static final RowMapper<AppointmentTime> builder = new RowMapper<AppointmentTime>() {
		@Override
		public AppointmentTime mapRow(ResultSet rs, int rowNum) throws SQLException {
			int weekday = rs.getInt("weekday");
			int timeZone = rs.getInt("timeZone");
			int number = rs.getInt("number");
			String type = rs.getString("type");
			return new AppointmentTime(weekday, timeZone, number, type);
		}
	};

	public AppointmentTime(int weekday, int timeZone, int number, String type) {
		this.weekday = weekday;
		this.timeZone = timeZone;
		this.number = number;
		this.type = type;
	}

	public int getWeekday() {
		return weekday;
	}

	public int getTimeZone() {
		return timeZone;
	}

	public int getNumber() {
		return number;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("weekday", this.weekday);
		res.put("timeZone", this.timeZone);
		res.put("number", this.number);
		res.put("type", this.type);
		return res;
	}

}
