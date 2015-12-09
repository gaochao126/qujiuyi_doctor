package com.jiuyi.doctor.patients.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.doctor.patients.PatientGroupManager;

/**
 * @Author: xutaoyang @Date: 下午6:36:40
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class PatientGroup {

	private int groupId;
	private String groupName;
	private int groupType;

	public PatientGroup(int groupId, String groupName, int groupType) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupType = groupType;
	}

	public PatientGroup(int groupId, String groupName) {
		this(groupId, groupName, PatientGroupManager.COMMON_GROUP);
	}

	public static RowMapper<PatientGroup> builder = new RowMapper<PatientGroup>() {

		@Override
		public PatientGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			int groupId = rs.getInt("id");
			String groupName = rs.getString("name");
			return new PatientGroup(groupId, groupName, PatientGroupManager.COMMON_GROUP);
		}
	};

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

}
