package com.jiuyi.doctor.consult.model;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午3:54:02
 *
 * @Description 图文咨询服务信息
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DoctorChat implements ISerializableObj {

	private int doctorId;
	private int status;
	private int price;

	public DoctorChat(int doctorId, int status, int price) {
		this.doctorId = doctorId;
		this.status = status;
		this.price = price;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public int getStatus() {
		return status;
	}

	public int getPrice() {
		return price;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("status", this.status);
		res.put("price", this.price);
		return res;
	}

}
