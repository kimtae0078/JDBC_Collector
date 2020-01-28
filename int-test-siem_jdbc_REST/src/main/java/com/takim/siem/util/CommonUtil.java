package com.takim.siem.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.takim.siem.model.JdbcInfo;
import com.takim.siem.model.KeyInfo;

public class CommonUtil{

	private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	// ResultSet 컬럼 개수 산출
	public static int rsColumnCount(ResultSet rs) {
		int columnSize = 0;
		try {
			java.sql.ResultSetMetaData metaData = rs.getMetaData();
			columnSize = metaData.getColumnCount();
		} catch (SQLException e) {
			logger.error("resultSet 컬럼 개수 메소드 에러");
		}
		return columnSize;
	}

	// sql문 생성
	public static String makeSQL(JdbcInfo jdbcVO, KeyInfo keyVO) {
		
		StringBuffer query = new StringBuffer();
        String table = jdbcVO.getTable();
        String orderKey1 = keyVO.getOrderColKey1();
        String uniqKey1 = keyVO.getUniqColKey1();
        String orderVal1 = keyVO.getOrderColVal1();
        String uniqVal1 = keyVO.getUniqColVal1();
        
        String orderKey2 = keyVO.getOrderColKey2();
        String uniqKey2 = keyVO.getUniqColKey2();
        String orderVal2 = keyVO.getOrderColVal2();
        String uniqVal2 = keyVO.getUniqColVal2();
        
        if(orderVal1 != null && !orderVal1.equals("") && uniqVal1 != null && !uniqVal1.equals("")) {
			if (orderVal2 != null && !orderVal2.equals("") && uniqVal2 != null && !uniqVal2.equals("")) {
				query.append("SELECT * FROM (SELECT * FROM " + table + " ORDER BY " + uniqKey1 + ", " + uniqKey2 + " DESC) tb WHERE "
						+ orderKey1 + " > '" + orderVal1 + "' AND " + orderKey2 + " > '" + orderVal2 + "' ORDER BY " + orderKey1 + ", " + orderKey2 + " ASC");
			} else {
				query.append("SELECT * FROM (SELECT * FROM " + table + " ORDER BY " + uniqKey1 + " DESC) tb WHERE "
						+ orderKey1 + " > '" + orderVal1 + "' ORDER BY " + orderKey1 + " ASC");
			}
        }else {
        	query.append("SELECT * FROM (SELECT * FROM " + table + " ORDER BY " + uniqKey1 + " desc) tb ORDER BY " + orderKey1 + " ASC");
        }
		return query.toString();
	}

	public static void setLastKeyValue(ResultSet rs, KeyInfo keyVO) {
        try {
        	rs.last();
			keyVO.setOrderColVal1(rs.getString(keyVO.getOrderColKey1()));
			keyVO.setUniqColVal1(rs.getString(keyVO.getUniqColKey1()));
	        if(!keyVO.getOrderColKey2().equals("") && !keyVO.getUniqColKey2().equals("")) {
	        	keyVO.setOrderColVal2(rs.getString(keyVO.getOrderColKey2()));
	        	keyVO.setUniqColVal2(rs.getString(keyVO.getUniqColKey2()));
	        }
		} catch (SQLException e) {
			logger.error("resultSet offset값 model 저장 에러");
		}
	}
	
	public static JdbcInfo jdbcInfoParsing(String jdbcJson) {

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jdbcJson);
		
		Gson gson = new Gson();
		JdbcInfo jdbcVO = gson.fromJson(element, JdbcInfo.class);
		
		return jdbcVO;
	}

}
