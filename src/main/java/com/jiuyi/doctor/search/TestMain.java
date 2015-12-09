package com.jiuyi.doctor.search;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午7:10:17
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class TestMain {
	static Class<Test> clazz = Test.class;
	static Field[] fields = clazz.getDeclaredFields();

	public static void main(String[] args) {
		for (Field field : fields) {
			field.setAccessible(true);
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Map<String, Object> key_value = new HashMap<String, Object>();
			key_value.put("param", StringUtil.getRandomStr(10));
			key_value.put("param1", StringUtil.getRandomStr(10));
			key_value.put("param2", StringUtil.getRandomStr(10));
			key_value.put("param3", StringUtil.getRandomStr(10));
			key_value.put("param4", StringUtil.getRandomStr(10));
			Test test = instantObj(key_value);
			test.getParam();
			// Test test = byGson(key_value);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	static Gson gson = new Gson();

	static Test byGson(Map<String, Object> key_value) {
		Test test = gson.fromJson(key_value.toString(), Test.class);
		return test;
	}

	static Test instantObj(Map<String, Object> key_value) {
		Test test = null;
		try {
			test = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
		}
		if (test == null) {
			System.out.println("obj test is null");
			return null;
		}
		for (Field field : fields) {
			try {
				field.set(test, key_value.get(field.getName()));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return test;
	}
}
