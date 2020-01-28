package com.takim.siem.model;

public class KeyInfo {

	private String idx;
	
	private String jdbcIdx;
	
	private String orderColKey1;
	
	private String orderColKey2;
	
	private String uniqColKey1;
	
	private String uniqColKey2;
	
	private String orderColVal1;
	
	private String orderColVal2;
	
	private String uniqColVal1;
	
	private String uniqColVal2;

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getJdbcIdx() {
		return jdbcIdx;
	}

	public void setJdbcIdx(String jdbcIdx) {
		this.jdbcIdx = jdbcIdx;
	}

	public String getOrderColKey1() {
		return orderColKey1;
	}

	public void setOrderColKey1(String orderColKey1) {
		this.orderColKey1 = orderColKey1;
	}

	public String getOrderColKey2() {
		return orderColKey2;
	}

	public void setOrderColKey2(String orderColKey2) {
		this.orderColKey2 = orderColKey2;
	}

	public String getUniqColKey1() {
		return uniqColKey1;
	}

	public void setUniqColKey1(String uniqColKey1) {
		this.uniqColKey1 = uniqColKey1;
	}

	public String getUniqColKey2() {
		return uniqColKey2;
	}

	public void setUniqColKey2(String uniqColKey2) {
		this.uniqColKey2 = uniqColKey2;
	}

	public String getOrderColVal1() {
		return orderColVal1;
	}

	public void setOrderColVal1(String orderColVal1) {
		this.orderColVal1 = orderColVal1;
	}

	public String getOrderColVal2() {
		return orderColVal2;
	}

	public void setOrderColVal2(String orderColVal2) {
		this.orderColVal2 = orderColVal2;
	}

	public String getUniqColVal1() {
		return uniqColVal1;
	}

	public void setUniqColVal1(String uniqColVal1) {
		this.uniqColVal1 = uniqColVal1;
	}

	public String getUniqColVal2() {
		return uniqColVal2;
	}

	public void setUniqColVal2(String uniqColVal2) {
		this.uniqColVal2 = uniqColVal2;
	}

	@Override
	public String toString() {
		return "KeyInfo [idx=" + idx + ", jdbcIdx=" + jdbcIdx + ", orderColKey1=" + orderColKey1 + ", orderColKey2="
				+ orderColKey2 + ", uniqColKey1=" + uniqColKey1 + ", uniqColKey2=" + uniqColKey2 + ", orderColVal1="
				+ orderColVal1 + ", orderColVal2=" + orderColVal2 + ", uniqColVal1=" + uniqColVal1 + ", uniqColVal2="
				+ uniqColVal2 + "]";
	}
	
	
	
}


