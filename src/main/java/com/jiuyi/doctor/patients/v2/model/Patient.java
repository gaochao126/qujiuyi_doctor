package com.jiuyi.doctor.patients.v2.model;

import java.util.List;

import com.jiuyi.frame.annotations.Age;
import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 上午9:58:46
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Patient implements ISerializableObj {

	private Integer patientId;
	private String name;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String headPortrait;
	private String remark;// 医生对患者的备注名
	private Integer gender;
	@Age
	private Integer age;
	private Integer src;
	private Integer type;
	private String phone;
	private String note;
	private List<Tag> tags;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("age", this.age);
		res.put("remark", this.remark);
		res.put("gender", this.gender);
		res.put("patientId", this.patientId);
		res.put("patientName", this.name);
		res.put("patientHead", this.headPortrait);
		res.put("note", this.note);
		res.put("type", this.type);
		res.put("phone", this.phone);
		res.putObjects("tags", tags);
		if (src != null && src != 0) {
			res.put("src", this.src);
		}
		return res;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public String getRemark() {
		return remark;
	}

	public Integer getGender() {
		return gender;
	}

	public Integer getAge() {
		return age;
	}

	public Integer getSrc() {
		return src;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setSrc(Integer src) {
		this.src = src;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public int getType() {
		return type;
	}

	public String getPhone() {
		return phone;
	}

	public String getNote() {
		return note;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setTags(List<Tag> tags) {
		this.tags = CollectionUtil.emptyIfNull(tags);
	}
}
