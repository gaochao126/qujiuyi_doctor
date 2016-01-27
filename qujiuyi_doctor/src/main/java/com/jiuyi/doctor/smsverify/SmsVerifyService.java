package com.jiuyi.doctor.smsverify;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.sms.SmsCheckService.CheckResult;
import com.jiuyi.frame.sms.SmsResp;
import com.jiuyi.frame.sms.SmsService;

/**
 * 服务端发起验证请求验证移动端(手机)发送的短信
 * 
 * @author Administrator
 *
 */
@Service
public class SmsVerifyService {

	private static final String ADMIN_PASSWORD = "jiuyi791";

	/** 仅供测试使用 */
	private Set<String> test_phone = new HashSet<>();

	public static void main(String[] args) {
		SmsResp res = SmsService.instance().sendCode("13983240200");
		System.err.println(res.getReason());

	}

	@PostConstruct
	public void init() {
		test_phone.add("15923330708");
		test_phone.add("13638357922");
		test_phone.add("15025467312");

		test_phone.add("18983635464");
		test_phone.add("13635335741");
		test_phone.add("13635335742");
		test_phone.add("18520807540");
		test_phone.add("17784765060");
		test_phone.add("18523419004");
		test_phone.add("15812098703");
		test_phone.add("18983472571");
		test_phone.add("15823566822");
		test_phone.add("18223506390");
		test_phone.add("18725850672");
		test_phone.add("18100863325");
		test_phone.add("18100863327");
		test_phone.add("18100863330");
		test_phone.add("13370756360");
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 *            电话
	 * @return
	 */
	protected ServerResult sendCode(String phone) {
		if (inWhiteList(phone)) {
			return new ServerResult();
		}
		SmsResp resp = SmsService.instance().sendCode(phone);
		return new ServerResult(resp.getError_code(), resp.getReason());
	}

	/**
	 * @param phone
	 * @return
	 */
	protected ServerResult sendVoiceCode(String phone) {
		if (inWhiteList(phone)) {
			return new ServerResult();
		}
		SmsResp resp = SmsService.instance().sendVoiceCode(phone);
		return new ServerResult(resp.getError_code(), resp.getReason());
	}

	/** 电话验证码check */
	public ServerResult checkCode(String phone, String code) {
		if (inWhiteList(phone)) {
			return new ServerResult();
		}
		CheckResult res = SmsService.instance().checkCode(phone, code);
		if (res == null) {
			return new ServerResult(ResultConst.SERVER_ERR);
		}
		return new ServerResult(res.getR(), res.getDesc());
	}

	private boolean inWhiteList(String phone) {
		return test_phone.contains(phone);
	}

	/**
	 * @param phone
	 * @param password
	 * @return
	 */
	public ServerResult addWhiteList(String phone, String password) {
		if (!password.equals(ADMIN_PASSWORD)) {
			return new ServerResult();
		}
		this.test_phone.add(phone);
		return new ServerResult();
	}

	public ServerResult removeWhiteList(String phone) {
		this.test_phone.remove(phone);
		return new ServerResult();
	}

	public ServerResult clearWhiteList() {
		this.test_phone.clear();
		return new ServerResult();
	}

	/**
	 * @param password
	 * @return
	 */
	protected ServerResult loadWhiteList(String password) {
		if (!password.equals(ADMIN_PASSWORD)) {
			return new ServerResult();
		}
		ServerResult res = new ServerResult();
		res.put("list", this.test_phone);
		return res;
	}

}
