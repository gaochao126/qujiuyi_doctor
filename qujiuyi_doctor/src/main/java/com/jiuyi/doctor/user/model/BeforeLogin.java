package com.jiuyi.doctor.user.model;

/**
 * @Author: xutaoyang @Date: 上午9:56:05
 *
 * @Description 保存在登录之前的一些数据，比如salt
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class BeforeLogin {

	private String salt;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean hasSalt() {
		return this.salt != null;
	}

	public String resetSalt() {
		String res = this.salt;
		this.salt = null;
		return res;
	}

}
