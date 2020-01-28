package com.takim.siem.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.takim.siem.jdbc.MariaUtil;
import com.takim.siem.jdbc.MssqlUtil;
import com.takim.siem.jdbc.MysqlUtil;
import com.takim.siem.jdbc.OracleUtil;
import com.takim.siem.jdbc.PostgreUtil;
import com.takim.siem.kafka.KafkaProducerUtil;
import com.takim.siem.model.JdbcInfo;
import com.takim.siem.repository.Repository;
import com.takim.siem.scheduler.AutoScheduler;
import com.takim.siem.util.CommonUtil;
import com.takim.siem.util.IPTraceUtil;

/**
 * Application이 실행 REST API
 * 
 * @file JdbcRestController.java
 * @brief RestAPI 요청 확인
 * @version 1.0
 * @date 2019. 07. 03
 * @author
 */
@RestController
@WebListener
public class JdbcRestController implements javax.servlet.ServletContextListener {

	private final static Logger logger = LoggerFactory.getLogger(JdbcRestController.class);

	@Override
	public final void contextInitialized(final ServletContextEvent sce) {
		logger.info("메인 스케줄러 실행");
		// Client IP
		IPTraceUtil.getClientIP();
		
		// kafka producer 생성
		KafkaProducerUtil.createProducer();
		
		AutoScheduler trigger = new AutoScheduler();
		List<JdbcInfo> jdbcVoList = new ArrayList<JdbcInfo>();
		try {
		  	jdbcVoList = Repository.selectIsCollectTrue();
			for (int i = 0; i < jdbcVoList.size(); i++) {
				trigger.schedule(jdbcVoList.get(i));
			}
		} catch (ParseException e) {
			logger.error("메인 스케줄러 스케줄러 실행 에러");
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void addScheduler(@RequestBody String jdbcJson) {
		logger.info("스케줄러 추가");
		JdbcInfo jdbcVO = CommonUtil.jdbcInfoParsing(jdbcJson);
		try {
			AutoScheduler trigger = new AutoScheduler();
			trigger.schedule(jdbcVO);
		} catch (ParseException e) {
			logger.error("스케줄러 추가 등록 에러");
		}

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteScheduler(@RequestBody String idx) {
		logger.info("스케줄러 완전 삭제");
		unScheduler(idx);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void updateScheduler(@RequestBody String jdbcJson) {
		logger.info("스케줄러 수정");
		JdbcInfo jdbcVO = CommonUtil.jdbcInfoParsing(jdbcJson);

		unScheduler(jdbcVO.getIdx());

		if (jdbcVO.isCollect() == true) {
			AutoScheduler trigger = new AutoScheduler();
			try {
				trigger.schedule(jdbcVO);
			} catch (ParseException e) {
				logger.error("스케줄러 수정 후 스케줄러 실행 에러");
			}
		}
	}

	@RequestMapping(value = "/validationCheck", method = RequestMethod.POST)
	public String validationCheck(@RequestBody String jdbcJson) {
		logger.info("테이블 검증");
		JdbcInfo jdbcVO = CommonUtil.jdbcInfoParsing(jdbcJson);

		String dbms = jdbcVO.getDbms();
		List<Map<String, Object>> resultList = null;

		switch (dbms) {
		case "maria":
			resultList = MariaUtil.selectTableValidation(jdbcVO);
			break;
		case "mssql":
			resultList = MssqlUtil.selectTableValidation(jdbcVO);
			break;
		case "mysql":
			resultList = MysqlUtil.selectTableValidation(jdbcVO);
			break;
		case "oracle":
			resultList = OracleUtil.selectTableValidation(jdbcVO);
			break;
		case "postgre":
			resultList = PostgreUtil.selectTableValidation(jdbcVO);
			break;
		default:
			break;
		}
		
		Gson gson = new Gson();
		String resultGson = gson.toJson(resultList);

		return resultGson;
	}

	public void unScheduler(String idx) {
		logger.info("인스턴스 스케줄러 삭제");
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			for (String group : scheduler.getTriggerGroupNames()) {
				for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(group))) {
					if (triggerKey.toString().contains(idx)) {
						scheduler.unscheduleJob(triggerKey);
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error("실행중인 스케줄러 중단 및 삭제 에러");
		}
	}

}
