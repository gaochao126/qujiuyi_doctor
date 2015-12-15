package com.jiuyi.doctor.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jiuyi.doctor.prescription.Prescription;
import com.jiuyi.frame.util.ObjectUtil;
import com.jiuyi.frame.util.ValidateResult;

public class TestMain {

	public static void main(String[] args) {
		Prescription prescription = new Prescription();
		ValidateResult res = ObjectUtil.validateRes(prescription);
		System.out.println(res.isSuccess() + "-->" + res.getMsg());
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
