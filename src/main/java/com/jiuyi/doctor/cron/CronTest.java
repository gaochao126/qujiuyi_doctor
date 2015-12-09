package com.jiuyi.doctor.cron;

import org.springframework.stereotype.Component;

@Component
public class CronTest {

	// @Scheduled(cron = CronTime.TEST)
	public void runJob() {
		System.err.println(System.currentTimeMillis());
	}

}
