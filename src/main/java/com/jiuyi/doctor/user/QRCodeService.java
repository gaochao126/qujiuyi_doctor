package com.jiuyi.doctor.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.chatserver.ChatServerRequestEntity;
import com.jiuyi.doctor.chatserver.ChatServerResponse;
import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.StringUtil;

@Service
public class QRCodeService {

	private ExecutorService executor;

	@Autowired
	private UserDAO dao;

	@Autowired
	private ChatServerService chatServerService;

	@PostConstruct
	public void init() {
		this.executor = Executors.newSingleThreadExecutor();
	}

	@PreDestroy
	public void destroy() {
		this.executor.shutdown();
	}

	/** 获取医生的二维码，这个二维码由微信生成，聊天服务器提供了接口，这里从聊天服务器获取二维码 */
	public void tryGenQRCode(Doctor doctor) {
		if (StringUtil.isNullOrEmpty(doctor.getQrCodeImg())) {
			executor.submit(new QRCodeRunnable(doctor));
		}
	}

	private class QRCodeRunnable implements Runnable {

		private Doctor doctor;

		public QRCodeRunnable(Doctor doctor) {
			this.doctor = doctor;
		}

		@Override
		public void run() {
			ChatServerRequestEntity request = new ChatServerRequestEntity("getTwoDimensionCodeByServer");
			Map<String, Object> params = new HashMap<>();
			params.put("id", String.valueOf(doctor.getId()));
			params.put("secret", "!@#$%^&*()");
			request.putParam("params", params);
			ChatServerResponse response = chatServerService.postMsgAsyn(request);
			if (response != null && response.isSuccess()) {
				String qrCode = response.getField("twoDimensionCode");
				Loggers.debug(qrCode);
				doctor.setQrCodeImg(qrCode);
				dao.updateQRCodeImg(doctor, qrCode);
			}
		}

	}

}
