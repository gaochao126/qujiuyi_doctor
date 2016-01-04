package com.jiuyi.doctor.cron;

/**
 * @Author: xutaoyang @Date: 下午5:09:28
 *
 * @Description 各个cron的时间配置
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class CronTime {
	/**
	 * 表达式格式：sec minute hour day month dayOfWeek
	 */

	/** 医生推荐指数，每晚12点 */
	public static final String DOCTOR_RECOMMEND_SCORE = "0 0 0 * * ?";

	/** 每5分钟跑一次，图文咨询结束后指定时间，把医生的即将到账转为余额 */
	public static final String CONSULT_INCOMING_TO_BALANCE = "0 0/5 * * * ?";

	/** 每5分钟跑一次，图文咨询,医生如果没有反应，则让咨询失效 */
	public static final String CHECK_CONSULT = "0 0/5 * * * ?";

	/** 每1小时跑一次，私人医生服务结束后指定时间，把医生的即将到账转为余额 */
	public static final String PERSONAL_DOCTOR_INCOMING_TO_BALANCE = "0 0 0/1 * * ?";

	/** 每天00:00 清理计数器 */
	public static final String CLEAR_DONE_COUNTER_DAILY = "0 0 0 * * ?";

	/** 每周一00:00 清理计数器 */
	public static final String CLEAR_DONE_COUNTER_WEEKLY = "0 0 0 ? * 2";

	/** 每月1日 00:00 清理计数器 */
	public static final String CLEAR_DONE_COUNTER_MONTH = "0 0 0 1 * ?";

	/** 每5分钟跑一次，处方在创建48小时后，只要处于未支付状态，则标记为过期 */
	public static final String EXPIRE_PRESCRIPTION = "0 0/5 * * * ?";

	/** 测试,每秒 */
	public static final String TEST = "0/1 * * * * ?";

}
