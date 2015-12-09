package com.jiuyi.doctor.search;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午11:22:19
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class TestController {

	@RequestMapping("test")
	@ResponseBody
	public ServerResult loadInfo(@TokenUser Doctor doctor) {
		ServerResult res = new ServerResult();
		res.put("id", doctor.getId());
		return res;
	}

	@RequestMapping("test1")
	@ResponseBody
	public ServerResult test1(@TokenUser Doctor doctor, @Param("param") String param) {
		ServerResult res = new ServerResult();
		res.put("id", doctor.getId());
		res.put("param", param);
		return res;
	}

	@RequestMapping("test2")
	@ResponseBody
	public ServerResult test2(@TokenUser Doctor doctor, @Param("test") Test test) {
		ServerResult res = new ServerResult();
		res.put("id", doctor.getId());
		res.put("param", test.getParam());
		return res;
	}

	@RequestMapping("test3")
	@ResponseBody
	public ServerResult test3(@TokenUser Doctor doctor, @Param("test") Test test) {
		ServerResult res = new ServerResult();
		res.put("id", doctor.getId());
		res.put("param", test.getTest().getParam());
		return res;
	}
}
