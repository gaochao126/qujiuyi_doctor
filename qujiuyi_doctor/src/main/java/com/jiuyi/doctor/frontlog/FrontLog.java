/**
 * 
 */
package com.jiuyi.doctor.frontlog;

/**
 * 
 * @author xutaoyang
 *
 */
public class FrontLog {

	private long id;
	private int appVersion;
	private int deviceType;
	private String deviceId;
	private String os;
	private int level;
	private String log;

	public long getId() {
		return id;
	}

	public int getAppVersion() {
		return appVersion;
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

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
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

}
