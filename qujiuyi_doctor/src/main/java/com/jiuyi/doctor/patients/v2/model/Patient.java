package com.jiuyi.doctor.patients.v2.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.DateUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午3:52:30
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Patient extends SimplePatient {

	public List<Tag> tags;
	public int type;
	public final String phone;
	public final String note;

	public static final RowMapper<Patient> builder = new RowMapper<Patient>() {
		@Override
		public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer patientId = rs.getInt("id");
			String patientName = rs.getString("nickname");
			String headPortrait = rs.getString("headPortrait");
			String remark = rs.getString("remark");
			String note = rs.getString("note");
			String patientHead = StringUtil.isNullOrEmpty(headPortrait) ? "" : DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + headPortrait;
			Integer gender = rs.getInt("gender");
			Date birthday = rs.getDate("birthday");
			int age = birthday == null ? 0 : DateUtil.getYearGap(birthday);
			String phone = rs.getString("phone");
			return new Patient(patientId, patientName, remark, patientHead, gender, age, note, phone);
		}
	};

	public Patient(Integer patientId, String patientName, String remark, String patientHead, Integer gender, Integer age, String note, String phone) {
		super(patientId, patientName, remark, patientHead, gender, age);
		this.note = note;
		this.phone = phone;
	}

	public void setTags(List<Tag> tags) {
		this.tags = CollectionUtil.emptyIfNull(tags);
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = super.serializeToMapObject();
		res.put("note", this.note);
		res.put("type", this.type);
		res.put("phone", this.phone);
		res.putObjects("tags", tags);
		return res;
	}
}
