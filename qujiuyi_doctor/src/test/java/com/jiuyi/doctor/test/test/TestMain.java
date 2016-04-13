package com.jiuyi.doctor.test.test;

import java.lang.reflect.InvocationTargetException;

public class TestMain {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		new TestMain().test();
	}

	void test() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ServiceClass clazz = ServiceClass.class.newInstance();

		String res = clazz.handler();

		System.out.println(res);
	}

	String method() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return "main method";
	}
}
