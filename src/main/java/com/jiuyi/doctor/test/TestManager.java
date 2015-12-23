package com.jiuyi.doctor.test;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ServerResult;

@Service
public class TestManager {

	@PostConstruct
	public void init() {
	}

	public ServerResult testApi() {
		return new ServerResult();
	}

}
