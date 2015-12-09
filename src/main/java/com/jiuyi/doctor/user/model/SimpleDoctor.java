package com.jiuyi.doctor.user.model;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

public class SimpleDoctor implements ISerializableObj {

	private int id;
	private String name;
	private String head;
	private String title;
	private String hospital;

	public SimpleDoctor(int id, String name, String head, String title, String hospital) {
		this.id = id;
		this.name = name;
		this.head = head;
		this.title = title;
		this.hospital = hospital;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getHead() {
		return head;
	}

	public String getTitle() {
		return title;
	}

	public String getHospital() {
		return hospital;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", this.id);
		res.put("name", this.name);
		res.put("head", this.head);
		res.put("title", this.title);
		res.put("hospital", this.hospital);
		return res;
	}

}
