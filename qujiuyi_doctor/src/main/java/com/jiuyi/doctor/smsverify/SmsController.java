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
	private static final String CMD_SEND_VOICE_CODE = CMD + "voice_code";
	private static final String CMD_WHITE_LIST = CMD + "white_list";
	private static final String CMD_ADD_WHITE_LIST = CMD + "add_white";
	private static final String CMD_REMOVE_WHITE_LIST = CMD + "remove_white";
	private static final String CMD_CLEAR_WHITE_LIST = CMD + "clear_white";

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

	/**
	 * 发送语音验证码
	 * 
	 * @param phone
	 *            电话
	 * @return
	 */
	@RequestMapping(CMD_SEND_VOICE_CODE)
	public ServerResult sendVoiceCode(@Param("phone") String phone) {
		return smsVerifyService.sendVoiceCode(phone);
	}

	@RequestMapping(CMD_WHITE_LIST)
	public ServerResult loadWhiteList(@Param("pwd") String password) {
		return smsVerifyService.loadWhiteList(password);
	}

	@RequestMapping(CMD_ADD_WHITE_LIST)
	public ServerResult addWhiteList(@Param("phone") String phone, @Param("pwd") String password) {
		return smsVerifyService.addWhiteList(phone, password);
	}

	@RequestMapping(CMD_REMOVE_WHITE_LIST)
	public ServerResult removeWhiteList(@Param("phone") String phone) {
		return smsVerifyService.removeWhiteList(phone);
	}

	@RequestMapping(CMD_CLEAR_WHITE_LIST)
	public ServerResult clearWhiteList() {
		return smsVerifyService.clearWhiteList();
	}

}
