/**
 * 
 */
package com.jiuyi.doctor.chatserver;

import com.jiuyi.frame.util.JsonUtil;

/**
 * 系统消息
 * 
 * @author xutaoyang
 *
 */
public class SystemMsg extends ChatServerRequestEntity {

	private static final String CMD = "sendSystemMsg";

	public SystemMsg() {
		super(CMD);
	}

	public SystemMsg(UserType targetType, Integer target, String summary, Object content) {
		this();
		putDetail("targetType", targetType.ordinal());
		putDetail("target", target);
		putDetail("summary", summary);
		putDetail("content", JsonUtil.toJson(content));
	}

	public SystemMsg(UserType targetType, Integer target, String summary, Object content, String weixinMsg) {
		this(targetType, target, summary, content);
		putDetail("weixinMsg", weixinMsg);
	}
}
