/**
 * 
 */
package com.jiuyi.doctor.prescription.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * 处方的备注，包括审核药师的审核结果和医生的修改批注
 * 
 * @author xutaoyang
 *
 */
public class PrescriptionRemark implements Comparable<PrescriptionRemark>, ISerializableObj {

	public static final RowMapper<PrescriptionRemark> builder = new RowMapper<PrescriptionRemark>() {

		@Override
		public PrescriptionRemark mapRow(ResultSet rs, int rowNum) throws SQLException {
			String username = rs.getString("username");
			int userType = rs.getInt("userType");
			String content = rs.getString("content");
			Date remarkDate = rs.getDate("remarkDate");
			return new PrescriptionRemark(username, remarkDate, userType, content);
		}
	};
	private String username;
	private Date remarkDate;
	private int userType;
	private String content;

	public PrescriptionRemark(String username, Date remarkDate, int userType, String content) {
		this.username = username;
		this.remarkDate = remarkDate;
		this.userType = userType;
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public Date getRemarkDate() {
		return remarkDate;
	}

	public int getUserType() {
		return userType;
	}

	public String getContent() {
		return content;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRemarkDate(Date remarkDate) {
		this.remarkDate = remarkDate;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int compareTo(PrescriptionRemark o) {
		return this.remarkDate.compareTo(o.getRemarkDate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jiuyi.frame.front.ISerializableObj#serializeToMapObject()
	 */
	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("username", this.username);
		res.put("userType", this.userType);
		res.put("content", this.content);
		res.put("date", DateUtil.date2Str(this.getRemarkDate()));
		return res;
	}

}
