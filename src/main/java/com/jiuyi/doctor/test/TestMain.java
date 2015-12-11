package com.jiuyi.doctor.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jiuyi.doctor.patients.v2.model.Patient;

public class TestMain {

	public static void main(String[] args) {
		List<Field> fields = getAllFields(Patient.class);
		for (Field field : fields) {
			System.out.println(field.getName());
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
