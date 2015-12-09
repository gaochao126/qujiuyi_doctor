package com.jiuyi.doctor.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午1:34:58
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class FeedbackManager {

	@Autowired
	FeedbackDao dao;

	protected ServerResult feedback(Doctor doctor, String content, String contact) {
		contact = StringUtil.isNullOrEmpty(contact) ? doctor.getPhone() : contact;
		Feedback feedback = new Feedback(doctor.getId(), content, contact);
		dao.addFeedback(doctor, feedback);
		return new ServerResult();
	}

}
