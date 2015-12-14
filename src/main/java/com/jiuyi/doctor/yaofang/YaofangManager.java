package com.jiuyi.doctor.yaofang;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YaofangManager {

	@SuppressWarnings("unused")
	private @Autowired YaofangDao dao;

	@PostConstruct
	public void init() {
	}

}
