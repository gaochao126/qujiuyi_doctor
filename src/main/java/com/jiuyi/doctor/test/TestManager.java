package com.jiuyi.doctor.test;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.frame.front.ServerResult;

@Service
public class TestManager {

	private @Autowired TestDao dao;

	@PostConstruct
	public void init() {
	}

	public ServerResult testApi() {
		return new ServerResult();
	}

	public Patient loadPatient(String id) {
		return dao.loadPatient(id);
	}
}
