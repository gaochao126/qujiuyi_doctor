package com.jiuyi.doctor.patients.v2.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 上午9:58:46
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class SimplePatient implements ISerializableObj {

	public final Integer patientId;
	public final String patientName;
	public final String patientHead;
	public final String remark;// 医生对患者的备注名
	public final Integer gender;
	public final Integer age;
	public Integer src;

	public static final RowMapper<SimplePatient> builder = new RowMapper<SimplePatient>() {
		@Override
		public SimplePatient mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer patientId = rs.getInt("patientId");
			String patientName = rs.getString("nickname");
			String headPortrait = rs.getString("headPortrait");
			String remark = rs.getString("remark");
			String patientHead = StringUtil.isNullOrEmpty(headPortrait) ? "" : DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + headPortrait;
			Integer gender = rs.getInt("gender");
			Date birthday = rs.getDate("birthday");
			int age = birthday == null ? 0 : DateUtil.getYearGap(birthday);
			return new SimplePatient(patientId, patientName, remark, patientHead, gender, age);
		}
	};

	public static final RowMapper<SimplePatient> type_builder = new RowMapper<SimplePatient>() {
		@Override
		public SimplePatient mapRow(ResultSet rs, int rowNum) throws SQLException {
			SimplePatient res = builder.mapRow(rs, rowNum);
			Integer src = rs.getInt("src");
			res.src = src;
			return res;
		}
	};

	public SimplePatient(Integer patientId, String patientName, String remark, String patientHead, Integer gender, Integer age) {
		this.patientId = patientId;
		this.patientName = patientName;
		this.patientHead = patientHead;
		this.remark = remark;
		this.gender = gender;
		this.age = age;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("age", this.age);
		res.put("remark", this.remark);
		res.put("gender", this.gender);
		res.put("patientId", this.patientId);
		res.put("patientName", this.patientName);
		res.put("patientHead", this.patientHead);
		if (src != null && src != 0) {
			res.put("src", this.src);
		}
		return res;
	}

}
