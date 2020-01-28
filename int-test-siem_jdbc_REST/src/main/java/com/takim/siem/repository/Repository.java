package com.takim.siem.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.takim.siem.model.JdbcInfo;
import com.takim.siem.model.KeyInfo;

public class Repository {

	private final static Logger logger = LoggerFactory.getLogger(Repository.class);

	public static List<JdbcInfo> selectIsCollectTrue() {
		List<JdbcInfo> jdbcVoList = new ArrayList<JdbcInfo>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String url = "jdbc:postgresql://172.16.100.171/test_db";
		String id = "test_user";
		String pw = "passw0rd";

		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("repository postgrsql driver 연결 오류");
			}

			conn = DriverManager.getConnection(url, id, pw);

			String sql = "SELECT * FROM jdbc_info WHERE is_collect=true";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JdbcInfo jdbcVO = new JdbcInfo();
				
				jdbcVO.setIdx(rs.getString("id"));
				jdbcVO.setDate(rs.getString("date"));
				jdbcVO.setDbms(rs.getString("dbms"));
				jdbcVO.setIp(rs.getString("ip"));
				jdbcVO.setPort(rs.getInt("port"));
				jdbcVO.setUsername(rs.getString("username"));
				jdbcVO.setPassword(rs.getString("password"));
				jdbcVO.setTable(rs.getString("tb"));
				jdbcVO.setCollect(rs.getBoolean("is_collect"));
				jdbcVO.setIntervalTime(rs.getInt("interval_time"));
				jdbcVO.setDatabaseName(rs.getString("database_name"));
				
				jdbcVoList.add(jdbcVO);
			}
		} catch (SQLException e) {
			logger.error("repository postgre SQL 에러 ");
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
				logger.error("repository postgre connection close 에러 ");
			}
		}
		return jdbcVoList;
	}

	public static JdbcInfo selectJdbcInfo(String idx) {
		JdbcInfo jdbcVO = new JdbcInfo();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String url = "jdbc:postgresql://172.16.100.171/test_db";
		String id = "test_user";
		String pw = "passw0rd";

		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("repository postgrsql driver 연결 오류");
			}
			
			conn = DriverManager.getConnection(url, id, pw);

			String sql = "SELECT * FROM jdbc_info WHERE id='" + idx + "'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				jdbcVO.setIdx(rs.getString("id"));
				jdbcVO.setDate(rs.getString("date"));
				jdbcVO.setDbms(rs.getString("dbms"));
				jdbcVO.setIp(rs.getString("ip"));
				jdbcVO.setPort(rs.getInt("port"));
				jdbcVO.setUsername(rs.getString("username"));
				jdbcVO.setPassword(rs.getString("password"));
				jdbcVO.setTable(rs.getString("tb"));
				jdbcVO.setCollect(rs.getBoolean("is_collect"));
				jdbcVO.setIntervalTime(rs.getInt("interval_time"));
				jdbcVO.setDatabaseName(rs.getString("database_name"));
			}
		} catch (SQLException e) {
			logger.error("repository postgre SQL 에러 ");
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
				logger.error("repository postgre connection close 에러 ");
			}
		}
		return jdbcVO;
	}

	public KeyInfo selectKeyInfo(String idx) {
		KeyInfo keyVO = new KeyInfo();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String url = "jdbc:postgresql://172.16.100.171/test_db";
		String id = "test_user";
		String pw = "passw0rd";

		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("repository postgrsql driver 연결 오류");
			}
			
			conn = DriverManager.getConnection(url, id, pw);

			String sql = "SELECT * FROM key_info WHERE jdbc_idx='" + idx + "'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				keyVO.setIdx(rs.getString("idx"));
				keyVO.setJdbcIdx(rs.getString("jdbc_idx"));
				keyVO.setOrderColKey1(rs.getString("order_col_key1"));
				keyVO.setOrderColKey2(rs.getString("order_col_key2"));
				keyVO.setOrderColVal1(rs.getString("order_col_val1"));
				keyVO.setOrderColVal2(rs.getString("order_col_val2"));
				keyVO.setUniqColKey1(rs.getString("uniq_col_key1"));
				keyVO.setUniqColKey2(rs.getString("uniq_col_key2"));
				keyVO.setUniqColVal1(rs.getString("uniq_col_val1"));
				keyVO.setUniqColVal2(rs.getString("uniq_col_val2"));
			}
		} catch (SQLException e) {
			logger.error("repository postgre SQL 에러 ");
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
				logger.error("repository postgre connection close 에러 ");
			}
		}
		return keyVO;
	}

	public void updateLastKeyValue(KeyInfo keyVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		String url = "jdbc:postgresql://172.16.100.171/test_db";
		String id = "test_user";
		String pw = "passw0rd";

		try {
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				logger.error("repository postgrsql driver 연결 오류");
			}
			
			conn = DriverManager.getConnection(url, id, pw);

			String sql = "UPDATE key_info SET order_col_val1=?, uniq_col_val1=?, order_col_val2=?, uniq_col_val2=? WHERE jdbc_idx='"
					+ keyVO.getJdbcIdx() + "'";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyVO.getOrderColVal1());
			pstmt.setString(2, keyVO.getUniqColVal1());
			
			if (!keyVO.getOrderColKey2().equals("") && !keyVO.getUniqColKey2().equals("")) {
				pstmt.setString(3, keyVO.getOrderColVal2());
				pstmt.setString(4, keyVO.getUniqColVal2());
			} else {
				pstmt.setString(3, "");
				pstmt.setString(4, "");
			}
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			logger.error("repository postgre SQL 에러 ");
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error("repository postgre connection close 에러 ");
			}
		}
	}

}
