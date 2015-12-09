package com.jiuyi.doctor.config;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

public class DoctorConfig implements ISerializableObj {

	private int pushStatus;// 0开启 1关闭

	public DoctorConfig(int pushStatus) {
		this.pushStatus = pushStatus;
	}

	public void openPush() {
		this.pushStatus = 0;
	}

	public void closePush() {
		this.pushStatus = 1;
	}

	public int getPushStatus() {
		return this.pushStatus;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("pushStatus", this.pushStatus);
		return res;
	}

}
