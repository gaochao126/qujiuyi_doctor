package com.jiuyi.doctor.chatserver;

import java.util.HashMap;
import java.util.Map;

import com.jiuyi.frame.httpclient.AbsHttpClient.IRequestEntity;

public class ChatServerRequestEntity implements IRequestEntity {

	protected Map<String, Object> root = new HashMap<>();

	public ChatServerRequestEntity() {
	}

	public ChatServerRequestEntity(String cmd) {
		this.root.put("cmd", cmd);
	}

	public void putParam(String key, Object value) {
		this.root.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public void putDetail(String key, Object val) {
		Map<String, Object> detail = (Map<String, Object>) root.get("params");
		if (detail == null) {
			detail = new HashMap<>();
			putParam("params", detail);
		}
		detail.put(key, val);
	}

	@Override
	public Map<String, ? extends Object> genEntity() {
		return this.root;
	}

}
