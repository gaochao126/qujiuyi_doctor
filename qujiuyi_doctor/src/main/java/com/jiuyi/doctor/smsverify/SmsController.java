package com.jiuyi.doctor.smsverify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

@RestController
public class SmsController {

	private static final String CMD = "sms_";
	private static final String CMD_SEND_CODE = CMD + "send_code";

	@Autowired
	private SmsVerifyService smsVerifyService;

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 *            电话
	 * @return
	 */
	@RequestMapping(CMD_SEND_CODE)
	public ServerResult sendCode(@Param("phone") String phone) {
		return smsVerifyService.sendCode(phone);
	}

}
