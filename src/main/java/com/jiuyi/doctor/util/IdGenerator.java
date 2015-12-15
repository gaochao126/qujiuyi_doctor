/**
 * 
 */
package com.jiuyi.doctor.util;

import java.util.UUID;

/**
 * id生成器
 * 
 * @author xutaoyang
 *
 */
public class IdGenerator {

	public static String genId() {
		return UUID.randomUUID().toString();

	}

}
