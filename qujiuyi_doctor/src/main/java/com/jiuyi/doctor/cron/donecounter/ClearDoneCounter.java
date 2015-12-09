package com.jiuyi.doctor.cron.donecounter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.cron.CronTime;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午6:53:57
 *
 * @Description 清理计数器
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class ClearDoneCounter extends DbBase {

	@Scheduled(cron = CronTime.CLEAR_DONE_COUNTER_DAILY)
	public void clearDaily() {
		jdbc.update("truncate `t_doctor_counter_daily`");
	}

	@Scheduled(cron = CronTime.CLEAR_DONE_COUNTER_WEEKLY)
	public void clearWeekly() {
		jdbc.update("truncate `t_doctor_counter_weekly`");
	}

	@Scheduled(cron = CronTime.CLEAR_DONE_COUNTER_MONTH)
	public void clearMonth() {
		jdbc.update("truncate `t_doctor_counter_month`");
	}
}
