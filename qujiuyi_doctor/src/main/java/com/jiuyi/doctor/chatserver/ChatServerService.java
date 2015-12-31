package com.jiuyi.doctor.chatserver;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.httpclient.AsynHttpClientService;
import com.jiuyi.frame.httpclient.SimpleHttpClientService;
import com.jiuyi.frame.httpclient.AbsHttpClient.IRequestEntity;
import com.jiuyi.frame.util.JsonUtil;

/**
 * @Author: xutaoyang @Date: 上午9:32:53
 *
 * @Description 与聊天服务器通信
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ChatServerService {

	private String CHAT_SERVER_URL;
	private String ONLINE = "0";
	private String OFFLINE = "1";
	private String cmd = "syncUserInfoToServer";
	private String userType = "1";// 医生
	private AsynHttpClientService httpClient;

	private SimpleHttpClientService simpleHttpClientService;

	@Autowired
	DBConfig dbConfig;

	@PostConstruct
	public void init() {
		this.CHAT_SERVER_URL = dbConfig.getConfig("doctor.chatServerUrl");
		this.httpClient = new AsynHttpClientService(CHAT_SERVER_URL);
		this.simpleHttpClientService = new SimpleHttpClientService(CHAT_SERVER_URL);
	}

	@PreDestroy
	public void destroy() {
		this.httpClient.shutdown();
	}

	/** 登录信息同步到聊天服务器，通知该用户上线 */
	public void onLogin(Doctor doctor) {
		LoginRequest request = new LoginRequest(cmd, String.valueOf(doctor.getId()), doctor.getAccess_token(), userType, doctor.getDeviceType(), ONLINE);
		postMsg(request);
	}

	/** 把登出信息同步到聊天服务器 */
	public void onLogout(Doctor doctor) {
		LoginRequest request = new LoginRequest(cmd, String.valueOf(doctor.getId()), doctor.getAccess_token(), userType, doctor.getDeviceType(), OFFLINE);
		postMsg(request);
	}

	/** 异步post信息到聊天服务器 */
	public void postMsg(IRequestEntity request) {
		Loggers.debug("to chat server post:" + request);
		httpClient.post(request);
	}

	/** 同步post信息到聊天服务器,并解析返回值 */
	public ChatServerResponse postMsgAsyn(IRequestEntity request) {
		String json = simpleHttpClientService.postWithRes(request);
		return JsonUtil.fromJson(json, ChatServerResponse.class);
	}

	protected class LoginRequest implements IRequestEntity {
		String cmd;
		Map<String, Object> params;

		public LoginRequest(String cmd, String userId, String token, String userType, Integer deviceType, String online) {
			this.cmd = cmd;
			this.params = new HashMap<>();
			this.params.put("userType", userType);
			this.params.put("userId", userId);
			this.params.put("online", online);
			this.params.put("token", token);
			this.params.put("deviceType", deviceType);
		}

		public Map<String, ? extends Object> genEntity() {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("cmd", cmd);
			result.put("params", params);
			return result;
		}
	}

}
