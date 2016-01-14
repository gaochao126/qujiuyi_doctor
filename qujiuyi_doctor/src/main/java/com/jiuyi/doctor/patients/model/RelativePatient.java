/**
 * 
 */
package com.jiuyi.doctor.patients.model;

import java.util.Date;

import com.jiuyi.frame.annotations.Column;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * 就诊人
 * 
 * @author xutaoyang
 *
 */
public class RelativePatient implements ISerializableObj {

	@Column("id")
	private int relativeId;
	private String name;
	private int gender;
	private Date birthday;

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("relativeId", relativeId);
		res.put("name", name);
		res.put("gender", gender);
		res.put("birthday", birthday);
		return res;
	}

	public int getRelativeId() {
		return relativeId;
	}

	public String getName() {
		return name;
	}

	public int getGender() {
		return gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setRelativeId(int relativeId) {
		this.relativeId = relativeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
}
