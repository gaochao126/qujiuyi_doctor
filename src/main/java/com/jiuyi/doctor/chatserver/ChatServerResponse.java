package com.jiuyi.doctor.chatserver;

import java.util.Map;

public class ChatServerResponse {

	private String cmd;
	private int resultCode;
	private String resultDesc;

	private Map<String, Object> detail;

	public boolean isSuccess() {
		return this.resultCode == 0;
	}

	public String getCmd() {
		return cmd;
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	@SuppressWarnings("unchecked")
	public <T> T getField(String key) {
		return (T) detail.get(key);
	}
}
