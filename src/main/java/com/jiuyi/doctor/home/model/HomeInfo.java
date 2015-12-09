package com.jiuyi.doctor.home.model;

import java.util.Date;

/**
 * @Author: xutaoyang @Date: 上午10:48:36
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class HomeInfo {

	public Date lastGetUnreadEvaTime;// 上一次获取未读评价的事件

	public HomeInfo(Date lastGetUnreadEvaTime) {
		this.lastGetUnreadEvaTime = lastGetUnreadEvaTime;
	}

}
