package com.jiuyi.doctor.hospitals.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午4:34:20
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Department implements ISerializableObj {

	private int id;
	private String name;
	private int parentId;
	private List<Department> children = new ArrayList<>();

	public static final RowMapper<Department> builder = new RowMapper<Department>() {
		@Override
		public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int parentId = rs.getInt("parentId");
			return new Department(id, name, parentId);
		}
	};

	public Department(int id, String name, int parentId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isRoot() {
		return this.id == this.parentId;
	}

	public void addChild(Department department) {
		this.children.add(department);
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		if (!CollectionUtil.isNullOrEmpty(this.children)) {
			res.putObjects("children", this.children);
		}
		return res;
	}

}
