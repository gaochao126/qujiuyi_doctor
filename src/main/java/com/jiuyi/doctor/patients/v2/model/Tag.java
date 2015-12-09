package com.jiuyi.doctor.patients.v2.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午2:31:23
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Tag implements ISerializableObj {

	private int id;
	private String name;
	private Date createTime;
	private List<Integer> patientIds = new ArrayList<>();

	public static final RowMapper<Tag> builder = new RowMapper<Tag>() {
		@Override
		public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Date createTime = rs.getTimestamp("createTime");
			return new Tag(id, name, createTime);
		}
	};

	public Tag(int id, String name, Date createTime) {
		this.id = id;
		this.name = name;
		this.createTime = createTime;
	}

	public boolean allNotExist(Integer[] patientIds) {
		for (Integer patientId : patientIds) {
			if (this.patientIds.contains(patientId)) {
				return false;
			}
		}
		return true;
	}

	public boolean allExist(List<Integer> patientIds) {
		for (Integer patientId : patientIds) {
			if (!this.patientIds.contains(patientId)) {
				return false;
			}
		}
		return true;
	}

	public void addPatients(List<Integer> addPatients) {
		this.patientIds.addAll(addPatients);
	}

	public void removePatients(List<Integer> removePatients) {
		this.patientIds.removeAll(removePatients);
	}

	public void addPatient(Integer patientId) {
		this.patientIds.add(patientId);
	}

	public void removePatient(Integer patientId) {
		this.patientIds.remove(patientId);
	}

	public Integer getPatientsCount() {
		return this.patientIds.size();
	}

	public void setPatientIds(List<Integer> patientIds) {
		this.patientIds = patientIds != null ? patientIds : new ArrayList<Integer>();
	}

	public List<Integer> getPatientIds() {
		return patientIds;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public boolean hasPatient(Integer patientId) {
		return this.patientIds.contains(patientId);
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		res.put("createTime", this.createTime);
		res.put("patientCount", this.patientIds.size());
		return res;
	}

}
