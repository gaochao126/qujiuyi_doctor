package com.jiuyi.doctor.user.model;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午2:10:23
 *
 * @Description 认领医生时返回的信息
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class RecognizeDoctor implements ISerializableObj {

	public final int id;
	public final String name;
	public final int hospitalId;
	public final String hospital;

	public final int departmentId;
	public final String department;

	public final String officePhone;
	public final String head;

	public final int status;
	public final int titleId;
	public final String title;

	public final String skill;// 特长
	public final String experience; // 职业经历
	public final String position;// 职位

	public RecognizeDoctor(int id, String name, int hospitalId, String hospital, int departmentId, String department, String officePhone, String headPath, int status, int titleId, String title,
			String skill, String experience, String position) {
		this.id = id;
		this.name = name;
		this.hospitalId = hospitalId;
		this.hospital = hospital;
		this.departmentId = departmentId;
		this.department = department;
		this.officePhone = officePhone;
		this.head = headPath;
		this.status = status;
		this.titleId = titleId;
		this.title = title;
		this.skill = skill;
		this.experience = experience;
		this.position = position;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		res.put("hospitalId", this.hospitalId);
		res.put("hospital", this.hospital);
		res.put("departmentId", this.departmentId);
		res.put("department", this.department);
		res.put("officePhone", this.officePhone);
		res.put("head", this.head);
		res.put("status", this.status);
		res.put("titleId", this.titleId);
		res.put("title", this.title);
		res.put("skill", this.skill);
		res.put("experience", this.experience);
		res.put("position", this.position);
		return res;
	}
}
