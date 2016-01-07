/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jiuyi.doctor.call;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyi.doctor.call.model.CallAuthen;
import com.jiuyi.doctor.call.model.CallEstablish;
import com.jiuyi.doctor.call.model.CallHangup;

/**
 * <p>
 * Title: Controller
 * </p>
 * <p>
 * Description: 鉴权控制器
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: hisunsray
 * </p>
 * <p>
 * Date: 2013-07-09
 * </p>
 * 
 * @version 1.0
 */
@Controller
public class AuthController {

	private static final Logger log = Logger.getLogger(AuthController.class.getName());

	@RequestMapping("callauth")
	public void doAuth(HttpServletRequest request, HttpServletResponse response) {
		Document doc = null;
		String body = "";
		log.info(" =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= starts =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= ");
		try {
			// 解析流，打印log
			InputStream in = request.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String str = null;
			StringBuffer xmlfile = new StringBuffer();
			while ((str = bf.readLine()) != null) {
				xmlfile.append(str);
			}
			log.info(" --- xml body --- :" + xmlfile);
			doc = DocumentHelper.parseText(xmlfile.toString());
		} catch (DocumentException e) {
			log.error(" *** DocumentException ***", e);
		} catch (IOException e1) {
			log.error(" *** IOException ***", e1);
		}
		Element root = doc.getRootElement();
		String action = root.elementTextTrim("action");
		if (action.equals("CallAuth")) {
			// 解析呼叫鉴权
			body = parseCallAuth(root);
		} else if (action.equals("CallEstablish")) {
			// 解析摘机请求
			body = parseCallEstablish(root);
		} else if (action.equals("Hangup")) {
			// 解析挂断请求
			body = parseHangup(root);
		}
		// 设置返回header
		response.setHeader("Status-Code", "HTTP/1.1 200 OK");
		response.setHeader("Date", new Date() + "");
		response.setHeader("Content-Length", body.length() + "");
		try {
			// 输出 ，返回到客户端
			OutputStream opt = response.getOutputStream();
			OutputStreamWriter out = new OutputStreamWriter(opt);
			out.write(body);
			out.flush();
		} catch (IOException e) {
			log.error(" *** IOException ***", e);
		}
		log.info("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= end =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\r\n");
	}

	/**
	 * 解析呼叫鉴权
	 * 
	 * @param e
	 *            Element
	 * @return result
	 */
	private String parseCallAuth(Element e) {
		log.info("--- parseCallAuth   start ---");

		CallAuthen call = new CallAuthen();
		call.setType(e.elementTextTrim("type"));
		call.setOrderId(e.elementTextTrim("orderid"));
		call.setSubId(e.elementTextTrim("subid"));
		call.setCaller(e.elementTextTrim("caller"));
		call.setCalled(e.elementTextTrim("called"));
		call.setCallSid(e.elementTextTrim("callSid"));
		log.info(" --- parseCallAuth --- :" + call.toString());
		// 请在此处增加逻辑判断代码

		// 返回的数据,如果需要控制呼叫时长需要增加sessiontime
		String result = "<?xml version='1.0' encoding='UTF-8' ?><Response><statuscode>0000</statuscode><statusmsg>Success</statusmsg><record>1</record></Response>";

		return result;
	}

	/**
	 * 解析摘机请求
	 * 
	 * @param e
	 *            Element
	 * @return result
	 */
	private String parseCallEstablish(Element e) {
		log.info("--- parseCallEstablish   start   ");

		CallEstablish call = new CallEstablish();
		call.setType(e.elementTextTrim("type"));
		call.setOrderId(e.elementTextTrim("orderid"));
		call.setSubId(e.elementTextTrim("subid"));
		call.setCaller(e.elementTextTrim("caller"));
		call.setCalled(e.elementTextTrim("called"));
		call.setCallSid(e.elementTextTrim("callSid"));
		log.info(" --- CallEstablish --- : " + call.toString());
		// 请在此处增加逻辑判断代码

		// 返回的数据,如果需要控制呼叫时长需要增加sessiontime
		String result = "<?xml version='1.0' encoding='UTF-8' ?><Response><statuscode>0000</statuscode><statusmsg>Success</statusmsg><billdata>ok</billdata></Response>";

		log.info("--- parseCallEstablish   end ---");

		return result;
	}

	/**
	 * 解析挂断请求
	 * 
	 * @param e
	 *            Element
	 * @return result
	 */
	private String parseHangup(Element e) {
		log.info("---parseHangup   start---");
		// 封装 CallHangup
		CallHangup call = new CallHangup();
		call.setType(e.elementTextTrim("type"));
		call.setOrderId(e.elementTextTrim("orderid"));
		call.setSubId(e.elementTextTrim("subid"));
		call.setCaller(e.elementTextTrim("caller"));
		call.setCalled(e.elementTextTrim("called"));
		call.setByeType(e.elementTextTrim("byeType"));
		call.setStarttime(e.elementTextTrim("starttime"));
		call.setEndtime(e.elementTextTrim("endtime"));
		call.setBilldata(e.elementTextTrim("billdata"));
		call.setCallSid(e.elementTextTrim("callSid"));
		call.setRecordurl(e.elementTextTrim("recordurl"));

		log.info(" --- CallHangup --- : " + call.toString());
		// 请在此处增加逻辑判断代码

		// 返回的数据
		String result = "<?xml version='1.0' encoding='UTF-8'?><Response><statuscode>0000</statuscode><statusmsg>Success</statusmsg><totalfee>0.120000</totalfee></Response>";

		return result;
	}
}
