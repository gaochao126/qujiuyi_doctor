package com.jiuyi.doctor.consult.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 下午4:48:05
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UnreadMsg implements ISerializableObj {

	private int sender;
	private int chatType;
	private String chatContent;
	private Date chatTime;
	private String serviceId;

	public static final RowMapper<UnreadMsg> builder = new RowMapper<UnreadMsg>() {
		@Override
		public UnreadMsg mapRow(ResultSet rs, int rowNum) throws SQLException {
			int sender = rs.getInt("sender");
			int chatType = rs.getInt("chatType");
			String chatContent = rs.getString("chatContent");
			Date chatTime = rs.getTimestamp("chatTime");
			String serviceId = rs.getString("serviceId");
			return new UnreadMsg(sender, chatType, chatContent, chatTime, serviceId);
		}
	};

	public UnreadMsg(int sender, int chatType, String chatContent, Date chatTime, String serviceId) {
		this.sender = sender;
		this.chatType = chatType;
		this.chatContent = chatContent;
		this.chatTime = chatTime;
		this.serviceId = serviceId;
	}

	public int getSender() {
		return sender;
	}

	public int getChatType() {
		return chatType;
	}

	public String getChatContent() {
		return chatContent;
	}

	public Date getChatTime() {
		return chatTime;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public void setChatType(int chatType) {
		this.chatType = chatType;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("patientId", this.sender);
		res.put("type", this.chatType);
		res.put("content", this.chatContent);
		res.put("date", DateUtil.date2Str(this.chatTime));
		res.put("id", this.serviceId);
		return res;
	}

}
