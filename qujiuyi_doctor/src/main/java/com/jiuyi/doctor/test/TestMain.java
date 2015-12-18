package com.jiuyi.doctor.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMain {

	public static void main(String[] args) {
		Object[] arr = Arrays.asList("fds").toArray(new String[1]);
		System.out.println(arr[0]);
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
