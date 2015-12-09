package com.jiuyi.doctor.patients.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.patients.PatientGroupManager;
import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 上午10:50:25
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class PatientInfo implements ISerializableObj {

	private int patientId;
	private String phone;
	private int age;
	private int gender;
	private String name;
	private String head;
	private String addr;
	private String allergicHistory;
	private List<Integer> groupIds = new ArrayList<Integer>();
	private int type;// 普通患者，私人患者等
	private boolean returnVisit;
	private List<ReturnVisit> returnVisits = new ArrayList<ReturnVisit>();// 回访

	/** 概要信息 */
	public PatientInfo(int patientId, String name, int gender, String phone, String head, String addr) {
		this.patientId = patientId;
		this.name = name;
		this.gender = gender;
		this.phone = phone;
		this.head = head;
		this.addr = addr;
	}

	/** 概要信息 以及与医生的关系 */
	public PatientInfo(int patientId, int age, int gender, String name, String head, String phone) {
		this.patientId = patientId;
		this.age = age;
		this.gender = gender;
		this.name = name;
		this.head = head;
		this.phone = phone;
	}

	/** 详细信息 */
	public PatientInfo(int patientId, String name, int gender, String phone, String head, String addr, int age, int typeId, String allergicHistory) {
		this(patientId, name, gender, phone, head, addr);
		this.age = age;
		this.allergicHistory = allergicHistory;
	}

	public static final RowMapper<PatientInfo> builder = new RowMapper<PatientInfo>() {

		@Override
		public PatientInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int gender = rs.getInt("gender");
			String name = rs.getString("nickname");
			String phone = rs.getString("phone");
			String head = DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + rs.getString("headPortrait");
			Date birthday = rs.getDate("birthday");
			int age = birthday == null ? 0 : DateUtil.getYearGap(birthday);
			return new PatientInfo(id, age, gender, name, head, phone);
		}
	};
	public static final RowMapper<PatientInfo> simpleBuilder = new RowMapper<PatientInfo>() {

		@Override
		public PatientInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int gender = rs.getInt("gender");
			String name = rs.getString("nickname");
			String phone = rs.getString("phone");
			String head = DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + rs.getString("headPortrait");
			String addr = rs.getString("address");
			String allergicHistory = rs.getString("allergicHistory");
			PatientInfo res = new PatientInfo(id, name, gender, phone, head, addr);
			res.setAllergicHistory(allergicHistory);
			return res;
		}

	};

	public static final RowMapper<PatientInfo> patientBuilder = new RowMapper<PatientInfo>() {

		@Override
		public PatientInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("nickname");
			int gender = rs.getInt("gender");
			String phone = rs.getString("phone");
			String head = DBConfigStatic.getConfig(Constants.KEY_PATIENT_HEAD) + rs.getString("headPortrait");
			String addr = rs.getString("address");

			Date birthday = rs.getDate("birthday");
			int age = birthday == null ? 0 : DateUtil.getYearGap(birthday);

			String allergicHistory = rs.getString("allergicHistory");
			Integer type = rs.getInt("type");
			type = type == 0 ? -1 : rs.getInt("type");
			return new PatientInfo(id, name, gender, phone, head, addr, age, type, allergicHistory);
		}

	};

	public int getPatientId() {
		return patientId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public List<Integer> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Integer> groupIds) {
		if (groupIds == null) {
			return;
		}
		this.groupIds.addAll(groupIds);
	}

	public void addGroupId(Integer groupId) {
		this.groupIds.add(groupId);
	}

	public List<ReturnVisit> getReturnVisits() {
		return returnVisits;
	}

	public void setReturnVisits(List<ReturnVisit> returnVisits) {
		this.returnVisits = returnVisits;
		this.returnVisit = returnVisits != null && returnVisits.size() > 0;
		if (returnVisit) {
			this.groupIds.add(PatientGroupManager.SystemGroup.RETURN_VISIT.id);
		}
	}

	public String getHead() {
		return head;
	}

	public String getAddr() {
		return addr;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if (type == PatientGroupManager.TYPE_PRIVATE) {
			this.groupIds.add(PatientGroupManager.SystemGroup.PRIVATE.id);
		}
	}

	public boolean isReturnVisit() {
		return returnVisit;
	}

	public String getAllergicHistory() {
		return allergicHistory;
	}

	public void setAllergicHistory(String allergicHistory) {
		this.allergicHistory = allergicHistory;
	}

	public MapObject toSimpleInfo() {
		MapObject res = new MapObject();
		res.put("patientId", this.patientId);
		res.put("name", this.name);
		res.put("phone", this.phone);
		res.put("gender", this.gender);
		res.put("age", this.age);
		res.put("patientHead", this.head);
		res.put("addr", this.addr);
		return res;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = toSimpleInfo();
		res.put("type", this.type);
		res.put("groupIds", this.groupIds);
		res.put("returnVisit", this.returnVisit);
		res.put("returnVisits", this.returnVisits);
		res.put("allergicHistory", this.allergicHistory);
		return res;
	}
}
