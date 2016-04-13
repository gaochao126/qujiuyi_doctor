package com.jiuyi.doctor.test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jiuyi.frame.util.StringUtil;

public class TestMain {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// String params = String.format("#name#=%s&#reason#=%s", "好的", "\t1.那啥啊；\n\t2.ahdfkdafd；\n\t3.ahdfkdafd");
		// SmsService.instance().sendSms("18223506390", "7783", params);

		// Object obj = new Object();
		//
		// synchronized (obj) {
		// System.out.println("lock one!!");
		// synchronized (obj) {
		// System.out.println("lock two!");
		// }
		// }

		// List<Integer> list = new ArrayList<>();
		// list.addAll(null);

		// SmsService.instance().sendSms("18223506390", "9822",
		// "#date#=12345678934567894356789043567890456789045678905678905467890456789&#address#=hesdahfsadhfjkasdfkj&#name#=fjkajfkdjskfjkasjfdkajdfkjaklsdfjklajdfskljaksdfj");

//		Date date = new Date(1450160461l);
//
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//
//		System.out.println(cal.get(Calendar.YEAR));
//
//		String head = "http://www.51791.com:51115/files/doctor/head/agPSYzyR1C-313.jpg";
//		System.out.println(head.substring(head.lastIndexOf("/") + 1));
		
		
		System.out.println(StringUtil.md5Str(StringUtil.md5Str(String.valueOf(246))));
		System.out.println(StringUtil.md5Str(String.valueOf(246)));
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
