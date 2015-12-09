package com.jiuyi.doctor.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.home.model.SystemMsg;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 上午10:37:01
 *
 * @Description 系统消息
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class SystemMsgManager {
	private List<SystemMsg> systemMsgs;
	private ConcurrentHashMap<Integer, SystemMsg> id_msg = new ConcurrentHashMap<>();

	@Autowired
	SystemMsgDao dao;

	@PostConstruct
	public void init() {
		List<SystemMsg> msgs = dao.loadAll();
		msgs = CollectionUtil.isNullOrEmpty(msgs) ? new ArrayList<SystemMsg>() : msgs;
		this.systemMsgs = Collections.synchronizedList(msgs);
		for (SystemMsg systemMsg : msgs) {
			this.id_msg.put(systemMsg.getId(), systemMsg);
		}
	}

	public void addSystemMsg(SystemMsg msg) {
		int id = dao.addSystemMsg(msg);
		msg.setId(id);
		this.systemMsgs.add(0, msg);
		this.id_msg.put(id, msg);
	}

	public List<SystemMsg> getSystemMsg(int page, int pageSize) {
		return CollectionUtil.getPage(this.systemMsgs, page, pageSize);
	}

	public SystemMsg getMsgById(Integer id) {
		return this.id_msg.get(id);
	}

	public void deleteMsg(Integer id) {
		for (Iterator<SystemMsg> iter = systemMsgs.listIterator(); iter.hasNext();) {
			SystemMsg msg = iter.next();
			if (msg.getId() == id) {
				iter.remove();
				break;
			}
		}
		id_msg.remove(id);
	}

	public static enum SystemMsgStatus {
		COMMON, EXPIRE, DELETED
	}
}
