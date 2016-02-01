/**
 * 
 */
package com.jiuyi.doctor.frontlog;

import java.util.Date;

/**
 * 
 * @author xutaoyang
 *
 */
public class FrontLog {

	private long id;
	private int appType;
	private String appVersion;
	private int deviceType;
	private String deviceId;
	private String os;
	private int level;
	private String log;
	private int userId;
	private Date date;

	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	public long getId() {
		return id;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getOs() {
		return os;
	}

	public int getLevel() {
		return level;
	}

	public String getLog() {
		return log;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
