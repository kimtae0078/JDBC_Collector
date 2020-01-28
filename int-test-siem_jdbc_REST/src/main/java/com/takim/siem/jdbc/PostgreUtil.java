package com.takim.siem.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.constant.Constant;
import com.takim.siem.kafka.KafkaProducerUtil;
import com.takim.siem.model.JdbcInfo;
import com.takim.siem.model.KeyInfo;
import com.takim.siem.util.CommonUtil;

public class PostgreUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(PostgreUtil.class);

	public static void selectAll(JdbcInfo jdbcVO, KeyInfo keyVO, String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String url = "jdbc:postgresql://" + jdbcVO.getIp() + "/" + jdbcVO.getDatabaseName() + "";
		String id = jdbcVO.getUsername();
		String pw = jdbcVO.getPassword();

		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("postgrsql driver 연결 오류");
			}
			
			conn = DriverManager.getConnection(url, id, pw);
			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			int columnSize = CommonUtil.rsColumnCount(rs);

			while (rs.next()) {
				// log convert
				StringBuffer log = new StringBuffer();
				for (int i = 1; i <= columnSize; i++) {
					log.append(rs.getString(i));
					log.append(", ");
				}
				log = log.delete(log.length() - 2, log.length());
				
				String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
				// kafka producer
				Constant.MESSAGE = new ProducerRecord<String, String>(Constant.KAFKA_TOPIC, "JDBC",
						jdbcVO.getIp() + ",\t" + Constant.COLLECTOR_IP + ",\t" + now + ",\t" + jdbcVO.getIdx() + ",\t" + log);
				
				KafkaProducerUtil.sendMessage(Constant.MESSAGE);
			}
			
			CommonUtil.setLastKeyValue(rs, keyVO);
			
		} catch (SQLException e) {
			logger.error("postgre SQL 에러 ");
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("postgre connection close 에러 ");
			}
		}
	}

	public static List<Map<String, Object>> selectTableValidation(JdbcInfo jdbcVO) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String url = "jdbc:postgresql://" + jdbcVO.getIp() + "/" + jdbcVO.getDatabaseName() + "";
		String id = jdbcVO.getUsername();
		String pw = jdbcVO.getPassword();
		
		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("postgrsql driver 연결 오류");
			}
			
			conn = DriverManager.getConnection(url, id, pw);

			String sql = "SELECT * FROM " + jdbcVO.getTable() + " LIMIT 10";

			pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			int columnSize = CommonUtil.rsColumnCount(rs);
			ResultSetMetaData rsmd = rs.getMetaData();

			String[] columns = new String[columnSize];
			for (int i = 0; i < columnSize; i++) {
				columns[i] = rsmd.getColumnName(i + 1);
			}
			while (rs.next()) {
				Map<String, Object> resultMap = new HashMap<>();
				for (int j = 0; j < columnSize; j++) {
					resultMap.put(columns[j], rs.getObject(columns[j]));
				}
				resultList.add(resultMap);
			}
		} catch (SQLException e) {
			logger.error("postgre SQL 에러 ");
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("postgre connection close 에러 ");
			}
		}
		return resultList;
	}

}
