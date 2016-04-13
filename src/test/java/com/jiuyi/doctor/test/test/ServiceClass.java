package com.jiuyi.doctor.test.test;

import java.lang.reflect.InvocationTargetException;

public class ServiceClass {

	String handler() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		TestMain obj = TestMain.class.newInstance();
		Object res = TestMain.class.getDeclaredMethod("method", (Class[]) null).invoke(obj, (Object[]) null);
		return (String) res;
	}

}
