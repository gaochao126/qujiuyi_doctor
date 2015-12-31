/**
 * 
 */
package com.jiuyi.doctor.user.model;

/**
 * 
 * @author xutaoyang
 *
 */
public class ExpiredToken {

	private String token;
	private long expireTime;

	public ExpiredToken(String token) {
		this.token = token;
		this.expireTime = System.currentTimeMillis();
	}

	/**
	 * 过了特定的时间再移除token
	 * 
	 * @param now
	 * @param removeTime
	 * @return
	 */
	public boolean needRemove(long now, int removeTime) {
		return now - expireTime >= removeTime;
	}

	public String getToken() {
		return token;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
