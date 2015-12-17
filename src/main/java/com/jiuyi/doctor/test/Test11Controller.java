package com.jiuyi.doctor.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

@RestController
public class Test11Controller {

	@RequestMapping("test_handler")
	public ServerResult testReturnValHandler(@Param("param") String param) {
		int id = 145;
		id += 1;
		System.out.println(id);

		while (id < 200) {
			System.out.println(id);
			id++;
		}

		return new ServerResult("param", param);
	}

	@RequestMapping("test_handler1")
	public ServerResult testReturnValHandler1() {
		return new ServerResult();
	}

}
