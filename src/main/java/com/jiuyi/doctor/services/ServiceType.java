package com.jiuyi.doctor.services;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午2:35:39
 *
 * @Description 目前医生可以提供的服务
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public enum ServiceType implements ISerializableObj {

	ALL(0, "全部", true), CHAT(1, "图文咨询", true), PRIVATE_SERVICE(2, "私人医生", true), TEL_CHAT(3, "电话咨询", false), APPOINTMENT(4, "门诊预约", true), PRESCRIBE(5, "配药", true);

	public final int id;
	public final String name;
	public final boolean available;

	private ServiceType(int id, String name, boolean available) {
		this.id = id;
		this.name = name;
		this.available = available;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("id", id);
		res.put("name", name);
		res.put("available", available);
		return res;
	}

}
