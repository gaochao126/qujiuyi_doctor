package com.jiuyi.doctor.appointment.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 上午10:26:50
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AppointmentTimeWrapper implements ISerializableObj {

	private int status;

	private int price;

	private String limitDesease;

	private String message;

	private List<AppointmentTime> appointmentTimeList = new ArrayList<>();

	public static final RowMapper<AppointmentTimeWrapper> builder = new RowMapper<AppointmentTimeWrapper>() {
		@Override
		public AppointmentTimeWrapper mapRow(ResultSet rs, int rowNum) throws SQLException {
			int status = rs.getInt("status");
			int price = rs.getInt("price");
			String limitDesease = rs.getString("limitDesease");
			String message = rs.getString("message");
			return new AppointmentTimeWrapper(status, price, limitDesease, message);
		}
	};

	public AppointmentTimeWrapper() {
		this.status = ServiceStatus.CLOSED.ordinal();
	}

	public AppointmentTimeWrapper(int status, int price, String limitDesease, String message) {
		super();
		this.status = status;
		this.price = price;
		this.limitDesease = limitDesease;
		this.message = message;
	}

	public List<AppointmentTime> loadDailyAppointmentTimes(int weekday) {
		List<AppointmentTime> res = new ArrayList<AppointmentTime>();
		for (AppointmentTime appointmentTime : this.appointmentTimeList) {
			if (appointmentTime.getWeekday() == weekday) {
				res.add(appointmentTime);
			}
		}
		return res;
	}

	public void setNumber(AppointmentTime appointmentTime) {
		int weekday = appointmentTime.getWeekday();
		int timeZone = appointmentTime.getTimeZone();
		int number = appointmentTime.getNumber();
		AppointmentTime time = getByWeekdayAndTimeZone(weekday, timeZone);
		if (time == null) {
			time = new AppointmentTime(weekday, timeZone, number, "普通");
			this.appointmentTimeList.add(time);
			return;
		}
		time.setNumber(number);
	}

	public void setType(AppointmentTime appointmentTime) {
		int weekday = appointmentTime.getWeekday();
		int timeZone = appointmentTime.getTimeZone();
		String type = appointmentTime.getType();
		AppointmentTime time = getByWeekdayAndTimeZone(weekday, timeZone);
		if (time == null) {
			time = new AppointmentTime(weekday, timeZone, 5, type);
			this.appointmentTimeList.add(time);
			return;
		}
		time.setType(type);
	}

	protected AppointmentTime getByWeekdayAndTimeZone(int weekday, int timeZone) {
		for (AppointmentTime appointmentTime : appointmentTimeList) {
			if (appointmentTime.getWeekday() == weekday && appointmentTime.getTimeZone() == timeZone) {
				return appointmentTime;
			}
		}
		return null;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<AppointmentTime> getAppointmentTimeList() {
		return appointmentTimeList;
	}

	public void setAppointmentTimeList(List<AppointmentTime> appointmentTimeList) {
		this.appointmentTimeList = appointmentTimeList;
	}

	public String getLimitDesease() {
		return limitDesease;
	}

	public void setLimitDesease(String limitDesease) {
		this.limitDesease = limitDesease;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("status", this.status);
		res.put("price", this.price);
		res.put("limitDesease", this.limitDesease);
		res.put("message", this.message);
		res.putObjects("detail", this.appointmentTimeList);
		return res;
	}
}
