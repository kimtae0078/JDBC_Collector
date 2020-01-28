package com.takim.siem.scheduler;

import java.text.ParseException;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.model.JdbcInfo;

public class AutoScheduler {
	private Scheduler scheduler;
	private final static Logger logger = LoggerFactory.getLogger(AutoScheduler.class);
	
	public void schedule(JdbcInfo jdbcVO) throws ParseException {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			System.out.println("스케줄러 생성 :"+scheduler);
			// 트리거 세팅
			SimpleTriggerImpl simpleTrigger = (SimpleTriggerImpl) TriggerBuilder.newTrigger()
					// trigger의 name과 group 지정
					.withIdentity(jdbcVO.getIdx(), Scheduler.DEFAULT_GROUP)
					.startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule()
					.withMisfireHandlingInstructionNowWithExistingCount()
					.withIntervalInMilliseconds(jdbcVO.getIntervalTime()) // 시간 주기 설정
					.withRepeatCount(SimpleTriggerImpl.REPEAT_INDEFINITELY)) // 무한한 반복
					.build();
			
			org.quartz.CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity(jdbcVO.getIdx(), Scheduler.DEFAULT_GROUP)
					.startNow()
					.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/1 ? * *"))
					.build();
			
 			// 잡 (할 일) 세팅
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("jdbcVO", jdbcVO);
			JobDetail DbJob = JobBuilder
					.newJob(DbJob.class)
					.setJobData(dataMap)
					.build();
			scheduler.scheduleJob(DbJob, cronTrigger);
			scheduler.start();
		} catch (Exception e) {
			logger.error("트리거 생성 에러");
		}
	}

}
