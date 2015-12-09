package com.jiuyi.doctor.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午11:44:47
 *
 * @Description 意见反馈
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class FeedbackController extends ControllerBase {

	private static final String CMD = "feedback";

	public FeedbackController() throws Exception {
		super(CMD);
	}

	@Autowired
	FeedbackManager manager;

	@RequestMapping(CMD)
	@ResponseBody
	public ServerResult feedback(@TokenUser Doctor doctor, @Param("content") String feedback, @Param("contact") String contact) {
		return manager.feedback(doctor, feedback, contact);
	}

}
