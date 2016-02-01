package com.jiuyi.doctor.smsverify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.qujiuyi.util.commonres.CommonResult;
import com.qujiuyi.util.sms.SmsService;

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

	private @Autowired SmsDao dao;

	@PostConstruct
	public void init() {
		List<String> whiteList = dao.selectAll();
		test_phone.addAll(whiteList);
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
		CommonResult resp = SmsService.instance().sendCode(phone);
		return new ServerResult(resp.getResultCode(), resp.getResultDesc());
	}

	/**
	 * @param phone
	 * @return
	 */
	protected ServerResult sendVoiceCode(String phone) {
		if (inWhiteList(phone)) {
			return new ServerResult();
		}
		CommonResult resp = SmsService.instance().sendVoiceCode(phone);
		return new ServerResult(resp.getResultCode(), resp.getResultDesc());
	}

	/** 电话验证码check */
	public ServerResult checkCode(String phone, String code) {
		if (inWhiteList(phone)) {
			return new ServerResult();
		}
		CommonResult res = SmsService.instance().checkCode(phone, code);
		if (res == null) {
			return new ServerResult(ResultConst.SERVER_ERR);
		}
		return new ServerResult(res.getResultCode(), res.getResultDesc());
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
		boolean added = this.test_phone.add(phone);
		if (added) {
			dao.insert(phone);
		}
		return new ServerResult();
	}

	public ServerResult removeWhiteList(String phone) {
		this.test_phone.remove(phone);
		dao.delete(phone);
		return new ServerResult();
	}

	public ServerResult clearWhiteList() {
		this.test_phone.clear();
		dao.clear();
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
