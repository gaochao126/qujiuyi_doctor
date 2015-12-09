package com.jiuyi.doctor.activity.yiyuanyizhen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:55:01
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class YiyuanyizhenManager {

	@Autowired
	YiyuanyizhenDao dao;

	private static final int STATUS_CLOSE = 0;
	protected static final int STATUS_OPEN = 1;
	protected static final int DEFAULT_NUMBER = 10; // 默认名额

	protected ServerResult loadInfo(Doctor doctor) {
		Yiyuanyizhen yiyuanyizhen = loadYiyuanyizhen(doctor);
		return new ServerResult(yiyuanyizhen != null ? yiyuanyizhen : new Yiyuanyizhen(DEFAULT_NUMBER, STATUS_CLOSE));
	}

	protected ServerResult openYiyuan(Doctor doctor) {
		Yiyuanyizhen yiyuanyizhen = dao.load(doctor);
		if (yiyuanyizhen == null) {
			dao.open(doctor);
		} else {
			dao.updateStatus(doctor, STATUS_OPEN);
		}
		return new ServerResult();
	}

	protected ServerResult updateYiyuan(Doctor doctor, Yiyuanyizhen yiyuanyizhen) {
		dao.update(doctor, yiyuanyizhen);
		return new ServerResult();
	}

	protected ServerResult closeYiYuan(Doctor doctor) {
		dao.updateStatus(doctor, STATUS_CLOSE);
		return new ServerResult();
	}

	protected Yiyuanyizhen loadYiyuanyizhen(Doctor doctor) {
		return dao.load(doctor);
	}

	protected boolean isOpen(Doctor doctor) {
		Yiyuanyizhen yiyuanyizhen = loadYiyuanyizhen(doctor);
		return yiyuanyizhen != null && yiyuanyizhen.getStatus() == STATUS_OPEN;
	}
}
