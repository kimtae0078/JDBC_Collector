package com.takim.siem.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.takim.siem.jdbc.MariaUtil;
import com.takim.siem.jdbc.MssqlUtil;
import com.takim.siem.jdbc.MysqlUtil;
import com.takim.siem.jdbc.OracleUtil;
import com.takim.siem.jdbc.PostgreUtil;
import com.takim.siem.model.JdbcInfo;
import com.takim.siem.model.KeyInfo;
import com.takim.siem.repository.Repository;
import com.takim.siem.util.CommonUtil;

public class DbJob implements org.quartz.Job {
	private Repository repository = new Repository();
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("잡 생성 시간:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
		JobDataMap dataMap = context.getMergedJobDataMap();
		JdbcInfo jdbcVO = (JdbcInfo) dataMap.get("jdbcVO");
		String dbms = jdbcVO.getDbms();
		
		KeyInfo keyVO = repository.selectKeyInfo(jdbcVO.getIdx());
		String sql = CommonUtil.makeSQL(jdbcVO, keyVO);
		
		doExecute(jdbcVO, dbms, keyVO, sql);
	}

	private void doExecute(JdbcInfo jdbcVO, String dbms, KeyInfo keyVO, String sql) {
		switch (dbms) {
		case "maria":
			MariaUtil.selectAll(jdbcVO, keyVO, sql);
			break;
		case "mssql":
			MssqlUtil.selectAll(jdbcVO, keyVO, sql);
			break;
		case "mysql":
			MysqlUtil.selectAll(jdbcVO, keyVO, sql);
			break;
		case "oracle":
			OracleUtil.selectAll(jdbcVO, keyVO, sql);
			break;
		case "postgre":
			PostgreUtil.selectAll(jdbcVO, keyVO, sql);
			break;
		default:
			break;
		}
		// 마지막 오프셋 수정
		repository.updateLastKeyValue(keyVO);
	}

}
