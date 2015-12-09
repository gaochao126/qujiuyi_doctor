package com.jiuyi.doctor.praise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

@RestController
public class PraiseController extends ControllerBase {

	private static final String CMD = "praise_";
	private static final String CMD_TOGGLE = CMD + "toggle";
	private static final String CMD_RECOMMEND_DOCTOR = CMD + "recommend_doctor";

	@Autowired
	private PraiseManager manager;

	@RequestMapping(CMD_TOGGLE)
	public ServerResult togglePraise(@TokenUser Doctor doctor, @Param("id") Integer doctorId) {
		return manager.togglePraise(doctor, doctorId);
	}

	@RequestMapping(CMD_RECOMMEND_DOCTOR)
	public ServerResult recommendDoctor(@TokenUser Doctor doctor, @Param("num") Integer number) {
		return manager.recommendDoctor(doctor, number);
	}

}
