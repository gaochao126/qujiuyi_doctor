package com.jiuyi.doctor.app;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.Loggers;

/**
 * @Author: xutaoyang @Date: 下午7:36:19
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class AppController extends ControllerBase {

	private static final String CMD = "app_";
	private static final String CMD_IOS = CMD + "ios";
	private static final String CMD_ANDROID = CMD + "android_version";
	private static final String CMD_IOS_VERSION = CMD + "ios_version";
	private static final String CMD_KEFU_PHONE = "app_kefu_phone";

	@Autowired
	private DBConfig dbConfig;

	@RequestMapping(CMD_IOS)
	public void appIos(HttpServletResponse resp) {
		try {
			String app_ios_url = dbConfig.getConfig("doctor.app.ios.url");
			resp.sendRedirect(app_ios_url);
		} catch (IOException e) {
			Loggers.err("redirect to download ios err", e);
		}
	}

	@RequestMapping(CMD_ANDROID)
	@ResponseBody
	public ServerResult appAndroid(HttpServletResponse resp) {
		ServerResult res = new ServerResult();
		res.put("url", dbConfig.getConfigFromDB("doctor.app.android.download.url"));
		res.put("version", dbConfig.getConfigFromDB("doctor.app.android.version"));
		res.put("forceUpdate", dbConfig.getConfigFromDB("doctor.app.android.forceUpdate"));
		return res;
	}

	@RequestMapping(CMD_IOS_VERSION)
	@ResponseBody
	public ServerResult appIosVersion(HttpServletResponse resp) {
		ServerResult res = new ServerResult();
		res.put("version", dbConfig.getConfigFromDB("doctor.app.ios.version"));
		res.put("forceUpdate", dbConfig.getConfigFromDB("doctor.app.ios.forceUpdate"));
		return res;
	}

	@RequestMapping(CMD_KEFU_PHONE)
	@ResponseBody
	public ServerResult kefuPhone() {
		ServerResult res = new ServerResult();
		res.put("kefuPhone", dbConfig.getConfigFromDB("kefu.phone"));
		return res;
	}

}
