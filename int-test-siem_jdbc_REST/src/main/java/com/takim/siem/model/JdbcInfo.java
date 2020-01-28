package com.takim.siem.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

public class JdbcInfo {

	private String idx;
	
	private String date;
	
	private String dbms;
	
	private String ip;
	
	private int port;
	
	private String username;
	
	private String password;
	
	private String table;
	
	private boolean isCollect;

	private int intervalTime;

	private String databaseName;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDbms() {
		return dbms;
	}

	public void setDbms(String dbms) {
		this.dbms = dbms;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public String toString() {
		return "JdbcInfo [idx=" + idx + ", date=" + date + ", dbms=" + dbms + ", ip=" + ip + ", port=" + port
				+ ", username=" + username + ", password=" + password + ", table=" + table + ", isCollect=" + isCollect
				+ ", intervalTime=" + intervalTime + ", databaseName=" + databaseName + "]";
	}

	
	
	
}
