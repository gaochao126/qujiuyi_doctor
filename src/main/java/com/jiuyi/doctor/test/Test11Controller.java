package com.jiuyi.doctor.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.front.ServerResult;

@RestController
public class Test11Controller {

	@RequestMapping("test_handler")
	public ServerResult testReturnValHandler() {
		return new ServerResult();
	}

	@RequestMapping("test_handler1")
	public ServerResult testReturnValHandler1() {
		return new ServerResult();
	}

}
