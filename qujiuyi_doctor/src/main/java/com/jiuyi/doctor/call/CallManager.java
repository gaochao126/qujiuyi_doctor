/**
 * 
 */
package com.jiuyi.doctor.call;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.jiuyi.frame.conf.DBConfigStatic;
import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;

/**
 * @author xutaoyang
 *
 */
@Service
public class CallManager {

	private CCPRestSDK restAPI;

	@PostConstruct
	public void init() {
		restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		String account = DBConfigStatic.getConfig("doctor.call.account");
		String password = DBConfigStatic.getConfig("doctor.call.password");
		String appId = DBConfigStatic.getConfig("doctor.call.appid");
		restAPI.setAppId(appId);// 初始化应用ID
		restAPI.setSubAccount(account, password);// 初始化子帐号和子帐号TOKEN
	}

	/**
	 * 建立一个呼叫回拨
	 * 
	 * @param caller
	 *            发起者
	 * @param called
	 *            接受者
	 * @return
	 */
	public ServerResult makeCall(String caller, String called) {
		/*
		 * "主叫号码", "被叫号码", "被叫侧显示的号码", "主叫侧显示的号码", "xx.wav(可选第三方自定义回拨提示音)", "是否一直播放提示音", "用于终止播放提示音的按键", "第三方私有数据",
		 * "最大通话时长", "实时话单通知地址", "是否给主被叫发送话单", "是否录音", "通话倒计时", "到达倒计时时间播放的提示音"
		 */
		HashMap<String, Object> result = restAPI.callback("18223506390", "18100863330", "4009191791", "4009191791", null, null, null, null, null, null, null, null, null, null);
		Loggers.info("SDKTestCallback result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			@SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				Loggers.info(key + " = " + object);
			}
			return new ServerResult();
		} else {
			// 异常返回输出错误码和错误信息
			Loggers.info("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
			return new FailResult("电话线路正忙，请稍后重试");
		}
	}

	
}
