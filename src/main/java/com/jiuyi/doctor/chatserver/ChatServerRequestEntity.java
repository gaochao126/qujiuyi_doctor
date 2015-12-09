package com.jiuyi.doctor.chatserver;

import java.util.HashMap;
import java.util.Map;

import com.jiuyi.frame.httpclient.AbsHttpClient.IRequestEntity;

public class ChatServerRequestEntity implements IRequestEntity {

	private Map<String, Object> params = new HashMap<>();

	public ChatServerRequestEntity() {
	}

	public ChatServerRequestEntity(String cmd) {
		this.params.put("cmd", cmd);
	}

	public void putParam(String key, Object value) {
		this.params.put(key, value);
	}

	@Override
	public Map<String, ? extends Object> genEntity() {
		return this.params;
	}

}
