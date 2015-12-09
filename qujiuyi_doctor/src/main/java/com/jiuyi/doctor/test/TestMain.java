package com.jiuyi.doctor.test;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.jiuyi.doctor.prescribe.model.Prescribe;

public class TestMain {

	public static void main(String[] args) {

		try {
			Prescribe p = Prescribe.class.newInstance();

			Object patientAge = 15;

			BeanUtils.setProperty(p, "patientAge", patientAge);
			
			System.out.println(p.getPatientAge());
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

}
