package com.jiuyi.doctor.base;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.helper.ConstKeys;

/**
 * @Author: xutaoyang @Date: 上午9:32:56
 *
 * @Description 接收token的类，可以被集成
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class TokenObject implements ISerializableObj {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put(ConstKeys.TOKEN, token);
		return res;
	}

}
