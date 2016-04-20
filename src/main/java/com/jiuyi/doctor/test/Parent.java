package com.jiuyi.doctor.test;

import org.hibernate.validator.constraints.NotEmpty;

public class Parent {

	@NotEmpty
	protected String name;

	public Parent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
