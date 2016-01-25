package com.jiuyi.doctor.test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMain {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// String params = String.format("#name#=%s&#reason#=%s", "好的", "\t1.那啥啊；\n\t2.ahdfkdafd；\n\t3.ahdfkdafd");
		// SmsService.instance().sendSms("18223506390", "7783", params);

		Object obj = new Object();

		synchronized (obj) {
			System.out.println("lock one!!");
			synchronized (obj) {
				System.out.println("lock two!");
			}
		}
	}

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> res = new ArrayList<>();
		res.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class<?> superClass = clazz.getSuperclass();
		while (superClass != null) {
			res.addAll(Arrays.asList(superClass.getDeclaredFields()));
			superClass = superClass.getSuperclass();
		}
		return res;
	}

}
