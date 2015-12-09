package com.jiuyi.doctor.home.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 下午7:46:28
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class SystemMsg {

	private int id;
	private String title;
	private String content;
	private Date date;

	public static final RowMapper<SystemMsg> builder = new RowMapper<SystemMsg>() {
		@Override
		public SystemMsg mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String title = rs.getString("title");
			String content = rs.getString("content");
			Date date = rs.getDate("date");
			return new SystemMsg(id, title, content, date);
		}
	};

	public SystemMsg(String title, String content) {
		this.title = title;
		this.content = content;
		this.date = new Date();
	}

	public SystemMsg(int id, String title, String content, Date date) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
