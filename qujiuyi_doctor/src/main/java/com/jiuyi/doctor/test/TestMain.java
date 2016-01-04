package com.jiuyi.doctor.test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestMain {

	public static void main(String[] args) throws UnsupportedEncodingException {
		List<Integer> list = Arrays.asList(1, 1, 2, 3, 43, 4, 5, 5);
		Collections.shuffle(list);
		System.err.println(list.subList(0, 5));
		int i = 0 + 1;
		String res = 1 == i ? "test" : "1";
		System.err.println(res);
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
