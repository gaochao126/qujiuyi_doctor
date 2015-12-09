package com.jiuyi.doctor.consult.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 下午3:32:53
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class UnreadMsgWrapper implements ISerializableObj {

	private Map<String, MsgItem> serviceId_item = new HashMap<>();

	public UnreadMsgWrapper(List<UnreadMsg> unreadMsgs) {
		for (UnreadMsg unreadMsg : unreadMsgs) {
			String serviceId = unreadMsg.getServiceId();
			if (!serviceId_item.containsKey(serviceId)) {
				serviceId_item.put(serviceId, new MsgItem());
			}
			serviceId_item.get(serviceId).addMsg(unreadMsg);
		}
	}

	@Override
	public MapObject serializeToMapObject() {
		List<MapObject> list = new ArrayList<>();
		for (MsgItem msgItem : this.serviceId_item.values()) {
			list.add(msgItem.serializeToMapObject());
		}
		MapObject res = new MapObject();
		res.put("list", list);
		return res;
	}

	private class MsgItem implements ISerializableObj {

		UnreadMsg unreadMsg;
		int count;

		public void addMsg(UnreadMsg unreadMsg) {
			if (this.unreadMsg == null || this.unreadMsg.getChatTime().before(unreadMsg.getChatTime())) {
				this.unreadMsg = unreadMsg;
			}
			count++;
		}

		@Override
		public MapObject serializeToMapObject() {
			MapObject res = unreadMsg.serializeToMapObject();
			res.put("count", this.count);
			return res;
		}

	}
}
