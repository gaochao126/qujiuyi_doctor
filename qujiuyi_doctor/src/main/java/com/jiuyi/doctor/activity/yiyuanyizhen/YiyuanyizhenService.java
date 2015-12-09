package com.jiuyi.doctor.activity.yiyuanyizhen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午2:38:51
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class YiyuanyizhenService {

	@Autowired
	YiyuanyizhenDao dao;

	@Autowired
	YiyuanyizhenManager manager;

	/** 回加名额 */
	public void incNumber(int doctorId) {
		dao.addNumber(doctorId);
	}

	public boolean isOpen(Doctor doctor) {
		return manager.isOpen(doctor);
	}

	public void close(Doctor doctor) {
		manager.closeYiYuan(doctor);
	}
}
