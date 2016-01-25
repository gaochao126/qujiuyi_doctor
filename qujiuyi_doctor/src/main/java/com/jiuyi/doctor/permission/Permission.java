package com.jiuyi.doctor.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyi.doctor.user.model.DoctorStatus;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
	DoctorStatus value() default DoctorStatus.NORMAL;
}
